// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年4月11日, liuwl, creation
// ============================================================================
package com.betterjr.modules.generator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.utils.BetterDateUtils;

/**
 * @author liuwl
 *
 */
public class PatternSequenceGenerator extends SequenceGenerator {
    private static final String SEQ6_STRING = "#{Seq:";
    private static final String DATE_PREFIX = "#{Date:";
    private static final String BRACKET_SUFFIX = "}";

    protected Map<String, Object> params;

    @Override
    public Object getValue(final String anSeqId, final String anOperOrg, final Long anCustNo, final String anPattern,
            final String anCycle) throws BytterException {
        String result = anPattern;
        // translate the sequence no. with left padding 0
        if (StringUtils.contains(anPattern, SEQ6_STRING)) {
            String seqNum = String.valueOf(super.getValue(anSeqId, anOperOrg, anCustNo, anPattern, anCycle));
            final int seqLen = NumberUtils.toInt(StringUtils.substringBetween(anPattern, SEQ6_STRING, BRACKET_SUFFIX),
                    0);
            if (seqLen > 0) {
                seqNum = String.format(("%1$0" + seqLen + "d"), Integer.parseInt(seqNum));
                result = StringUtils.replace(result, (SEQ6_STRING + seqLen + BRACKET_SUFFIX), seqNum);
            }
        }
        // translate the system date field
        if (StringUtils.contains(anPattern, DATE_PREFIX)) {
            final String dateFormat = StringUtils.substringBetween(anPattern, DATE_PREFIX, BRACKET_SUFFIX);
            final String dateTime = BetterDateUtils.formatDate(BetterDateUtils.getNow(), dateFormat);
            result = StringUtils.replace(result, (DATE_PREFIX + dateFormat + BRACKET_SUFFIX), dateTime);
        }

        return result;
    }

    @Override
    public void setParameters(final Map<String, Object> params) {
        this.params = params;
    }
}
