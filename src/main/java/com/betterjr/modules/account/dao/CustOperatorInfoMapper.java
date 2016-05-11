package com.betterjr.modules.account.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@BetterjrMapper
public interface CustOperatorInfoMapper extends Mapper<CustOperatorInfo> {

    @Select("select a.* from T_CUST_OPERATOR a where a.C_STATUS ='1' and a.C_IDENTTYPE = #{identType} and a.C_IDENTNO = #{identNo}")
    @ResultType(CustOperatorInfo.class)
    public CustOperatorInfo findCustOperatorByIndentInfo(@Param("identType") String anIndentType, @Param("identNo") String anIndetNo);

    @Select("select a.* from T_CUST_OPERATOR a where  a.C_STATUS ='1' and a.C_OPERORG = #{operOrg} and a.C_CODE = #{operCode}")
    @ResultType(CustOperatorInfo.class)
    public CustOperatorInfo findCustOperatorByOperCode(@Param("operOrg") String anOperOrg, @Param("operCode") String anOperCode);
}