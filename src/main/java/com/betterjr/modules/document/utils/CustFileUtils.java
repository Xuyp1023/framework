package com.betterjr.modules.document.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.modules.document.data.DownloadFileInfo;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.entity.CustFileItem;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public abstract class CustFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(CustFileUtils.class);

    /**
     * 创建签名的文档信息
     * 
     * @param anFile
     *            签名的文件信息
     * @param anType
     *            文件类型
     * @param anFileName
     *            签名的文件名称
     * @param anFilePath
     *            签名的文件路径
     * @return
     */

    public static CustFileItem createSignDocFileItem(final String anFilePath, final long anSize,
            final String anWorkType, final String anFileName) {
        final CustFileItem fileItem = createDefFileItem(anFilePath, anSize, anWorkType, anFileName);
        fileItem.setBatchNo(findBatchNo());

        return fileItem;
    }

    private static CustFileItem createDefFileItem(final String anFilePath, final long anSize, final String anWorkType,
            final String anFileName) {
        final CustFileItem fileItem = new CustFileItem();
        fileItem.setId(SerialGenerator.getLongValue("CustFileItem.id"));
        fileItem.setFileLength(anSize);
        fileItem.setFilePath(anFilePath);
        fileItem.setFileInfoType(anWorkType);
        fileItem.setFileName(anFileName);
        fileItem.setBatchNo(0L);
        fileItem.setFileType(FileUtils.extractFileExt(anFileName));

        return fileItem;
    }

    public static CustFileItem createDefFileItemForStore(final String filePath, final Long fileLength,
            final String anWorkType, final String anFileName) {
        final CustFileItem fileItem = new CustFileItem();
        fileItem.setId(SerialGenerator.getLongValue("CustFileItem.id"));
        fileItem.setFileLength(fileLength);
        fileItem.setFilePath(filePath);
        fileItem.setFileInfoType(anWorkType);
        fileItem.setFileName(anFileName);
        fileItem.setBatchNo(0L);
        fileItem.setFileType(FileUtils.extractFileExt(anFileName));

        return fileItem;
    }

    /**
     * 创建上传文件的信息
     * 
     * @param anFileInfo
     *            文件路径信息
     * @param anWorkType
     *            文档业务类型
     * @param anFileName
     *            文件名称
     * @param anFileType
     *            文件类型
     * @return
     */
    public static CustFileItem createUploadFileItem(final String anFilePath, final long anSize, final String anWorkType,
            final String anFileName) {
        final CustFileItem fileItem = createDefFileItem(anFilePath, anSize, anWorkType, anFileName);

        return fileItem;
    }

    private static final FileStoreType fileStoreType = FileStoreType.OSS_STORE;

    public static Long findBatchNo() {

        return SerialGenerator.getLongValue("CustFileInfo.id");
    }

    /**
     * 将上传的文件持续化。
     *
     * @param anFileInfo
     * @param anInput
     * @return
     */
    public static boolean saveFileStream(final KeyAndValueObject anFileInfo, final InputStream anInput) {
        final File tmpFile = (File) anFileInfo.getValue();
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tmpFile);
            IOUtils.copy(anInput, outStream);
            return true;
        }
        catch (final IOException ex) {
            tmpFile.delete();
            return false;
        }
        finally {
            IOUtils.closeQuietly(outStream);
        }
    }

    /**
     * 输出PDF文件
     * 
     * @param anSb
     * @param anOut
     */
    public static void exportPDF(final StringBuffer anSb, final OutputStream anOut) {
        final Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        document.setMargins(0, 0, 0, 0);
        PdfWriter pdfwriter = null;
        try {
            pdfwriter = PdfWriter.getInstance(document, anOut);
            pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
            document.open();
            document.newPage();
            final HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);

            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            final CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            final Pipeline pipeline = new CssResolverPipeline(cssResolver,
                    new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfwriter)));

            final XMLWorker worker = new XMLWorker(pipeline, true);
            final XMLParser p = new XMLParser(worker);
            System.out.println(anSb.toString());
            final StringReader reader = new StringReader(anSb.toString());
            p.parse(reader);
            p.flush();
        }
        catch (IOException | DocumentException ex) {
            throw new BytterTradeException(30002, "产生PDF报告文件出现异常，请稍后再试", ex);
        }
        finally {
            document.close();
            pdfwriter.close();
        }
    }

    public static DownloadFileInfo sendToDownloadFileService(final CustFileItem anFileItem, final Long anCustNo,
            final String anPartnerCode, final String anBusingType) {
        final DownloadFileInfo fileInfo = BeanMapper.map(anFileItem, DownloadFileInfo.class);
        fileInfo.setBusinType(anBusingType);
        fileInfo.setCustNo(anCustNo);
        fileInfo.setPartnerCode(anPartnerCode);
        final String tmpToken = SerialGenerator.uuid().concat(Long.toHexString(System.currentTimeMillis()))
                .concat(SerialGenerator.randomBase62(20));
        fileInfo.setAccessToken(tmpToken);
        DownloadFileService.addDownloadFile(fileInfo);
        return fileInfo;
    }

    /**
     * 将PDF文件转换为图片列表
     * 
     * @param anPdfData
     *            PDF文件流
     * @param anScale
     *            缩放比例
     * @param anRotation
     *            旋转角度
     * @return
     */
    public static List<File> pdfChangeToImage(final InputStream anPdfData, final float anScale,
            final float anRotation) {
        final org.icepdf.core.pobjects.Document document = new org.icepdf.core.pobjects.Document();
        final List<File> resultList = new ArrayList();
        try {
            document.setInputStream(anPdfData, File.createTempFile("pdf2ImageSource", "pdf").getPath());
            // final float scale = 1.52f;// 缩放比例（大图）
            // float scale = 0.2f;// 缩放比例（小图）
            // final float rotation = 0f;// 旋转角度
            final Long startTime = System.currentTimeMillis();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                final BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.PRINT,
                        org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, anRotation, anScale);
                final File file = File.createTempFile("pdf2Image", ".jpeg");
                ImageIO.write(image, "jpeg", file);
                image.flush();
                resultList.add(file);
            }
            System.out.println("this spend time :" + (System.currentTimeMillis() - startTime));
            return resultList;
        }
        catch (final IOException e1) {
            for (final File ff : resultList) {
                if (ff.exists()) {
                    ff.delete();
                }
            }
            throw new BytterDeclareException("pdf2Image has error");
        }
        finally {
            document.dispose();
        }
    }
}