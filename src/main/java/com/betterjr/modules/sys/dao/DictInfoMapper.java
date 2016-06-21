package com.betterjr.modules.sys.dao;

import java.util.*;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.sys.entity.DictInfo;
 
@BetterjrMapper
public interface DictInfoMapper extends Mapper<DictInfo>    {
	@Select("select * from T_CFG_DICT where C_GROUP = #{groupId}")
	@ResultType(DictInfo.class)
	List<DictInfo> findByGroup(String groupId);

	@SelectKey(before = true, keyProperty = "id", resultType = Integer.class, statement = { "set id=@id, modiDate=@date" })
	// @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
	int insert(DictInfo dictInfo);

	@SelectKey(before = true, keyProperty = "id", resultType = Integer.class, statement = { "set id=@id, modiDate=@date" })
	// @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
	int insertSelective(DictInfo dictInfo);
}