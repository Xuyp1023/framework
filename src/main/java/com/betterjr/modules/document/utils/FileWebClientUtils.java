package com.betterjr.modules.document.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.betterjr.common.exception.BettjerIOException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MimeTypesHelper;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;

public class FileWebClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileWebClientUtils.class);

    /**
     * 下载文件
     *
     * @param response
     * @param anFileItem
     */
    public static void fileDownload(DataStoreService anDataStoreService, final HttpServletResponse response,
            final CustFileItem anFileItem) {

        fileDownloadWithOpenType(anDataStoreService, response, anFileItem, null);
    }

    public static void fileDownloadWithOpenType(DataStoreService anDataStoreService, final HttpServletResponse response,
            final CustFileItem anFileItem, final String anOpenType) {
        OutputStream os = null;
        String msg = null;
        try {
            if (anFileItem != null) {
                if (anDataStoreService.exists(anFileItem)) {
                    String openType = anOpenType;
                    if (StringUtils.isBlank(openType)) {
                        if (anFileItem.isInner(MimeTypesHelper.getMimeType(anFileItem.getFileType()))) {
                            openType = "inline";
                        } else {
                            openType = "attachment";
                        }
                    }
                    final String fileName = anFileItem.getFileName();
                    final StringBuilder sb = new StringBuilder(100);
                    sb.append(openType).append("; ").append("filename=")
                            .append(java.net.URLEncoder.encode(fileName, "UTF-8"));
                    os = response.getOutputStream();
                    response.setHeader("Content-Disposition", sb.toString());
                    response.setContentType(anFileItem.getFileType());
                    IOUtils.copy(anDataStoreService.loadFromStore(anFileItem), os);
                    return;
                } else {
                    msg = "file does not exist";
                }
            } else {
                msg = "No information was obtained for the download file";
            }
        }
        catch (final IOException e) {
            logger.error("下载文件失败，请检查；" + anFileItem, e);
            msg = "出现IO异常，请稍后再试!";
        }
        finally {
            if (msg != null) {
                response.reset();
                response.setContentType("text/html;UTF-8");
                try {
                    response.getWriter().append(msg);
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            IOUtils.closeQuietly(os);
        }
    }

    public static void fileDownloadWithFileName(final HttpServletResponse anResponse, final byte[] anData,
            final String anFileName) {
        String msg = null;
        OutputStream os = null;
        try (InputStream is = new ByteArrayInputStream(anData)) {
            final StringBuilder sb = new StringBuilder(100);
            sb.append("attachment").append("; ").append("filename=")
                    .append(java.net.URLEncoder.encode(anFileName, "UTF-8"));
            os = anResponse.getOutputStream();
            anResponse.setHeader("Content-Disposition", sb.toString());
            anResponse.setContentType("cer");
            IOUtils.copy(is, os);
            return;
        }
        catch (final IOException e) {
            logger.error("下载文件失败，请检查；" + anFileName, e);
            msg = "出现IO异常，请稍后再试!";
        }
        finally {
            if (msg != null) {
                anResponse.reset();
                anResponse.setContentType("text/html;UTF-8");
                try {
                    anResponse.getWriter().append(msg);
                }
                catch (final IOException e) {
                    logger.error("关闭文件流失败；" + anFileName, e);
                }
            }
            IOUtils.closeQuietly(os);
        }
    }

    public static void directExportPDF(final String anOutFileName, final Map<String, Object> anParam,
            final HttpServletResponse response, final String anModeFile, final String anModeName) {
        final FreemarkerService markerService = SpringContextHolder.getBean(FreemarkerService.class);
        final StringBuffer data = markerService.processTemplateByFileNameUnderModule(anModeFile, anParam, anModeName);
        String msg = null;
        try {
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + java.net.URLEncoder.encode(anOutFileName, "UTF-8"));
            CustFileUtils.exportPDF(data, response.getOutputStream());
        }
        catch (final IOException e) {
            msg = "出现文件异常，请稍后再试!";
        }
        catch (final BettjerIOException e) {
            msg = e.getMessage();
        }
        finally {
            if (msg != null) {
                try {
                    response.reset();
                    response.setContentType("text/html;UTF-8");
                    response.getWriter().append(msg);
                }
                catch (final IOException e) {
                    logger.error("directExportPDF can't output exception message", e);
                }
            }
        }
    }

    public String createWebUploadFile(IAgencyAuthFileGroupService anFileGroupService, MultipartFile anMultFile,
            String anFileInfoType) {
        FileStoreType storeType = anFileGroupService.findFileStoreType(anFileInfoType);
        String tmpWorkPath = anFileGroupService.findCreateFilePath(anFileInfoType);

        return tmpWorkPath;
    }

    public static void fileMultipleDownload(DataStoreService anDataStoreService, HttpServletResponse anResponse,
            List<CustFileItem> anFileItemList, String anFileName) {
        ZipOutputStream out = null;
        String msg = null;
        // 设置下载名称
        if (StringUtils.isBlank(anFileName)) {
            anFileName = "packedFile";
        }
        try {
            // 文件设置为下载保存
            anResponse.setHeader("Content-Disposition",
                    "attachment; filename=" + java.net.URLEncoder.encode(anFileName + ".zip", "UTF-8"));
            anResponse.setContentType("zip");
            // 建立zip
            out = new ZipOutputStream(anResponse.getOutputStream(), Charset.forName("UTF-8"));
            for (CustFileItem anFile : anFileItemList) {
                if (anDataStoreService.exists(anFile)) {
                    // 放入压缩文件项,为避免文件名重复，后面加入随机串
                    String tmpFileName = anFile.getFileName().substring(0, anFile.getFileName().lastIndexOf("."));
                    out.putNextEntry(
                            new ZipEntry(tmpFileName + SerialGenerator.randomBase62(4) + "." + anFile.getFileType()));
                    // 写入数据
                    IOUtils.copy(anDataStoreService.loadFromStore(anFile), out);
                    out.closeEntry();
                }
            }
        }
        catch (IOException e) {
            logger.error("下载文件失败，请检查；", e);
            msg = "出现IO异常，请稍后再试!";
        }
        finally {
            if (msg != null) {
                anResponse.reset();
                anResponse.setContentType("text/html;UTF-8");
                try {
                    anResponse.getWriter().append(msg);
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            // 关闭流
            IOUtils.closeQuietly(out);
        }

    }
}
