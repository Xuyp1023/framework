package com.betterjr.common.config;

/**
 * 统一定义参数中使用的参数名称；参数全部采取全局静态变量方式,参数变量名大写
 * 
 * @author zhoucy
 *
 */
public abstract class ParamNames {

    public static final String SYS_DATE = "SysDateInfo";

    public static final String OPEN_DAY = "openday";

    public static final String AGENCY = "SaleAgency";

    public static final String PAY_CENTER = "payCenter";

    public static final String PAY_CHANNLE = "paychannel";

    public static final String FUND_LIMIT = "fundLimit";

    public static final String FUND_INFO = "fundInfo";

    public static final String FUND_CALENDAR = "fundCalendar";
    
    public static final String FUND_AGENCY = "fundAgency";

    public static final String FUND_DAY = "fundDay";

    public static final String COMBINBEST = "combinVest";
    
    public static final String COMBIN = "combin";
    

    public static final String FUND_CONTROL = "fundContrl";

    public static final String BACK_INFO = "banckInfo";

    public static final String DISCOUNT = "discount";

    public static final String FUND_CHANGE = "change";

    public static final String METHOD_WEB_ORG = "webOrgMethod";

    public static final String REQUEST = "request";

    public static final String DATA = "data";
    
    public static final String ACCO_REQ = "accoReq";

    // 通配处理主要是为了方便配置中模糊配置，减少配置量
    // 通配基金代码
    public static final String IGNORE_FUNDCODE = "000000";

    // 通配份额类型
    public static final String IGNORE_SHARETYPE = "X";
    // 通配客户类型
    public static final String IGNORE_CUSTTYPE = "X";
    // 通配销售机构编码
    public static final String IGNORE_AGENCY = "000";
    // 通配接受方式
    public static final String IGNORE_ACCEPT = "X";
    // 通配业务代码
    public static final String IGNORE_BUSIN = "000";
    // 通配基金TA编码
    public static final String IGNORE_TANO = "00";
    // 默认取的工作日历天数，当前日期正负30天；共60天
    public static final int QUERY_OPENDAY_COUNT = 30;

    public static final int QUERY_REQUEST_DATE = 10;
    
    public static final int MAX_PAGE_SIZE = 100;
    public static final String PENGUIN_CODE ="PenguinCode";
    public static final String AGENCY_FILE_DOWNLOAD_PATH ="AgencyFileDownloadPath";
    public static final String OPENACCO_FILE_DOWNLOAD_PATH ="OpenAccoFileDownloadPath";
    public static final String BUSIN_VALIDDAY="BUSIN_VALIDDAY";
    
    /**
     * 系统销售机构代码 
     * 209: 大成基金 
     * 205：博时基金 
     * 201：南方基金 
     * 269：民生加银
     * 217: 招商基金
     */
    public static final String SALE_AGENTCY_DC = "209";
    public static final String SALE_AGENTCY_BS = "205";
    public static final String SALE_AGENTCY_NF = "201";
    public static final String SALE_AGENTCY_MS = "269";
    public static final String SALE_AGENTCY_ZS = "217";
    
  
    /**
     * 交易状态 00：待复核，，02：待报，03：驳回修改，04：废单，
     * 05：已撤，06：已报，07：确认成功，08：已结束
     */
    public static final String TRADE_STATUS_00="00";//待复核
    public static final String TRADE_STATUS_01="01";//待勾兑
    public static final String TRADE_STATUS_02="02";//待报
    public static final String TRADE_STATUS_03="03";//驳回修改
    public static final String TRADE_STATUS_04="04";//废单
    public static final String TRADE_STATUS_05="05";//已撤
    public static final String TRADE_STATUS_06="06";//已报
    public static final String TRADE_STATUS_07="07";//确认成功
    public static final String TRADE_STATUS_08="08";//已结束（确认失败）
    public static final String TRADE_STATUS_0A="0A";//申请失败
    public static final String TRADE_STATUS_10="10";//上报失败
    public static final String TRADE_STATUS_0B="OB";//撤单
    public static final String TRADE_STATUS_OC="OC";//撤单失败
    
    /**
     * 业务类型:开户
     */
    public static final String  BUSIFLAG_OPENACCO="01";
    /**
     * 业务类型：增开
     */
    public static final String BUSIFLAG_ADDACCO="08";
    /**
     * 业务类型：认购
     */
    public static final String BUSIFLAG_SUBSCRIPTION = "20";
    /**
     * 业务类型：申购
     */
    public static final String BUSIFLAG_PURCHASE = "22";
    /**
     * 业务类型：赎回
     */
    public static final String BUSIFLAG_REDEEM="24";
    /**
     * 业务类型：申购撤单
     */
    public static final String BUSIFLAG_REVOKE_PURCHASE = "R22";
    /**
     * 业务类型：赎回撤单
     */
    public static final String BUSIFLAG_REVOKE_REDEEM = "R24";
    /*
     * 支付状态：00-未支付
     */
    public static final String PAYSTATUS_00="00";
    
    public static final String DEFAULT_BANK_NET_NO = "0101";
    public static final String CONTRACT_PATH ="contract";
    public static final String FACTOR_CORE_CUSTINFO ="FactorCoreCustInfo";
}