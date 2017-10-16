package com.betterjr.modules.document;

import java.util.List;
import java.util.Map;

import com.betterjr.common.data.CheckDataResult;
import com.betterjr.modules.document.data.FileStoreType;
import com.betterjr.modules.document.data.OSSConfigInfo;
import com.betterjr.modules.document.entity.AgencyAuthorFileGroup;
import com.betterjr.modules.document.entity.AuthorFileGroup;

public interface IAgencyAuthFileGroupService {

    /**
     * 根据合作伙伴编码和业务类型，获得授权使用的业务文件类型
     * @param anAgencyNo 合作伙伴编码
     * @param anBusinFlag 业务文件类型
     * @return
     */
    public List<AgencyAuthorFileGroup> findAuthorFileGroup(String anAgencyNo, String anBusinFlag);

    public List<AgencyAuthorFileGroup> findAuthorFileGroup(String[] anAgencyNoList, String anBusinFlag);

    /**
     * 根据合作伙伴编码和业务类型，获得授权使用的业务文件类型，组合为直接使用的数据
     * @param anAgencyNo 合作伙伴编码
     * @param anBusinFlag 业务文件类型
     * @return
     */
    public List<String> composeList(String anAgencyNo, String anBusinFlag);

    public Map<String, AuthorFileGroup> findAllFileGroup();

    public List<AuthorFileGroup> findFileGroupList(String anBusinFlag);

    public List<AuthorFileGroup> findCustFileGroupList();

    /**
     * 查找文件存储类型
     * 
     * @param anFileInfoType
     *            文件业务类型
     * @return
     */
    public FileStoreType findFileStoreType(String anFileInfoType);

    /**
     * 查找Aliyun OSS 配置信息
     * @return
     */
    public OSSConfigInfo findOSSConfigInfo();

    /**
     *查找文件创建路径 
     * @param anAgencyNo 合作机构代码
     * @param anFileInfoType 文件类别
     * @return
     */
    public String findCreateFilePath(String anFileInfoType);

    /**
     * 查找文件绝对路径信息
     * @param anFilePath 文件路径信息
     * @return
     */
    public String findAbsFilePath(String anFilePath);

    /**
     * 查找模板文件路径
     * 
     * @param anAgencyNo
     *            合作机构
     * @param anFileInfoType
     *            文件类型
     * @return
     */
    public String findTempFilePath(String anAgencyNo, String anFileInfoType);

    /**
     * 查找demo样张的文件路径
     * 
     * @param anFileInfoType
     * @return
     */
    public String findDemoFilePath(String anAgencyNo, String anFileInfoType);

    /***
     * 根据不同条件查询返回文件类型对象
     * @param anMap 条件
     * @return
     */
    public AgencyAuthorFileGroup findAuthorFileGroupByMap(Map<String, Object> anMap);

    /**
     * 检查存储的文件类型是否允许
     * 
     * @param anFileInfoType
     *            文件业务类型
     * @param anFileType
     *            文件类型
     * @return
     */
    public CheckDataResult findFileTypePermit(String anFileInfoType, String anFileType);

    /**
     * 
     * @param anFileInfoType
     * @return
     */
    public String webFindFileTypePermitInfo(String anFileInfoType);

    /***
     * 查询文件类型对象
     * @param anFileInfoType
     * @return
     */
    public AuthorFileGroup findAuthFileGroup(String anFileInfoType);

}
