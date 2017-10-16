package com.betterjr.modules.generator.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.generator.entity.SequenceRecord;

@BetterjrMapper
public interface SequenceRecordMapper extends Mapper<SequenceRecord> {
    @Select("SELECT * FROM T_SYS_SEQ_RECORD WHERE C_SEQ_ID = #{seqId} AND C_OPERORG = #{operOrg} AND L_CUSTNO = #{custNo}  FOR UPDATE")
    @ResultType(SequenceRecord.class)
    public SequenceRecord getAndLockSeqDef(@Param("seqId") String seqId, @Param("operOrg") String operOrg,
            @Param("custNo") long custNo);
}