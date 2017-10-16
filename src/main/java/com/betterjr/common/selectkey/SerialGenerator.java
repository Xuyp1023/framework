package com.betterjr.common.selectkey;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Encodes;

public class SerialGenerator implements SessionIdGenerator {
    private final Logger logger = LoggerFactory.getLogger(SerialGenerator.class);

    public static final String BANK_SERIAL = "bank.serial";
    private static final String TRADE_ACCO = "SaleAccoRequestInfo.tradeAccount";
    private static final String REQUEST_NO = ".requestNo";

    private static final String CUST_ACCO = "SaleAccoRequestInfo.custNo";

    private static final String MONEY_ACCO = "SaleAccoBankInfo.id";

    public static final String OPERATOR_ID = "CustOperatorInfo.id";

    public static final String FLOW_DEF_ID = "CustFlowDefine.id";

    public static final String FLOW_ACT_ID = "CustFlowActivity.id";

    public static final String FLOW_ECT_ID = "CustFlowExecutor.id";

    public static final String FLOW_ORD_ID = "CustFlowOrder.id";

    public static final String FLOW_TASKECT_ID = "CustFlowTaskExecutor.id";

    public static final String FLOW_TASK_ID = "CustFlowTask.id";
    @Autowired
    private SelectKeyGenIDService selectKeyGenIDService = null;

    private static SerialGenerator generator = null;

    @PostConstruct
    public void init() {
        generator = this;
    }

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static int randomInt(int anMax) {
        return Math.abs(random.nextInt(anMax));
    }

    /***
     * 产生8点到15间的随机数
     * 
     * @return
     */
    public static String randomTime() {
        StringBuilder sb = new StringBuilder(6);
        int arrInt[] = new int[] { 7, 60, 60 };
        for (int i = 0; i < 3; i++) {
            int kk = randomInt(arrInt[i]);
            if (i == 0) {
                kk = kk + 8;
            }
            if (kk < 10) {
                sb.append("0");
            }
            sb.append(Integer.toString(kk));
        }

        return sb.toString();
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }

    @Override
    public Serializable generateId(Session session) {

        return uuid();
    }

    public static String getRequestNo(String anClass, String anNetNo) {
        String tmpStr = generator.getValue(anClass.concat(REQUEST_NO), 16).concat(anNetNo);
        System.out.println(tmpStr);
        return tmpStr;
    }

    public static long getLongValue(String anKey) {

        return generator.selectKeyGenIDService.getLongValue(anKey);
    }

    public static int getIntValue(String anKey) {

        return (int) generator.selectKeyGenIDService.getLongValue(anKey);
    }

    public static long getCustNo() {

        return generator.selectKeyGenIDService.getLongValue(CUST_ACCO);
    }

    public static long getMoneyAccountID() {

        return generator.selectKeyGenIDService.getLongValue(MONEY_ACCO);
    }

    public static String getTradeAcco(String anNetNo) {

        return "A" + anNetNo + generator.getValue(TRADE_ACCO, 12, false);
    }

    public static String getAppSerialNo(int anLen) {
        return generator.getAppValue(anLen);
    }

    private String getAppValue(int anLen) {

        String key = BANK_SERIAL.concat(Integer.toString(anLen));
        return getValue(key, anLen);
    }

    private String getValue(String anKey, int anLen) {

        return getValue(anKey, anLen, true);
    }

    public static String getConfirmSerialNo(String anAgencyNo) {
        String tmpStr = generator.getValue("SaleConfirmInfo.id", 17, true);
        if (anAgencyNo != null) {
            tmpStr = anAgencyNo.concat(tmpStr);
        }
        return tmpStr;
    }

    private String getValue(String anKey, int anLen, boolean anUseDate) {

        String tmpStr = Long.toString(selectKeyGenIDService.getLongValue(anKey));
        if (anUseDate) {
            anLen = anLen - 8;
        }
        if (tmpStr.length() > anLen) {
            tmpStr = tmpStr.substring(tmpStr.length() - anLen);
        } else {
            for (int i = tmpStr.length(); i < anLen; i++) {
                tmpStr = "0".concat(tmpStr);
            }
        }
        if (anUseDate) {
            return BetterDateUtils.getNumDate().concat(tmpStr);
        } else {
            return tmpStr;
        }
    }

    /**
     * 根据客户号和业务类型，获取循环序列号，循环的信息定义在序列数据表中<BR>
     * 循环序号号的KEY值是：客户号+“_” + 业务类型
     * @param anCustNo 客户号
     * @param anWorkType 业务类型
     * @return
     */
    public static String findAppNoWithDayAndType(Long anCustNo, String anWorkType) {
        if (anCustNo == null) {

            return "";
        }

        if (anWorkType == null) {

            anWorkType = "";
        }

        return anWorkType.concat(findAppNoWithDay(Long.toString(anCustNo).concat("_").concat(anWorkType)));
    }

    /**
     * 根据业务类型，获取循环序列号，循环的信息定义在序列数据表中
     * @param anWorkType
     * @return
     */
    public static String findAppNoWithDayAndType(String anWorkType) {
        if (StringUtils.isBlank(anWorkType)) {

            return "";
        }

        return anWorkType.concat(findAppNoWithDay(anWorkType));
    }

    /**
     * 根据业务类型获取循环序列号
     * @param anWorkType 业务类型
     * @return
     */
    public static String findAppNoWithDay(String anWorkType) {

        return generator.findAppNo(anWorkType);
    }

    private String findAppNo(String anWorkType) {

        return this.selectKeyGenIDService.findAppNoWithDay(anWorkType);
    }
}
