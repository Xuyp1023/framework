package com.betterjr.modules.document.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.CheckDataResult;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.document.utils.FileManager;
import com.betterjr.modules.document.utils.FileManagerFactory;

/**
 * 根据文档的业务类型，将文档保存到不同的存储设备上
 * 
 * @author zhoucy
 *
 */
@Service
public class DataStoreService {
    private static final Logger logger = LoggerFactory.getLogger(DataStoreService.class);
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService fileItemService;

    @Reference(interfaceClass = IAgencyAuthFileGroupService.class)
    private IAgencyAuthFileGroupService fileGroupService;

    /**
     * 保存本地文档到存储
     * 
     * @param anFile
     *            本地档件
     * @param anFileInfoType
     *            文档业务类型
     * @return
     */
    public String saveFileData(final File anFile, final String anFileInfoType) {
        InputStream inData = null;
        try {
            inData = new FileInputStream(anFile);
            return saveStreamData(inData, anFileInfoType);
        }
        catch (final FileNotFoundException e) {

            return "";
        }
        finally {
            IOUtils.closeQuietly(inData);
        }
    }

    /**
     * 保存数据流到存储
     * 
     * @param anStream
     *            数据流
     * @param anFileInfoType
     *            文档业务类型
     * @return
     */
    public String saveStreamData(final InputStream anStream, final String anFileInfoType) {
        final FileStoreType storeType = this.fileGroupService.findFileStoreType(anFileInfoType);
        final FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
        final String filePath = this.fileGroupService.findCreateFilePath(anFileInfoType);
        if (fileManager.save(filePath, anStream)) {

            return filePath;
        } else {

            return "";
        }
    }

    /**
     * 保存文件到存储；用于后端业务调用
     * 
     * @param anFile
     *            文件
     * @param anFileInfoType
     *            文件类型
     * @param anFileName
     *            文件名称
     * @return
     */
    public CustFileItem saveFileToStoreWithBatchNo(final File anFile, final String anFileInfoType,
            final String anFileName) {

        return subSaveFileToStore(anFile, anFileInfoType, anFileName, true);
    }

    public CustFileItem saveFileToStore(final File anFile, final String anFileInfoType, final String anFileName) {

        return subSaveFileToStore(anFile, anFileInfoType, anFileName, false);
    }

    public CustFileItem subSaveFileToStore(final File anFile, final String anFileInfoType, final String anFileName,
            final boolean anBatchNo) {
        InputStream inData = null;
        try {
            inData = new FileInputStream(anFile);
            return subSaveStreamToStore(inData, anFileInfoType, anFileName, anBatchNo);
        }
        catch (final FileNotFoundException e) {
            return null;
        }
        finally {
            IOUtils.closeQuietly(inData);
        }
    }

    /**
     * 保存数据流到存储；用于后端业务调用
     * 
     * @param anStream
     *            数据流
     * @param anFileInfoType
     *            文档业务类型
     * @param anFileName
     *            文件名称
     * @return 文件信息
     */
    public CustFileItem saveStreamToStoreWithBatchNo(final InputStream anStream, final String anFileInfoType,
            final String anFileName) {

        return subSaveStreamToStore(anStream, anFileInfoType, anFileName, true);
    }

    public CustFileItem saveStreamToStore(final InputStream anStream, final String anFileInfoType,
            final String anFileName) {

        return subSaveStreamToStore(anStream, anFileInfoType, anFileName, false);
    }

    private CustFileItem subSaveStreamToStore(final InputStream anStream, final String anFileInfoType,
            final String anFileName, final boolean anWithBatchNo) {
        try {
            final FileStoreType storeType = this.fileGroupService.findFileStoreType(anFileInfoType);
            final String tmpExt = FileUtils.extractFileExt(anFileName);
            final CheckDataResult checkResult = this.fileGroupService.findFileTypePermit(anFileInfoType, tmpExt);
            if (checkResult.isOk() == false) {
                throw new BytterTradeException(123456,
                        "保存的文件类型是【" + tmpExt + "】, 系统允许的文件类型是【" + checkResult.getMessage() + "】， 请检查！");
            }
            final FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
            final String tmpFilePath = this.fileGroupService.findCreateFilePath(anFileInfoType);
            logger.info("save stream info is storeType:" + storeType + ", tmpFilePath=" + tmpFilePath);
            if (fileManager.save(tmpFilePath, anStream)) {
                logger.info("data store is storeType:" + storeType + ", tmpFilePath=" + tmpFilePath);
                final long dataSize = fileManager.findSize(tmpFilePath);
                return fileItemService.saveAndUpdateFileItem(tmpFilePath, dataSize, anFileInfoType, anFileName,
                        storeType, anWithBatchNo);
            }
        }
        catch (final BytterTradeException ex) {
            throw ex;
        }
        catch (final Exception ex) {
            logger.error("保存文件出现异常！", ex);
        }
        return null;
    }

    /**
     * 保存数据到存储；用于WEB端业务调用
     * 
     * @param anStream
     *            数据流
     * @param anFileInfoType
     *            文档业务类型
     * @param anFileName
     *            文件名称
     * @return 直接返回WEB端使用的JSON格式内容
     */
    public String webSaveStreamToStore(final InputStream anStream, final String anFileInfoType,
            final String anFileName) {
        final CustFileItem tmpFileItem = saveStreamToStore(anStream, anFileInfoType, anFileName);
        if (tmpFileItem != null) {

            return AjaxObject.newOk("上传文件成功", tmpFileItem).toJson();
        }

        return AjaxObject.newOk("上传文件失败").toJson();
    }

    /**
     * 跟据BatchNo，加载文档数据流
     * 
     * @param batchNo
     */
    public InputStream loadFromStoreByBatchNo(final Long anBatchNo) {
        final CustFileItem fileItem = this.fileItemService.findOneByBatchNo(anBatchNo);
        BTAssert.notNull(fileItem, "没有找到fileItem");
        return loadFromStore(fileItem);
    }

    /**
     * 根据文档ID信息，加载文档数据流
     * 
     * @param anFileId
     *            文档ID
     * @return
     */
    public InputStream loadFromStore(final Long anFileId) {
        final CustFileItem fileItem = this.fileItemService.findOne(anFileId);
        return loadFromStore(fileItem);
    }

    /**
     * 根据文档信息，加载数据流
     * 
     * @param anFileItem
     *            文档信息
     * @return
     */
    public InputStream loadFromStore(final CustFileItem anFileItem) {
        if (anFileItem != null) {
            return loadFromStore(anFileItem.getFilePath(), FileStoreType.checking(anFileItem.getStoreType()));
        }
        return null;
    }

    /**
     * 根据文档类型和文件路径，加载数据流
     * 
     * @param anFilePath
     *            文档路径
     * @param anStoreType
     *            存储类型
     * @return
     */
    public InputStream loadFromStore(final String anFilePath, final FileStoreType anStoreType) {
        final FileManager fileManager = FileManagerFactory.create(anStoreType, fileGroupService);
        return fileManager.load(anFilePath);
    }

    /**
     * 根据文档信息检查文档是否存在
     * 
     * @param anItem
     *            文档信息
     * @return
     */
    public boolean exists(final CustFileItem anItem) {
        if (anItem == null) {
            return false;
        }
        return exists(anItem.getFilePath(), anItem.getStoreType());
    }

    /**
     * 根据文档路径和文档类型，检查文档是否存在
     * 
     * @param anFilePath
     *            文档路径
     * @param anStoreType
     *            文档类型
     * @return
     */
    public boolean exists(final String anFilePath, final String anStoreType) {
        final FileStoreType storeType = FileStoreType.checking(anStoreType);
        final FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
        return fileManager.exists(anFilePath);
    }

    /**
     * 根据文档路径和文档类型，获得文档的大小
     * 
     * @param anFilePath
     *            文档路径
     * @param anStoreType
     *            文档类型
     * @return
     */
    public long findSize(final String anFilePath, final String anStoreType) {
        final FileStoreType storeType = FileStoreType.checking(anStoreType);
        final FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);

        return fileManager.findSize(anFilePath);
    }

    /**
     * 加载数据字节流
     * 
     * @param anFileId
     * @return
     */
    public byte[] loadDataFromStore(final Long anFileId) {

        return subLoadData(anFileId, true);
    }

    private byte[] subLoadData(final Long anFileId, final boolean anUserFileId) {
        InputStream inStream = null;
        byte[] bbs = null;
        if (anUserFileId) {
            inStream = loadFromStore(anFileId);
        } else {
            inStream = loadFromStoreByBatchNo(anFileId);
        }
        if (inStream != null) {
            try {
                bbs = IOUtils.toByteArray(inStream);
            }
            catch (final IOException ex) {}
            finally {
                IOUtils.closeQuietly(inStream);
            }
        }

        return bbs;
    }

    public byte[] loadDataFromStoreByBatchNo(final Long anBatchNo) {

        return subLoadData(anBatchNo, false);
    }

    /**
     * PDF转图片，标准大小；即A4纸大小的图片列表;920个像素
     * 
     * @param anPdfData
     * @return
     */
    public Long savePdf2ImageStand(final InputStream anPdfData) {

        return pdf2Image(anPdfData, 1.52f, 0);
    }

    /**
     * PDF转图片，缩微图大小120个像素
     * 
     * @param anPdfData
     * @return
     */
    public Long savePdf2ImageSmall(final InputStream anPdfData) {

        return pdf2Image(anPdfData, 0.2f, 0);
    }

    private Long pdf2Image(final InputStream anPdfData, final float anScale, final float anRotation) {
        final List<File> tmpList = CustFileUtils.pdfChangeToImage(anPdfData, anScale, anRotation);
        InputStream tmpIn = null;
        final StringBuilder sb = new StringBuilder();
        try {
            int index = 0;
            for (final File file : tmpList) {
                tmpIn = new FileInputStream(file);
                final CustFileItem tmpItem = saveStreamToStore(tmpIn, "otherFile",
                        "image".concat(Integer.toString(index)).concat(".jpeg"));
                IOUtils.closeQuietly(tmpIn);
                if (index > 0) {
                    sb.append(",");
                }
                sb.append(Long.toString(tmpItem.getId()));
                index = index + 1;
            }

            return this.fileItemService.updateCustFileItemInfo(sb.toString(), null);
        }
        catch (final IOException ex) {
            IOUtils.closeQuietly(tmpIn);
            throw BytterException.unchecked(ex);
        }
        finally {
            for (final File ff : tmpList) {
                if (ff.delete() == false) {
                    logger.warn("delete temp file :" + ff.getPath() + ", has error");
                }
            }
        }
    }
}