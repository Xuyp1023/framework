package com.betterjr.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.sys.entity.TempFundGroupInfo;

@BetterjrMapper
public interface TempFundGroupInfoMapper extends Mapper<TempFundGroupInfo> {

    // 最近三个月平均七日年化收益率排名,前20
    @Select(" SELECT A.FUNDCODE,A.YIELD,ROWNUM AS RANKING FROM (SELECT FUNDCODE, avg(YIELD) YIELD FROM (SELECT *  FROM TEMP_FUNDINFO T "
            + " WHERE TO_DATE(T.FUNDDAY, 'yyyy-mm-dd') <= TO_DATE(#{iFundDay}, 'yyyy-mm-dd') "
            + " AND TO_DATE(T.FUNDDAY, 'yyyy-mm-dd') >= add_months(TO_DATE(#{iFundDay}, 'yyyy-mm-dd'), -3)" + "  )"
            + " GROUP BY FUNDCODE  ORDER BY YIELD desc, FUNDCODE) A WHERE ROWNUM<=20")

    public List<Map> findYieldAvgByFundDay(@Param("iFundDay") String fundDay);

    // 最近七日年化收益率排名
    @Select("SELECT A.FUNDCODE, A.YIELD, ROWNUM AS RANKING FROM (SELECT T.FUNDCODE,"
            + " T.YIELD,  rank() over(partition by t.fundcode order by T.YIELD desc) RABKING"
            + " FROM TEMP_FUNDINFO T   WHERE TO_DATE(T.FUNDDAY, 'yyyy-mm-dd') <= TO_DATE(#{iFundDay}, 'yyyy-mm-dd') "
            + " AND TO_DATE(T.FUNDDAY, 'yyyy-mm-dd') >= (TO_DATE(#{iFundDay}, 'yyyy-mm-dd') - 7)"
            + "  ORDER BY YIELD desc) A" + " WHERE A.RABKING = 1 ")
    @ResultType(TempFundGroupInfo.class)
    public List<Map> findYieldByFundDay(@Param("iFundDay") String fundDay);

    @Select("SELECT T.fundcode,T.fundday,T.income FROM TEMP_FUNDINFO T "
            + "WHERE to_date(T.fundday, 'yyyy-mm-dd') <= to_date(#{iFundDay}, 'yyyy-mm-dd')"
            + " and to_date(T.fundday, 'yyyy-mm-dd') >= (to_date(#{iFundDay}, 'yyyy-mm-dd') -7)" + "  ")
    @ResultType(TempFundGroupInfo.class)
    public List<TempFundGroupInfo> findIncomeByFundDay(@Param("iFundDay") String fundDay);

    @Select("select fundday from TEMP_FUNDINFO WHERE TO_DATE(fundday,'yyyy-mm-dd')>=TO_DATE(#{beginDate}, 'yyyy-mm-dd') "
            + " AND TO_DATE(fundday,'yyyy-mm-dd')<=TO_DATE(#{endDate}, 'yyyy-mm-dd') group by fundday order by fundday desc")
    public List<Map> getFundDay(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

}