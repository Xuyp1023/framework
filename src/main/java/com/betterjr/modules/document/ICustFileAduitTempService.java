package com.betterjr.modules.document;

import java.util.List;

import com.betterjr.modules.document.entity.AgencyAuthorFileGroup;
import com.betterjr.modules.document.entity.CustFileAduitTemp;
import com.betterjr.modules.document.entity.CustFileItem;

public interface ICustFileAduitTempService {

    /****
     * 获取临时文件
     * @param anCustNo
     * @param anAgencyAuthorFileGroupList
     * @return
     */
    public List<CustFileItem> findCustAduitTemp(Long anCustNo, Long anSelectCustNo,
            List<AgencyAuthorFileGroup> anAgencyAuthorFileGroupList);

    /***
     * 保存临时文件
     * @param anCustFileAduitTemp
     */
    public boolean addCustFileAduitTemp(CustFileAduitTemp anCustFileAduitTemp);

    /***
     * 检查文件是否都已上传
     * @param anCustNo 关联上传文件的客户号
     * @param anAgencyAuthorFileGroupList 文件类型列表
     * @return
     */
    public boolean checkCustFileAduitTempExist(Long anCustNo, Long anSelectCustNo,
            List<AgencyAuthorFileGroup> anAgencyAuthorFileGroupList);

    /***
     * 删除临时审核附件关系表数据
     * @param anId 附件 id
     * @return
     */
    public boolean saveDeleteFileAduitTemp(Long anId);

    /***
     * 添加客户文件关系
     * @param anRelationCustNo 关联的客户号
     * @param fileIds 上传的文件列表(以,分隔)
     */
    public void saveCustFileAduitTemp(Long anCustNo, Long anRelateCustNo, String anFileIds, String anCustType);

    /***
     * 查询关系审核附件
     * @param anCustNo
     * @return
     */
    public List<CustFileItem> findRelateAduitTempFile(Long anCustNo);

    /***
     * 保存附件关系
     * @param anPassFiles 审核通过的文件列表
     * @param anFailFiles 审核不通过的文件列表
     */
    public void saveAcceptFileTemp(String anPassFiles, String anFailFiles);

    public void saveAduitFile(Long anCustNo, Long anRelateCustNo);

}
