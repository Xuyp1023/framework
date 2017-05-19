package com.betterjr.modules.document.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.CheckDataResult;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.entity.CustFileItem;
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
    public String saveFileData(File anFile, String anFileInfoType) {
        InputStream inData = null;
        try {
            inData = new FileInputStream(anFile);
            return saveStreamData(inData, anFileInfoType);
        }
        catch (FileNotFoundException e) {

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
    public String saveStreamData(InputStream anStream, String anFileInfoType) {
        FileStoreType storeType = this.fileGroupService.findFileStoreType(anFileInfoType);
        FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
        String filePath = this.fileGroupService.findCreateFilePath(anFileInfoType);
        if (fileManager.save(filePath, anStream)) {

            return filePath;
        }
        else {

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
    public CustFileItem saveFileToStoreWithBatchNo(File anFile, String anFileInfoType, String anFileName) {

        return subSaveFileToStore(anFile, anFileInfoType, anFileName, true);
    }

    public CustFileItem saveFileToStore(File anFile, String anFileInfoType, String anFileName) {

        return subSaveFileToStore(anFile, anFileInfoType, anFileName, false);
    }

    public CustFileItem subSaveFileToStore(File anFile, String anFileInfoType, String anFileName, boolean anBatchNo) {
        InputStream inData = null;
        try {
            inData = new FileInputStream(anFile);
            return subSaveStreamToStore(inData, anFileInfoType, anFileName, anBatchNo);
        }
        catch (FileNotFoundException e) {
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
    public CustFileItem saveStreamToStoreWithBatchNo(InputStream anStream, String anFileInfoType, String anFileName) {

        return subSaveStreamToStore(anStream, anFileInfoType, anFileName, true);
    }

    public CustFileItem saveStreamToStore(InputStream anStream, String anFileInfoType, String anFileName) {

        return subSaveStreamToStore(anStream, anFileInfoType, anFileName, false);
    }

    private CustFileItem subSaveStreamToStore(InputStream anStream, String anFileInfoType, String anFileName, boolean anWithBatchNo) {
        try {
            FileStoreType storeType = this.fileGroupService.findFileStoreType(anFileInfoType);
           String tmpExt = FileUtils.extractFileExt(anFileName);
            CheckDataResult checkResult = this.fileGroupService.findFileTypePermit(anFileInfoType, tmpExt);
            if (checkResult.isOk() == false){
                throw new BytterTradeException(123456, "保存的文件类型是【"+ tmpExt +"】, 系统允许的文件类型是【"+checkResult.getMessage()+"】， 请检查！");
            }
            FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
            String tmpFilePath = this.fileGroupService.findCreateFilePath(anFileInfoType);
            logger.info("save stream info is storeType:" + storeType +", tmpFilePath=" + tmpFilePath);
            if (fileManager.save(tmpFilePath, anStream)) {
                logger.info("data store is storeType:" + storeType +", tmpFilePath=" + tmpFilePath);
                long dataSize = fileManager.findSize(tmpFilePath);
                return fileItemService.saveAndUpdateFileItem(tmpFilePath, dataSize, anFileInfoType, anFileName, storeType, anWithBatchNo);
            }
        }
        catch (BytterTradeException ex) {
            throw ex;
        }
        catch (Exception ex) {
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
    public String webSaveStreamToStore(InputStream anStream, String anFileInfoType, String anFileName) {
        CustFileItem tmpFileItem = saveStreamToStore(anStream, anFileInfoType, anFileName);
        if (tmpFileItem != null) {

            return AjaxObject.newOk("上传文件成功", tmpFileItem).toJson();
        }

        return AjaxObject.newOk("上传文件失败").toJson();
    }

    /**
     * 跟据BatchNo，加载文档数据流
     * @param batchNo
     */
	public InputStream loadFromStoreByBatchNo(Long anBatchNo) {
		CustFileItem fileItem = this.fileItemService.findOneByBatchNo(anBatchNo);
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
    public InputStream loadFromStore(Long anFileId) {
        CustFileItem fileItem = this.fileItemService.findOne(anFileId);
        return loadFromStore(fileItem);
    }

    /**
     * 根据文档信息，加载数据流
     * 
     * @param anFileItem
     *            文档信息
     * @return
     */
    public InputStream loadFromStore(CustFileItem anFileItem) {
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
    public InputStream loadFromStore(String anFilePath, FileStoreType anStoreType) {
        FileManager fileManager = FileManagerFactory.create(anStoreType, fileGroupService);
        return fileManager.load(anFilePath);
    }

    /**
     * 根据文档信息检查文档是否存在
     * 
     * @param anItem
     *            文档信息
     * @return
     */
    public boolean exists(CustFileItem anItem) {
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
    public boolean exists(String anFilePath, String anStoreType) {
        FileStoreType storeType = FileStoreType.checking(anStoreType);
        FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);
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
    public long findSize(String anFilePath, String anStoreType) {
        FileStoreType storeType = FileStoreType.checking(anStoreType);
        FileManager fileManager = FileManagerFactory.create(storeType, fileGroupService);

        return fileManager.findSize(anFilePath);
    }


}