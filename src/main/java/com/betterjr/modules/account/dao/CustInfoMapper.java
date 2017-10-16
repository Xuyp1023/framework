package com.betterjr.modules.account.dao;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.account.entity.CustInfo;

@BetterjrMapper
public interface CustInfoMapper extends Mapper<CustInfo> {
    /*
    @Select("select a.* from T_CUSTINFO a left join T_CUST_OPERATOR_RELATION b on a.l_custno = b.l_custno left join T_CUST_OPERATOR c on c.id=b.L_OPERNO where a.c_status !='9' and c.id =#{anOperatorID}")
    @ResultType(CustInfo.class)
    public List<CustInfo> findCustInfoByOperator(Long  anOperatorID);
    
    @Select("select a.* from T_CUSTINFO a where and a.c_status !='9' and a.c_identtype = #{identType} and a.c_identno = #{identNo}")
    @ResultType(CustInfo.class)
    public List<CustInfo> findCustInfoByIndentNo(@Param("identType") String anIndentType, @Param("identNo") String anIndetNo);
    */}