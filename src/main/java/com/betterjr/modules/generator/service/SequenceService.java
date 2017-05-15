// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年4月11日, liuwl, creation
// ============================================================================
package com.betterjr.modules.generator.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.mapper.pagehelper.PageHelper;
import com.betterjr.modules.generator.dao.SequenceRecordMapper;
import com.betterjr.modules.generator.entity.SequenceRecord;

/**
 * @author liuwl
 *
 */
@Service
public class SequenceService extends BaseService<SequenceRecordMapper, SequenceRecord> {

    private static final Logger logger = LoggerFactory.getLogger(SequenceService.class);

    private static final String CYCLE_DAY = "D";
    private static final String CYCLE_WEEK = "W";
    private static final String CYCLE_MONTH = "M";
    private static final String CYCLE_YEAR = "Y";
    private static final long DEFAULT_START_FROM = 1L;
    private static final long DEFAULT_INCREMENT = 1L;
    private static final long DEFAULT_MAX = 99999999999999L;

    public long saveGetSequence(final String anSeqId, final String anOperOrg, final Long anCustNo, final String anCycle) {
        long currentSeqNo = 0;
        final String operOrg = StringUtils.isBlank(anOperOrg) ? "DEFAULT" : anOperOrg;
        final long custNo = anCustNo == null ? 0L : anCustNo.longValue();
        try {
            PageHelper.restPage();// 清除分页信息
            logger.debug("Start SeqId:" + anSeqId + "  operOrg:" + operOrg + " custNo:" + custNo);
            SequenceRecord sequenceRecord = this.mapper.getAndLockSeqDef(anSeqId, operOrg, custNo);

            if (sequenceRecord == null) {
                try {
                    sequenceRecord = saveRetrieveDefaultSeqDef(anSeqId, operOrg, custNo, anCycle);
                }
                catch (final Exception e) {
                    logger.error("Exception SeqId:" + anSeqId + "  operOrg:" + operOrg + " custNo:" + custNo, e);
                    sequenceRecord = this.mapper.getAndLockSeqDef(anSeqId, anOperOrg, anCustNo);
                }
                if (sequenceRecord == null) {
                    throw new BytterException("Failed to find the sequence definition");
                }
            }
            logger.debug("End SeqId:" + anSeqId + "  operOrg:" + operOrg + " custNo:" + custNo + " " + sequenceRecord);
            currentSeqNo = sequenceRecord.getNextValue();

            final boolean isReachTheNextCycle = isReachTheNextCycle(sequenceRecord.getCycle(), sequenceRecord.getCycleStartDate());

            // if currentSeqNo > max value or reach the next cycle, reset the seqDef's data
            if (currentSeqNo > sequenceRecord.getMaximumValue() || isReachTheNextCycle) {
                sequenceRecord.setCycleStartDate(BetterDateUtils.getNumDate());
                sequenceRecord.setNextValue(sequenceRecord.getStartValue());
                currentSeqNo = sequenceRecord.getNextValue();
            }

            sequenceRecord.setNextValue(currentSeqNo + sequenceRecord.getIncrementStep());

            final int result = this.updateByPrimaryKey(sequenceRecord);
            BTAssert.isTrue(result == 1, "update sequenceRecord error.");
        }
        catch (final Exception e) {
            throw new BytterException("error occured when getting sequence", e);
        }
        finally {
        }
        return currentSeqNo;
    }

    private synchronized SequenceRecord saveRetrieveDefaultSeqDef(final String anSeqId, final String anOperOrg, final long anCustNo, final String anCycle) {
        try {
            final SequenceRecord sequenceRecord = new SequenceRecord();

            sequenceRecord.setMaximumValue(DEFAULT_MAX);
            sequenceRecord.setStartValue(DEFAULT_START_FROM);
            sequenceRecord.setIncrementStep(DEFAULT_INCREMENT);
            sequenceRecord.setNextValue(DEFAULT_START_FROM);
            sequenceRecord.setSeqId(anSeqId);

            sequenceRecord.setOperOrg(anOperOrg);
            sequenceRecord.setCustNo(anCustNo);

            sequenceRecord.setCycle(anCycle == null ? CYCLE_YEAR : anCycle);
            sequenceRecord.setCycleStartDate(BetterDateUtils.getNumDate());

            final int result = this.mapper.insert(sequenceRecord);

            BTAssert.isTrue(result == 1, "create sequenceRecord error.");

            return this.mapper.getAndLockSeqDef(anSeqId, anOperOrg, anCustNo);
        }
        catch (final Exception e) {
            throw new BytterException("error occured creating default sequence def", e);
        }
    }

    private static boolean isReachTheNextCycle(final String anCycle, final String anCycleStartDate) {
        if (StringUtils.isBlank(anCycleStartDate)) {
            return true;
        }

        final LocalDate date1 = LocalDate.parse(anCycleStartDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        final LocalDate date2 = LocalDate.now();

        if (StringUtils.isEmpty(anCycle)) {
            return false;
        }
        else if (StringUtils.equalsIgnoreCase(anCycle, CYCLE_DAY)) {
            return date1.isBefore(date2);
        }
        else if (StringUtils.equalsIgnoreCase(anCycle, CYCLE_WEEK)) {

            if (date1.getYear() == date2.getYear()) {
                if (date1.getMonthValue() == date2.getMonthValue()) {
                    final Instant instant1 = date1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    final Date d1 = Date.from(instant1);
                    final Instant instant2 = date2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    final Date d2 = Date.from(instant2);

                    if (isSameDate(d1, d2)) {
                        return false;
                    }
                }
            }
            return true;
        }
        else if (StringUtils.equalsIgnoreCase(anCycle, CYCLE_MONTH)) {
            if (date1.getYear() == date2.getYear()) {
                if (date1.getMonthValue() == date2.getMonthValue()) {
                    return false;
                }
            }
            return true;
        }
        else if (StringUtils.equalsIgnoreCase(anCycle, CYCLE_YEAR)) {
            if (date1.getYear() == date2.getYear()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isSameDate(final Date date1, final Date date2) {
        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal1.setFirstDayOfWeek(Calendar.MONDAY);// 西方周日为一周的第一天，咱得将周一设为一周第一天
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        cal1.setTime(date1);
        cal2.setTime(date2);
        final int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0)// subYear==0,说明是同一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) // subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)// subYear==-1,说明cal比cal2小一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    public static void main(final String[] args) {
        final boolean result = isReachTheNextCycle("M", "20160401");
        System.out.println(result);
    }
}
