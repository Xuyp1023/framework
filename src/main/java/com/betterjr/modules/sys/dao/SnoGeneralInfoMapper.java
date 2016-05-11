package com.betterjr.modules.sys.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.sys.entity.SnoGeneralInfo;

@BetterjrMapper
public interface SnoGeneralInfoMapper extends Mapper<SnoGeneralInfo> {

	@Update("update T_CFG_SNOGENERAL a set a.L_LASTNO = #{snoInfo.lastNo} where a.C_OPERTYPE = #{snoInfo.operType} and a.L_LASTNO < #{snoInfo.lastNo} ")
	int updateSimple(@Param("snoInfo") SnoGeneralInfo snoInfo);

}