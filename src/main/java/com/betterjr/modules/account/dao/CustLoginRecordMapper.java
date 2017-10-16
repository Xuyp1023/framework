package com.betterjr.modules.account.dao;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.account.entity.CustLoginRecord;

@BetterjrMapper
public interface CustLoginRecordMapper extends Mapper<CustLoginRecord> {

    // @SelectKey(before = true, keyProperty = "id", resultType = Integer.class, statement = { "set id=@id,
    // loginTime=@time, loginDate=@date, ipaddr=@clientip"})
    @Override
    public int insert(CustLoginRecord record);

    // @SelectKey(before = true, keyProperty = "id", resultType = Integer.class, statement = { "set id=@id,
    // loginTime=@time, loginDate=@date, ipaddr=@clientip"})
    @Override
    public int insertSelective(CustLoginRecord record);

}