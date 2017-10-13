package com.betterjr.modules.document;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.betterjr.modules.document.data.AccountAduitData;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.entity.CustFileAduit;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.entity.CustResolveFile;

public interface ICustFileService {

    /**
     * 更新用户认证文件信息，包括增删改（由上传的文件数量决定）
     * 
     * @param request
     */
    public String webUpdateCustFileAuditInfo(Map<String, String[]> anParamMap, Enumeration<String> anParamNames, Long anCustNo);

    /**
     * 查询用户认证文件信息
     * 
     * @return
     */
    public String webFindCustFileAuditInfo(Long anCustNo);

    /**
     * 根据客户号和文件业务类型查找文件的批次号
     * 
     * @param anCustNo
     *            客户号
     * @param anFileBusinType
     *            文件业务类型
     * @return
     */
    public List<Long> findBatchNo(Long anCustNo, List<String> anFileBusinType);

    /**
     * 更新文件审批情况信息
     * 
     * @param anAduitData
     *            审批方数据
     * @return
     */
    public boolean updateAuditFileGroup(AccountAduitData anAduitData);

    /**
     * 查找客户认证材料缺少的文件业务类型
     * 
     * @param anCustNo
     *            客户编号
     * @param anAgencyNo
     *            合作伙伴代码
     * @param anBusinFlag
     *            业务类型
     * @return
     */
    public String webFindDeficiencyFileInfoList(Long anCustNo, String anAgencyNos);

    public List<CustFileItem> findUploadFileByAgency(String anRequestNo, String anBusinFlag, String anAgecyNo);

    public List<CustFileItem> findUploadFiles(String anRequestNo, String anBusinFlag);

    public List<CustFileItem> findUploadFileByCustNo(Long custNo, String anBusinFlag);

    /**
     * 根据文件ID号，查询单个文件信息
     * 
     * @param id
     * @return
     */
    public CustFileItem findOne(Long id);

    /**
     * 根据文件编号，更新文件批次号
     * 
     * @param batchNo
     *            文件批次号
     * @param fileItemId
     *            文件编号
     * @return
     */
    public boolean updateFileItems(Long batchNo, Long fileItemId);

    /**
     * 更新文件列表的批次号，如果批次号不存在，则创建批次号
     * 
     * @param anFileList
     *            以逗号分隔的文件编号
     * @param anBatchNo
     *            文件批次号
     * @return 返回文件批次号
     */
    public Long updateCustFileItemInfo(String anFileList, Long anBatchNo);

    /**
     * 根据batchNo获得一个文件的信息
     * 
     * @param anBatchNo
     * @return
     */
    public CustFileItem findOneByBatchNo(Long anBatchNo);

    public Map<String, CustFileItem> findItems(Map<String, Long> anMap);

    /**
     * 根据批次号，查询本批次上传的文件。
     * 
     * @param anBatchNo
     *            批次号
     * @return
     */
    public List<CustFileItem> findCustFiles(Long anBatchNo);

    /**
     * 根据批次号和文件业务类型，获得相关的文件信息
     * 
     * @param anBatchNoList
     *            批次号列表
     * @param anbusinTypeList
     *            文件类型列表
     * @return
     */
    public List<CustFileItem> findCustFilesByBatch(List<Long> anBatchNoList, List<String> anbusinTypeList);

    /**
     * 根据批次号获得文件列表
     * 
     * @param anBatchNoList
     *            批次号列表
     * @return
     */
    public List<CustFileItem> findCustFilesByBatch(List<Long> anBatchNoList);

    public CustFileItem saveAndUpdateFileItem(String filePath, Long fileLength, String anWorkType, String anFileName, FileStoreType anStoreType,
            boolean anWithBatchNo);

    /**
     * 保存文件信息，如果存在就更新，不存在就增加
     * 
     * @param anFileItem
     * @return
     */
    public String webSaveAndUpdateFileItem(String filePath, Long fileLength, String anWorkType, String anFileName, FileStoreType anStoreType);

    /**
     * 删除附件，具体逻辑是如果存在并匹配上了，就可以设置batchNo为负值，便于今后查询
     * 
     * @param anId
     *            文件ID号
     * @param anBatchNo
     *            文件批次号
     */
    public String webDeleteFileItem(Long anId, Long anBatchNo);

    /**
     * 删除附件，具体逻辑是如果存在并匹配上了，就可以设置batchNo为负值，便于今后查询
     * 
     * @param anId
     *            文件ID号
     * @param anBatchNo
     *            文件批次号
     */
    public boolean deleteFileItem(Long anId, Long anBatchNo);

    /**
     * 根据fileList和现有batchNo更新,会删除已删除的文件
     */
    public Long updateAndDelCustFileItemInfo(String anFileList, Long anBatchNo);

    /***
     * 添加客户审核附件
     * 
     * @param anCustFileAduit
     * @return
     */
    public boolean addCustFileAduit(CustFileAduit anCustFileAduit);

    /***
     * 删除审核表中的附件关联
     * 
     * @param anId
     * @return
     */
    public boolean delCustFileAduit(Long anId);

    /**
     * 根据id 查询文件
     * 
     * @param anIds
     *            id以逗号隔开
     */
    public List<CustFileItem> findFileListByIds(String[] anIds);

    /****
     * 审核通过查询的附件来源为审核正式表
     * 
     * @param anCustNo
     * @param anRelateCustNo
     * @return
     */
    public List<CustFileItem> findCustFileAduit(Long anCustNo, Long anRelateCustNo);

    public String webfindResolveFile(Long anId);

    public boolean sendResolveMessage(CustResolveFile anResolveFile);

    public CustResolveFile webSaveAddResolveFile(CustResolveFile anResolveFile);

    public CustFileItem findOneAndButchId(Long anId);

    public void saveModifyResolveFile(Map<String, Object> anResolveFileMap);

    public void saveModifyResolveFile(CustResolveFile anResolveFile);

    public void savePlatformAduitFile(final Long anCustNo, final Long anBatchNo);
}
