package com.betterjr.modules.account.entity;

import java.math.BigDecimal;
import java.util.Map;

import com.betterjr.common.annotation.*;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.AttachDataFace;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.data.SaleRequestFace;

import javax.persistence.*;

import org.springframework.util.LinkedCaseInsensitiveMap;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SALE_ACCOREQUEST")
public class SaleAccoRequestInfo implements BetterjrEntity, SaleRequestFace, BaseRemoteEntity, AttachDataFace {
    /**
     * 申请单号
     */
    @Id
    @Column(name = "C_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "申请单号", comments = "申请单号")
    @OrderBy("DESC")
    private String requestNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * TA代码
     */
    @Column(name = "C_TANO", columnDefinition = "VARCHAR")
    @MetaData(value = "TA代码", comments = "TA代码")
    private String tano;

    /**
     * 交易账户
     */
    @Column(name = "C_TRADEACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "交易账户", comments = "交易账户")
    private String tradeAccount;

    /**
     * 基金账户
     */
    @Column(name = "C_FUNDACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "基金账户", comments = "基金账户")
    private String fundAccount;

    /**
     * 销售人代码
     */
    @Column(name = "C_AGENCYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "销售人代码", comments = "销售人代码")
    private String agencyNo;

    /**
     * 分中心
     */
    @Column(name = "C_PAYCENTER", columnDefinition = "VARCHAR")
    @MetaData(value = "分中心", comments = "分中心")
    private String payCenterNo;

    /**
     * 网点编码
     */
    @Column(name = "C_NETNO", columnDefinition = "VARCHAR")
    @MetaData(value = "网点编码", comments = "网点编码")
    private String netNo;

    /**
     * 业务代码
     */
    @Column(name = "C_BUSINFLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "业务代码", comments = "业务代码")
    private String businFlag;

    /**
     * 客户类型；0机构，1个人
     */
    @Column(name = "C_CUSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户类型", comments = "客户类型；0机构，1个人")
    private String custType;

    /**
     * 客户姓名
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户姓名", comments = "客户姓名")
    private String custName;

    /**
     * 客户昵称
     */
    @Column(name = "C_NICKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户昵称", comments = "客户昵称")
    private String nickName;

    /**
     * 客户简称
     */
    @Column(name = "C_SHORTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户简称", comments = "客户简称")
    private String shortName;

    /**
     * 证件类型
     */
    @Column(name = "C_IDENTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "证件类型", comments = "证件类型")
    private String identType;

    /**
     * 证件号码
     */
    @Column(name = "C_IDENTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "证件号码", comments = "证件号码")
    private String identNo;

    /**
     * 邮编
     */
    @Column(name = "C_ZIPCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "邮编", comments = "邮编")
    private String zipCode;

    /**
     * 通讯地址
     */
    @Column(name = "C_ADDRESS", columnDefinition = "VARCHAR")
    @MetaData(value = "通讯地址", comments = "通讯地址")
    private String address;

    /**
     * 电话
     */
    @Column(name = "C_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "电话", comments = "电话")
    private String phone;

    /**
     * 家庭电话
     */
    @Column(name = "C_HOME_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "家庭电话", comments = "家庭电话")
    private String homePhone;

    /**
     * 单位电话号码
     */
    @Column(name = "C_OFFICE_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "单位电话号码", comments = "单位电话号码")
    private String officePhone;

    /**
     * 传真号码
     */
    @Column(name = "C_FAXNO", columnDefinition = "VARCHAR")
    @MetaData(value = "传真号码", comments = "传真号码")
    private String faxNo;

    /**
     * 手机
     */
    @Column(name = "C_MOBILENO", columnDefinition = "VARCHAR")
    @MetaData(value = "手机", comments = "手机")
    private String mobileNo;

    /**
     * email地址
     */
    @Column(name = "C_EMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "email地址", comments = "email地址")
    private String email;

    /**
     * 微信
     */
    @Column(name = "C_WEIXIN", columnDefinition = "VARCHAR")
    @MetaData(value = "微信", comments = "微信")
    private String weiXin;

    /**
     * 微博
     */
    @Column(name = "C_WEIBO", columnDefinition = "VARCHAR")
    @MetaData(value = "微博", comments = "微博")
    private String weibo;

    /**
     * QQ号码
     */
    @Column(name = "C_QQ", columnDefinition = "VARCHAR")
    @MetaData(value = "QQ号码", comments = "QQ号码")
    private String qq;

    /**
     * 性别
     */
    @Column(name = "C_SEX", columnDefinition = "VARCHAR")
    @MetaData(value = "性别", comments = "性别")
    private String custSex;

    /**
     * 城市地区代码
     */
    @Column(name = "C_CITYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "城市地区代码", comments = "城市地区代码")
    private String cityNo;

    /**
     * 县/区代码
     */
    @Column(name = "C_COUNTY", columnDefinition = "VARCHAR")
    @MetaData(value = "县/区代码", comments = "县/区代码")
    private String county;

    /**
     * 银行编码
     */
    @Column(name = "C_BANKNO", columnDefinition = "VARCHAR")
    @MetaData(value = "银行编码", comments = "银行编码")
    private String bankNo;

    /**
     * 银行账户
     */
    @Column(name = "C_BANKACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "银行账户", comments = "银行账户")
    private String bankAccount;

    /**
     * 银行全称
     */
    @Column(name = "C_BANKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "银行全称", comments = "银行全称")
    private String bankName;

    /**
     * 银行户名
     */
    @Column(name = "C_BANKACCONAME", columnDefinition = "VARCHAR")
    @MetaData(value = "银行户名", comments = "银行户名")
    private String bankAcountName;

    /**
     * 预约单状态
     */
    @Column(name = "C_PRESTATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "预约单状态", comments = "预约单状态")
    private String preStatus;

    /**
     * 申请状态
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "申请状态", comments = "申请状态")
    private String tradeStatus; // 开户状态查询：开户是否成功

    /**
     * 上次申请状态
     */
    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "上次申请状态", comments = "上次申请状态")
    private String lastTradeStatus; // 开户状态查询：开户是否符合通过

    /**
     * 出生日期
     */
    @Column(name = "D_BIRTHDAY", columnDefinition = "VARCHAR")
    @MetaData(value = "出生日期", comments = "出生日期")
    private String birthDay;

    /**
     * 职业
     */
    @Column(name = "C_VOCATION", columnDefinition = "VARCHAR")
    @MetaData(value = "职业", comments = "职业")
    private String vocation;

    /**
     * 学历
     */
    @Column(name = "C_EDUCATION", columnDefinition = "VARCHAR")
    @MetaData(value = "学历", comments = "学历")
    private String education;

    /**
     * 年收入
     */
    @Column(name = "C_INCOME", columnDefinition = "VARCHAR")
    @MetaData(value = "年收入", comments = "年收入")
    private String incoming;

    /**
     * 行业
     */
    @Column(name = "C_CORPVOCATE", columnDefinition = "VARCHAR")
    @MetaData(value = "行业", comments = "行业")
    private String corpVocate;

    /**
     * 企业性质
     */
    @Column(name = "C_CORPROPERTY", columnDefinition = "VARCHAR")
    @MetaData(value = "企业性质", comments = "企业性质")
    private String corpProperty;

    /**
     * 注册资本 0-500万以下，1-500万－1000万，2-1000万－3000万，3-3000万－5000万，4-5000万－10000万，5-10000万以上
     */
    @Column(name = "C_REGCAPITAL", columnDefinition = "VARCHAR")
    @MetaData(value = "注册资本  0-500万以下", comments = "注册资本  0-500万以下，1-500万－1000万，2-1000万－3000万，3-3000万－5000万，4-5000万－10000万，5-10000万以上")
    private String regCapital;

    /**
     * 国籍或地区
     */
    @Column(name = "C_NATIONAL", columnDefinition = "VARCHAR")
    @MetaData(value = "国籍或地区", comments = "国籍或地区")
    private String national;

    /**
     * 投资经历 0：无经验，1：1-3年，2：3-5年，3：5-10年，4：10年以上
     */
    @Column(name = "C_INVEST", columnDefinition = "VARCHAR")
    @MetaData(value = "投资经历 0：无经验", comments = "投资经历 0：无经验，1：1-3年，2：3-5年，3：5-10年，4：10年以上")
    private String investType;

    /**
     * 深圳股东账户
     */
    @Column(name = "C_SZACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "深圳股东账户", comments = "深圳股东账户")
    private String szAccount;

    /**
     * 上海股东账户
     */
    @Column(name = "C_SHACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "上海股东账户", comments = "上海股东账户")
    private String shAccount;

    /**
     * 账户申请日期
     */
    @Column(name = "D_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "账户申请日期", comments = "账户申请日期")
    private String tradeDate;

    /**
     * 账户申请时间
     */
    @Column(name = "T_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "账户申请时间", comments = "账户申请时间")
    private String tradeTime;

    /**
     * 下单日期
     */
    @Column(name = "D_OPERDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "下单日期", comments = "下单日期")
    private String operDate;

    /**
     * 下单时间
     */
    @Column(name = "T_OPERTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "下单时间", comments = "下单时间")
    private String operTime;

    /**
     * 凭证编号
     */
    @Column(name = "C_VOUCHERNO", columnDefinition = "VARCHAR")
    @MetaData(value = "凭证编号 ", comments = "凭证编号 ")
    private String voucherNo;

    /**
     * 密函编号
     */
    @Column(name = "C_CIPHERTEXT", columnDefinition = "VARCHAR")
    @MetaData(value = "密函编号", comments = "密函编号")
    private String cipherText;

    /**
     * 网络地址
     */
    @Column(name = "C_IPADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "网络地址", comments = "网络地址")
    private String ipaddr;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员编码", comments = "操作员编码")
    private String operNo;

    /**
     * 复核人编码
     */
    @Column(name = "C_CHECKNO", columnDefinition = "VARCHAR")
    @MetaData(value = "复核人编码", comments = "复核人编码")
    private String checkerNo;

    /**
     * 法人姓名
     */
    @Column(name = "C_LAWNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "法人姓名", comments = "法人姓名")
    private String lawName;

    /**
     * 法人证件类型
     */
    @Column(name = "C_LAWTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件类型", comments = "法人证件类型")
    private String lawIdentType;

    /**
     * 法人证件号码
     */
    @Column(name = "C_LAWNO", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件号码", comments = "法人证件号码")
    private String lawIdentNo;

    /**
     * 经办人识别方式 1-书面委托，2-印鉴 3-密码，4-证件
     */
    @Column(name = "C_CONTACT_IDENT_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人识别方式 1-书面委托", comments = "经办人识别方式 1-书面委托，2-印鉴 3-密码，4-证件")
    private String contIdentifyType;

    /**
     * 经办人姓名
     */
    @Column(name = "C_CONTACT", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人姓名", comments = "经办人姓名")
    private String contName;

    /**
     * 经办人证件类型
     */
    @Column(name = "C_CONTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件类型", comments = "经办人证件类型")
    private String contIdentType;

    /**
     * 经办人证件号码
     */
    @Column(name = "C_CONTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件号码", comments = "经办人证件号码")
    private String contIdentNo;

    /**
     * 经办人联系电话
     */
    @Column(name = "C_CONTPHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人联系电话", comments = "经办人联系电话")
    private String contPhone;

    /**
     * 经办人email地址
     */
    @Column(name = "C_CONTEMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人email地址", comments = "经办人email地址")
    private String contEmail;

    /**
     * 经办人传真号码
     */
    @Column(name = "C_CONTFAX", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人传真号码", comments = "经办人传真号码")
    private String contFax;

    /**
     * 机构类型；0-保险机构，1-基金公司，2-上市公司，3-信托公司，4-证券公司，5-理财产品，6-企业年金，7-社保基金，8-其他机构
     */
    @Column(name = "C_INSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "机构类型", comments = "机构类型；0-保险机构，1-基金公司，2-上市公司，3-信托公司，4-证券公司，5-理财产品，6-企业年金，7-社保基金，8-其他机构")
    private String instType;

    /**
     * 促销活动代码
     */
    @Column(name = "C_PROMOTION", columnDefinition = "VARCHAR")
    @MetaData(value = "促销活动代码", comments = "促销活动代码")
    private String promotion;

    /**
     * 客户分组；00-普通客户，01重要客户，02-VIP客户
     */
    @Column(name = "C_CUSTGROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "客户分组", comments = "客户分组；00-普通客户，01重要客户，02-VIP客户")
    private String custGroup;

    /**
     * 联行号
     */
    @Column(name = "C_BRANCHBANK", columnDefinition = "VARCHAR")
    @MetaData(value = "联行号", comments = "联行号")
    private String branchBank;

    /**
     * 对帐单寄送方式 共8个字符，每个字符代表一种交易手段，其含义为：第1位：邮寄，第2位：传真，第3位：E-mail，第4位：短消息，第5~8位：保留。每位字符取1表示采用此种手段，取0表示不使用
     */
    @Column(name = "C_DELIVERWAY", columnDefinition = "VARCHAR")
    @MetaData(value = "对帐单寄送方式 共8个字符", comments = "对帐单寄送方式 共8个字符，每个字符代表一种交易手段，其含义为：第1位：邮寄，第2位：传真，第3位：E-mail，第4位：短消息，第5~8位：保留。每位字符取1表示采用此种手段，取0表示不使用")
    private String deliverWay;

    /**
     * 对帐单寄送选择 1-不寄送，2-按月 3-按季，4-半年 5-一年
     */
    @Column(name = "C_DELIVERTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "对帐单寄送选择 1-不寄送", comments = "对帐单寄送选择 1-不寄送，2-按月 3-按季，4-半年 5-一年")
    private String deliverType;

    /**
     * 使用的交易手段 共8个字符，每个字符代表一种交易手段，其含义为：第1位：CALLCENTER 第2位：INTERNET 第3位：自助终端 第4~8位：保留 每个字符取1表示使用此种手段，取0表示不使用
     */
    @Column(name = "C_TRADING", columnDefinition = "VARCHAR")
    @MetaData(value = "使用的交易手段 共8个字符", comments = "使用的交易手段 共8个字符，每个字符代表一种交易手段，其含义为：第1位：CALLCENTER 第2位：INTERNET 第3位：自助终端 第4~8位：保留 每个字符取1表示使用此种手段，取0表示不使用")
    private String tradeMethod;

    /**
     * 多渠道开户标志 0-首次开设TA帐户 1-已经其它渠道开户
     */
    @Column(name = "C_MULTIACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "多渠道开户标志  0-首次开设TA帐户 1-已经其它渠道开户", comments = "多渠道开户标志  0-首次开设TA帐户 1-已经其它渠道开户")
    private Boolean muiltAcco;

    /**
     * 撤单标志
     */
    @Column(name = "C_CANCELFLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "撤单标志", comments = "撤单标志")
    private String canceled;

    /**
     * 下单方式(0 客户下单 1 系统自动下单)
     */
    @Column(name = "C_ORDER_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "下单方式(0 客户下单 1 系统自动下单)", comments = "下单方式(0 客户下单 1 系统自动下单)")
    private String orderWay;

    /**
     * 银行卡签/解约状态0处理中1成功2失败
     */
    @Column(name = "C_SIGNSTATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "银行卡签/解约状态0处理中1成功2失败", comments = "银行卡签/解约状态0处理中1成功2失败")
    private Boolean signStatus;

    /**
     * 操作机构；用于远程接口调用
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构；用于远程接口调用")
    private String operOrg;

    /**
     * 客户分类；01普通客户；02企业年金计划；03银行、券商、信托公司等的理财产品或理财计划；04保险产品；05社保基金组合；09其他
     */
    @Column(name = "C_CUSTCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "客户分类", comments = "客户分类；01普通客户；02企业年金计划；03银行、券商、信托公司等的理财产品或理财计划；04保险产品；05社保基金组合；09其他")
    private String custClass;

    /**
     * 未成年人标志 0-否，1-是
     */
    @Column(name = "C_MINOR_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "未成年人标志 0-否", comments = "未成年人标志 0-否，1-是")
    private Boolean minor;

    /**
     * 未成年人ID号 原身份证字段在未成人开户时代表监护人的身份证号
     */
    @Column(name = "C_MINORID", columnDefinition = "VARCHAR")
    @MetaData(value = "未成年人ID号 原身份证字段在未成人开户时代表监护人的身份证号", comments = "未成年人ID号 原身份证字段在未成人开户时代表监护人的身份证号")
    private String minorId;

    /**
     * 经纪人
     */
    @Column(name = "C_BROKER", columnDefinition = "VARCHAR")
    @MetaData(value = "经纪人", comments = "经纪人")
    private String broker;

    /**
     * 客户经理代码
     */
    @Column(name = "C_CUSTMANAGER", columnDefinition = "VARCHAR")
    @MetaData(value = "客户经理代码", comments = "客户经理代码")
    private String custManager;

    /**
     * 推荐人姓名
     */
    @Column(name = "C_REFERRAL", columnDefinition = "VARCHAR")
    @MetaData(value = "推荐人姓名", comments = "推荐人姓名")
    private String referral;

    /**
     * 推荐人手机号码
     */
    @Column(name = "C_REFERRALMOBILE", columnDefinition = "VARCHAR")
    @MetaData(value = "推荐人手机号码", comments = "推荐人手机号码")
    private String referralMobile;

    /**
     * 受理方式 ；0柜台，1电话，2网上交易，3传真，4手机，5其它
     */
    @Column(name = "C_ACCEPT", columnDefinition = "VARCHAR")
    @MetaData(value = "受理方式 ", comments = "受理方式 ；0柜台，1电话，2网上交易，3传真，4手机，5其它")
    private String acceptMode;

    /**
     * 经办人手机号码
     */
    @Column(name = "C_CONTMOBILE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人手机号码", comments = "经办人手机号码")
    private String contMobileNo;

    /**
     * 证件有效期
     */
    @Column(name = "D_CERTVALIDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "证件有效期", comments = "证件有效期")
    private String certValidDate;

    /**
     * 经办人证件有效期
     */
    @Column(name = "D_CONTCERT_VALIDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件有效期", comments = "经办人证件有效期")
    private String contCertValidDate;

    /**
     * 法人证件有效期
     */
    @Column(name = "D_LAWCERT_VALIDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件有效期", comments = "法人证件有效期")
    private String lawCertValidDate;

    /**
     * 工作单位
     */
    @Column(name = "C_CORPNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "工作单位", comments = "工作单位")
    private String corpName;

    /**
     * 客户风险等级
     */
    @Column(name = "C_RISKNO", columnDefinition = "VARCHAR")
    @MetaData(value = "客户风险等级", comments = "客户风险等级")
    private String riskNo;

    /**
     * 关联协议号
     */
    @Column(name = "C_SOURCE_KEY", columnDefinition = "VARCHAR")
    @MetaData(value = "关联协议号", comments = "关联协议号")
    private String sourceKey;

    /**
     * 操作方式；0-柜台；2-网上交易
     */
    @Column(name = "C_OPERWAY", columnDefinition = "VARCHAR")
    @MetaData(value = "操作方式", comments = "操作方式；0-柜台；2-网上交易")
    private String operWay;

    /**
     * 资金账户
     */
    @Column(name = "L_MONEYACCOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "资金账户", comments = "资金账户")
    private Long moneyAccount;

    /**
     * 账户确认日期
     */
    @Column(name = "D_CDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "账户确认日期", comments = "账户确认日期")
    private String confirmDate;

    /**
     * 错误代码
     */
    @Column(name = "C_ERRCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "错误代码", comments = "错误代码")
    private String errCode;

    /**
     * 账户申请结果描述
     */
    @Column(name = "C_ERRDETAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "账户申请结果描述", comments = "账户申请结果描述")
    private String errDetail;

    /**
     * 交易处理返回代码系统处理后返回错误信息
     */
    @Column(name = "C_CAUSE", columnDefinition = "VARCHAR")
    @MetaData(value = "交易处理返回代码系统处理后返回错误信息", comments = "交易处理返回代码系统处理后返回错误信息")
    private String confirmCause;

    /**
     * 确认单号
     */
    @Column(name = "C_CSERIALNO", columnDefinition = "VARCHAR")
    @MetaData(value = "确认单号", comments = "确认单号")
    private String confirmSerialno;

    /**
     * 对方交易帐号 对代理人申报008或058，此处赋值为新交易帐号
     */
    @Column(name = "C_TAGET_TRADEACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "对方交易帐号 对代理人申报008或058", comments = "对方交易帐号 对代理人申报008或058，此处赋值为新交易帐号")
    private String tagetTradeAccount;

    /**
     * 基金账户卡的凭证号
     */
    @Column(name = "C_ACCOUNTCARDNO", columnDefinition = "VARCHAR")
    @MetaData(value = "基金账户卡的凭证号", comments = "基金账户卡的凭证号")
    private String accountCardNo;

    /**
     * 摘要说明
     */
    @Column(name = "C_SPECIFICATION", columnDefinition = "VARCHAR")
    @MetaData(value = "摘要说明", comments = "摘要说明")
    private String specification;

    /**
     * 冻结截止日期 格式为：YYYYMMDD
     */
    @Column(name = "D_FROZENENDLINE", columnDefinition = "VARCHAR")
    @MetaData(value = "冻结截止日期  格式为：YYYYMMDD", comments = "冻结截止日期  格式为：YYYYMMDD")
    private String frozenEndLine;

    /**
     * 冻结原因 0-司法冻结，1-柜台冻结 2-质押冻结， 3-质押(司法冻结) 4-柜台(司法冻结)
     */
    @Column(name = "C_FROZENCAUSE", columnDefinition = "VARCHAR")
    @MetaData(value = "冻结原因  0-司法冻结", comments = "冻结原因  0-司法冻结，1-柜台冻结 2-质押冻结， 3-质押(司法冻结) 4-柜台(司法冻结)")
    private String frozenCause;

    /**
     * 原TA确认流水号
     */
    @Column(name = "C_OLD_CSERIALNO", columnDefinition = "VARCHAR")
    @MetaData(value = "原TA确认流水号", comments = "原TA确认流水号")
    private String originConfirmNo;

    /**
     * 原申请单编号
     */
    @Column(name = "C_OLD_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "原申请单编号", comments = "原申请单编号")
    private String originRequestNo;

    /**
     * TA的原确认日期
     */
    @Column(name = "D_OLD_CDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "TA的原确认日期", comments = "TA的原确认日期")
    private String originConfirmDate;

    /**
     * 机构客户的复核人员名字
     */
    @Column(name = "C_CHECKER", columnDefinition = "VARCHAR")
    @MetaData(value = "机构客户的复核人员名字", comments = "机构客户的复核人员名字")
    private String checker;

    /**
     * 基金公司客户编号
     */
    @Column(name = "C_SALE_CUSTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "基金公司客户编号", comments = "基金公司客户编号")
    private String saleCustNo;

    /**
     * 基金公司申请单号
     */
    @Column(name = "C_SALE_REQUESTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "基金公司申请单号", comments = "基金公司申请单号")
    private String saleRequestNo;

    /**
     * 经办人编号
     */
    @Column(name = "L_CONTACTOR", columnDefinition = "INTEGER")
    @MetaData(value = "经办人编号", comments = "经办人编号")
    private Long contactor;

    /**
     * 签约协议号
     */
    @Column(name = "C_CONTRACTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "签约协议号", comments = "签约协议号")
    private String contractNo;

    /**
     * 经办人编号序列号，用于大成基金
     */
    @Column(name = "L_CONTACTOR_SERIAL", columnDefinition = "INTEGER")
    @MetaData(value = "经办人申请序列号", comments = "经办人编号序列号")
    private Long contactorSerial;

    /**
     * 直销网点编码定义
     */
    @Column(name = "C_SALE_NETNO", columnDefinition = "VARCHAR")
    @MetaData(value = "网点编码", comments = "网点编码")
    private String saleNetNo;

    /**
     * 直销分中心编码定义
     */
    @Column(name = "C_SALE_PAYCENTER", columnDefinition = "VARCHAR")
    @MetaData(value = "分中心", comments = "分中心")
    private String salePayCenterNo;

    /**
     * 销售商交易账户
     */
    @Column(name = "C_SALE_TRADEACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "销售商交易账户", comments = "销售商交易账户")
    private String saleTradeAccount;

    /**
     * 处理状态，0未 处理，1已处理；指是否发送到基金公司
     */
    @Column(name = "C_DEAL", columnDefinition = "VARCHAR")
    @MetaData(value = "处理状态", comments = "数据发送处理状态")
    private String dealFlag;

    /**
     * 审批状态，0未审批，1已审批，2终审
     */
    @Column(name = "C_ADUIT", columnDefinition = "VARCHAR")
    @MetaData(value = "审批状态", comments = "审批状态，0未审批，1已审批，2终审")
    private String aduitFlag;

    @Transient
    private BigDecimal regBalance;
    @Transient
    private String businLicRegDate;
    @Transient
    private BigDecimal paidCapital;
    @Transient
    private Integer personCount;
    @Transient
    private BigDecimal operateArea;
    @Transient
    private Integer ownerShipYear;
    @Transient
    private String busiScope;
    @Transient
    private String setupDate;
    @Transient
    private String regAddr;
    @Transient
    private String validDate;
    // add by hubl
    @Transient
    private String certValidForever;
    @Transient
    private String isThreeInOne;
    @Transient
    private String lawCertValidForever;
    
    @Transient
    private Map<String, String> attach;

    public Map<String, String> getAttach() {
        return this.attach;
    }

    public void setAttach(Map<String, String> anAttach) {
        this.attach = anAttach;
    }

    /**
     * 基金公司资金账户
     */
    @Column(name = "C_SALE_MONEYACCOUNT", columnDefinition = "VARCHAR")
    @MetaData(value = "基金公司资金账户", comments = "基金公司资金账户")
    private String saleMoneyAccount;

    /**
     * 组织机构代码证
     */
    @Column(name = "C_BUSI_LICENCE", columnDefinition = "VARCHAR")
    @MetaData(value = "组织机构代码证", comments = "组织机构代码证")
    private String busiLicence;

    private String coreList;
    
    public String getCoreList() {
        return this.coreList;
    }

    public void setCoreList(String anCoreList) {
        this.coreList = anCoreList;
    }

    private static final long serialVersionUID = 1443065594917L;

    /**
     * 博时开户资料处理
     * @param anKey
     * @param anValue
     */
    public void addAttachValue(String anKey, String anValue) {
        if (this.attach == null) {
            this.attach = new LinkedCaseInsensitiveMap();
        }
        this.attach.put(anKey, anValue);
    }

    public String removeAttachValue(String anKey) {
        if (this.attach != null) {
            return this.attach.remove(anKey);
        }
        return "";
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getTano() {
        return tano;
    }

    public void setTano(String tano) {
        this.tano = tano == null ? null : tano.trim();
    }

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount == null ? null : tradeAccount.trim();
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount == null ? null : fundAccount.trim();
    }

    public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo == null ? null : agencyNo.trim();
    }

    public String getPayCenterNo() {
        return payCenterNo;
    }

    public void setPayCenterNo(String payCenterNo) {
        this.payCenterNo = payCenterNo == null ? null : payCenterNo.trim();
    }

    public String getNetNo() {
        return netNo;
    }

    public void setNetNo(String netNo) {
        this.netNo = netNo == null ? null : netNo.trim();
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag == null ? null : businFlag.trim();
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType == null ? null : custType.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType == null ? null : identType.trim();
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone == null ? null : homePhone.trim();
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone == null ? null : officePhone.trim();
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo == null ? null : faxNo.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin == null ? null : weiXin.trim();
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo == null ? null : weibo.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getCustSex() {
        return custSex;
    }

    public void setCustSex(String custSex) {
        this.custSex = custSex == null ? null : custSex.trim();
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo == null ? null : cityNo.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAcountName() {
        return bankAcountName;
    }

    public void setBankAcountName(String bankAcountName) {
        this.bankAcountName = bankAcountName == null ? null : bankAcountName.trim();
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus == null ? null : preStatus.trim();
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus == null ? null : tradeStatus.trim();
    }

    public String getLastTradeStatus() {
        return lastTradeStatus;
    }

    public void setLastTradeStatus(String lastTradeStatus) {
        this.lastTradeStatus = lastTradeStatus == null ? null : lastTradeStatus.trim();
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay == null ? null : birthDay.trim();
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation == null ? null : vocation.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming == null ? null : incoming.trim();
    }

    public String getCorpVocate() {
        return corpVocate;
    }

    public void setCorpVocate(String corpVocate) {
        this.corpVocate = corpVocate == null ? null : corpVocate.trim();
    }

    public String getCorpProperty() {
        return corpProperty;
    }

    public void setCorpProperty(String corpProperty) {
        this.corpProperty = corpProperty == null ? null : corpProperty.trim();
    }

    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital == null ? null : regCapital.trim();
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national == null ? null : national.trim();
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType == null ? null : investType.trim();
    }

    public String getSzAccount() {
        return szAccount;
    }

    public void setSzAccount(String szAccount) {
        this.szAccount = szAccount == null ? null : szAccount.trim();
    }

    public String getShAccount() {
        return shAccount;
    }

    public void setShAccount(String shAccount) {
        this.shAccount = shAccount == null ? null : shAccount.trim();
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate == null ? null : tradeDate.trim();
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime == null ? null : tradeTime.trim();
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate == null ? null : operDate.trim();
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime == null ? null : operTime.trim();
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo == null ? null : voucherNo.trim();
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText == null ? null : cipherText.trim();
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo == null ? null : operNo.trim();
    }

    public String getCheckerNo() {
        return checkerNo;
    }

    public void setCheckerNo(String checkerNo) {
        this.checkerNo = checkerNo == null ? null : checkerNo.trim();
    }

    public String getLawName() {
        return lawName;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName == null ? null : lawName.trim();
    }

    public String getLawIdentType() {
        return lawIdentType;
    }

    public void setLawIdentType(String lawIdentType) {
        this.lawIdentType = lawIdentType == null ? null : lawIdentType.trim();
    }

    public String getLawIdentNo() {
        return lawIdentNo;
    }

    public void setLawIdentNo(String lawIdentNo) {
        this.lawIdentNo = lawIdentNo == null ? null : lawIdentNo.trim();
    }

    public String getContIdentifyType() {
        return contIdentifyType;
    }

    public void setContIdentifyType(String contIdentifyType) {
        this.contIdentifyType = contIdentifyType == null ? null : contIdentifyType.trim();
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(String contIdentType) {
        this.contIdentType = contIdentType == null ? null : contIdentType.trim();
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(String contIdentNo) {
        this.contIdentNo = contIdentNo == null ? null : contIdentNo.trim();
    }

    public String getContPhone() {
        return contPhone;
    }

    public void setContPhone(String contPhone) {
        this.contPhone = contPhone == null ? null : contPhone.trim();
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType == null ? null : instType.trim();
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion == null ? null : promotion.trim();
    }

    public String getCustGroup() {
        return custGroup;
    }

    public void setCustGroup(String custGroup) {
        this.custGroup = custGroup == null ? null : custGroup.trim();
    }

    public String getBranchBank() {
        return branchBank;
    }

    public void setBranchBank(String branchBank) {
        this.branchBank = branchBank == null ? null : branchBank.trim();
    }

    public String getDeliverWay() {
        return deliverWay;
    }

    public void setDeliverWay(String deliverWay) {
        this.deliverWay = deliverWay == null ? null : deliverWay.trim();
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType == null ? null : deliverType.trim();
    }

    public String getTradeMethod() {
        return tradeMethod;
    }

    public void setTradeMethod(String tradeMethod) {
        this.tradeMethod = tradeMethod == null ? null : tradeMethod.trim();
    }

    public Boolean getMuiltAcco() {
        return muiltAcco;
    }

    public void setMuiltAcco(Boolean muiltAcco) {
        this.muiltAcco = muiltAcco;
    }

    public String getCanceled() {
        return canceled;
    }

    public void setCanceled(String canceled) {
        this.canceled = canceled == null ? null : canceled.trim();
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay == null ? null : orderWay.trim();
    }

    public Boolean getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Boolean signStatus) {
        this.signStatus = signStatus;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getCustClass() {
        return custClass;
    }

    public void setCustClass(String custClass) {
        this.custClass = custClass == null ? null : custClass.trim();
    }

    public Boolean getMinor() {
        return minor;
    }

    public void setMinor(Boolean minor) {
        this.minor = minor;
    }

    public String getMinorId() {
        return minorId;
    }

    public void setMinorId(String minorId) {
        this.minorId = minorId == null ? null : minorId.trim();
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getCustManager() {
        return custManager;
    }

    public void setCustManager(String custManager) {
        this.custManager = custManager == null ? null : custManager.trim();
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral == null ? null : referral.trim();
    }

    public String getReferralMobile() {
        return referralMobile;
    }

    public void setReferralMobile(String referralMobile) {
        this.referralMobile = referralMobile == null ? null : referralMobile.trim();
    }

    public String getAcceptMode() {
        return acceptMode;
    }

    public void setAcceptMode(String acceptMode) {
        this.acceptMode = acceptMode == null ? null : acceptMode.trim();
    }

    public String getContMobileNo() {
        return contMobileNo;
    }

    public void setContMobileNo(String contMobileNo) {
        this.contMobileNo = contMobileNo == null ? null : contMobileNo.trim();
    }

    public String getCertValidDate() {
        return certValidDate;
    }

    public void setCertValidDate(String certValidDate) {
        this.certValidDate = certValidDate == null ? null : certValidDate.trim();
    }

    public String getContCertValidDate() {
        return contCertValidDate;
    }

    public void setContCertValidDate(String contCertValidDate) {
        this.contCertValidDate = contCertValidDate == null ? null : contCertValidDate.trim();
    }

    public String getLawCertValidDate() {
        return lawCertValidDate;
    }

    public void setLawCertValidDate(String lawCertValidDate) {
        this.lawCertValidDate = lawCertValidDate == null ? null : lawCertValidDate.trim();
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }

    public String getRiskNo() {
        return riskNo;
    }

    public void setRiskNo(String riskNo) {
        this.riskNo = riskNo == null ? null : riskNo.trim();
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey == null ? null : sourceKey.trim();
    }

    public String getOperWay() {
        return operWay;
    }

    public void setOperWay(String operWay) {
        this.operWay = operWay == null ? null : operWay.trim();
    }

    public Long getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(Long moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate == null ? null : confirmDate.trim();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public String getErrDetail() {
        return errDetail;
    }

    public void setErrDetail(String errDetail) {
        this.errDetail = errDetail == null ? null : errDetail.trim();
    }

    public String getConfirmCause() {
        return confirmCause;
    }

    public void setConfirmCause(String confirmCause) {
        this.confirmCause = confirmCause == null ? null : confirmCause.trim();
    }

    public String getConfirmSerialno() {
        return confirmSerialno;
    }

    public void setConfirmSerialno(String confirmSerialno) {
        this.confirmSerialno = confirmSerialno == null ? null : confirmSerialno.trim();
    }

    public String getTagetTradeAccount() {
        return tagetTradeAccount;
    }

    public void setTagetTradeAccount(String tagetTradeAccount) {
        this.tagetTradeAccount = tagetTradeAccount == null ? null : tagetTradeAccount.trim();
    }

    public String getAccountCardNo() {
        return accountCardNo;
    }

    public void setAccountCardNo(String accountCardNo) {
        this.accountCardNo = accountCardNo == null ? null : accountCardNo.trim();
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification == null ? null : specification.trim();
    }

    public String getFrozenEndLine() {
        return frozenEndLine;
    }

    public void setFrozenEndLine(String frozenEndLine) {
        this.frozenEndLine = frozenEndLine == null ? null : frozenEndLine.trim();
    }

    public String getFrozenCause() {
        return frozenCause;
    }

    public void setFrozenCause(String frozenCause) {
        this.frozenCause = frozenCause == null ? null : frozenCause.trim();
    }

    public String getOriginConfirmNo() {
        return originConfirmNo;
    }

    public void setOriginConfirmNo(String originConfirmNo) {
        this.originConfirmNo = originConfirmNo == null ? null : originConfirmNo.trim();
    }

    public String getOriginRequestNo() {
        return originRequestNo;
    }

    public void setOriginRequestNo(String originRequestNo) {
        this.originRequestNo = originRequestNo == null ? null : originRequestNo.trim();
    }

    public String getOriginConfirmDate() {
        return originConfirmDate;
    }

    public void setOriginConfirmDate(String originConfirmDate) {
        this.originConfirmDate = originConfirmDate == null ? null : originConfirmDate.trim();
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker == null ? null : checker.trim();
    }

    public String getSaleCustNo() {
        return saleCustNo;
    }

    public void setSaleCustNo(String saleCustNo) {
        this.saleCustNo = saleCustNo == null ? null : saleCustNo.trim();
    }

    public String getSaleRequestNo() {
        return saleRequestNo;
    }

    public void setSaleRequestNo(String saleRequestNo) {
        this.saleRequestNo = saleRequestNo == null ? null : saleRequestNo.trim();
    }

    public Long getContactor() {
        return contactor;
    }

    public void setContactor(Long contactor) {
        this.contactor = contactor;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getContactorSerial() {
        return this.contactorSerial;
    }

    public void setContactorSerial(Long anContactorSerial) {
        this.contactorSerial = anContactorSerial;
    }

    public String getSaleNetNo() {
        return this.saleNetNo;
    }

    public void setSaleNetNo(String anSaleNetNo) {
        this.saleNetNo = anSaleNetNo;
    }

    public String getSalePayCenterNo() {
        return this.salePayCenterNo;
    }

    public void setSalePayCenterNo(String anSalePayCenterNo) {
        this.salePayCenterNo = anSalePayCenterNo;
    }

    public String getSaleTradeAccount() {
        return this.saleTradeAccount;
    }

    public void setSaleTradeAccount(String anSaleTradeAccount) {
        this.saleTradeAccount = anSaleTradeAccount;
    }

    public String getDealFlag() {
        return this.dealFlag;
    }

    public void setDealFlag(String anDealFlag) {
        this.dealFlag = anDealFlag;
    }

    public String getContEmail() {
        return this.contEmail;
    }

    public void setContEmail(String anContEmail) {
        this.contEmail = anContEmail;
    }

    public String getContFax() {
        return this.contFax;
    }

    public void setContFax(String anContFax) {
        this.contFax = anContFax;
    }

    public String getAduitFlag() {
        return this.aduitFlag;
    }

    public void setAduitFlag(String anAduitFlag) {
        this.aduitFlag = anAduitFlag;
    }

    public String getSaleMoneyAccount() {
        return this.saleMoneyAccount;
    }

    public void setSaleMoneyAccount(String anSaleMoneyAccount) {
        this.saleMoneyAccount = anSaleMoneyAccount;
    }

    public String getBusiLicence() {
        return this.busiLicence;
    }

    public void setBusiLicence(String anBusiLicence) {
        this.busiLicence = anBusiLicence;
    }

    public BigDecimal getRegBalance() {
        return this.regBalance;
    }

    public void setRegBalance(BigDecimal anRegBalance) {
        this.regBalance = anRegBalance;
    }

    public String getBusinLicRegDate() {
        return this.businLicRegDate;
    }

    public void setBusinLicRegDate(String anBusinLicRegDate) {
        this.businLicRegDate = anBusinLicRegDate;
    }

    public BigDecimal getPaidCapital() {
        return this.paidCapital;
    }

    public void setPaidCapital(BigDecimal anPaidCapital) {
        this.paidCapital = anPaidCapital;
    }

    public Integer getPersonCount() {
        return this.personCount;
    }

    public void setPersonCount(Integer anPersonCount) {
        this.personCount = anPersonCount;
    }

    public BigDecimal getOperateArea() {
        return this.operateArea;
    }

    public void setOperateArea(BigDecimal anOperateArea) {
        this.operateArea = anOperateArea;
    }

    public Integer getOwnerShipYear() {
        return this.ownerShipYear;
    }

    public void setOwnerShipYear(Integer anOwnerShipYear) {
        this.ownerShipYear = anOwnerShipYear;
    }

    public String getBusiScope() {
        return this.busiScope;
    }

    public void setBusiScope(String anBusiScope) {
        this.busiScope = anBusiScope;
    }

    public String getSetupDate() {
        return this.setupDate;
    }

    public void setSetupDate(String anSetupDate) {
        this.setupDate = anSetupDate;
    }

    public String getRegAddr() {
        return this.regAddr;
    }

    public void setRegAddr(String anRegAddr) {
        this.regAddr = anRegAddr;
    }

    public String getValidDate() {
        return this.validDate;
    }

    public void setValidDate(String anValidDate) {
        this.validDate = anValidDate;
        if (BetterStringUtils.isBlank(certValidDate)){
            this.certValidDate = anValidDate;
        }
    }

    public String getCertValidForever() {
        return this.certValidForever;
    }

    public void setCertValidForever(String anCertValidForever) {
        this.certValidForever = anCertValidForever;
    }

    public String getIsThreeInOne() {
        return this.isThreeInOne;
    }

    public void setIsThreeInOne(String anIsThreeInOne) {
        this.isThreeInOne = anIsThreeInOne;
    }

    public String getLawCertValidForever() {
        return this.lawCertValidForever;
    }

    public void setLawCertValidForever(String anLawCertValidForever) {
        this.lawCertValidForever = anLawCertValidForever;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", requestNo=").append(requestNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", tano=").append(tano);
        sb.append(", tradeAccount=").append(tradeAccount);
        sb.append(", fundAccount=").append(fundAccount);
        sb.append(", agencyNo=").append(agencyNo);
        sb.append(", payCenterNo=").append(payCenterNo);
        sb.append(", netNo=").append(netNo);
        sb.append(", businFlag=").append(businFlag);
        sb.append(", custType=").append(custType);
        sb.append(", custName=").append(custName);
        sb.append(", nickName=").append(nickName);
        sb.append(", shortName=").append(shortName);
        sb.append(", identType=").append(identType);
        sb.append(", identNo=").append(identNo);
        sb.append(", zipCode=").append(zipCode);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", homePhone=").append(homePhone);
        sb.append(", officePhone=").append(officePhone);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", mobileNo=").append(mobileNo);
        sb.append(", email=").append(email);
        sb.append(", weiXin=").append(weiXin);
        sb.append(", weibo=").append(weibo);
        sb.append(", qq=").append(qq);
        sb.append(", custSex=").append(custSex);
        sb.append(", cityNo=").append(cityNo);
        sb.append(", county=").append(county);
        sb.append(", bankNo=").append(bankNo);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankAcountName=").append(bankAcountName);
        sb.append(", preStatus=").append(preStatus);
        sb.append(", tradeStatus=").append(tradeStatus);
        sb.append(", lastTradeStatus=").append(lastTradeStatus);
        sb.append(", birthDay=").append(birthDay);
        sb.append(", vocation=").append(vocation);
        sb.append(", education=").append(education);
        sb.append(", incoming=").append(incoming);
        sb.append(", corpVocate=").append(corpVocate);
        sb.append(", corpProperty=").append(corpProperty);
        sb.append(", regCapital=").append(regCapital);
        sb.append(", national=").append(national);
        sb.append(", investType=").append(investType);
        sb.append(", szAccount=").append(szAccount);
        sb.append(", shAccount=").append(shAccount);
        sb.append(", tradeDate=").append(tradeDate);
        sb.append(", tradeTime=").append(tradeTime);
        sb.append(", operDate=").append(operDate);
        sb.append(", operTime=").append(operTime);
        sb.append(", voucherNo=").append(voucherNo);
        sb.append(", cipherText=").append(cipherText);
        sb.append(", ipaddr=").append(ipaddr);
        sb.append(", operNo=").append(operNo);
        sb.append(", checkerNo=").append(checkerNo);
        sb.append(", lawName=").append(lawName);
        sb.append(", lawIdentType=").append(lawIdentType);
        sb.append(", lawIdentNo=").append(lawIdentNo);
        sb.append(", contIdentifyType=").append(contIdentifyType);
        sb.append(", contName=").append(contName);
        sb.append(", contIdentType=").append(contIdentType);
        sb.append(", contIdentNo=").append(contIdentNo);
        sb.append(", contPhone=").append(contPhone);
        sb.append(", contMobileNo=").append(contMobileNo);
        sb.append(", contEmail=").append(contEmail);
        sb.append(", contFax=").append(contFax);
        sb.append(", instType=").append(instType);
        sb.append(", promotion=").append(promotion);
        sb.append(", custGroup=").append(custGroup);
        sb.append(", branchBank=").append(branchBank);
        sb.append(", deliverWay=").append(deliverWay);
        sb.append(", deliverType=").append(deliverType);
        sb.append(", tradeMethod=").append(tradeMethod);
        sb.append(", muiltAcco=").append(muiltAcco);
        sb.append(", canceled=").append(canceled);
        sb.append(", orderWay=").append(orderWay);
        sb.append(", signStatus=").append(signStatus);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", custClass=").append(custClass);
        sb.append(", minor=").append(minor);
        sb.append(", minorId=").append(minorId);
        sb.append(", broker=").append(broker);
        sb.append(", custManager=").append(custManager);
        sb.append(", referral=").append(referral);
        sb.append(", referralMobile=").append(referralMobile);
        sb.append(", acceptMode=").append(acceptMode);
        sb.append(", certValidDate=").append(certValidDate);
        sb.append(", contCertValidDate=").append(contCertValidDate);
        sb.append(", lawCertValidDate=").append(lawCertValidDate);
        sb.append(", corpName=").append(corpName);
        sb.append(", riskNo=").append(riskNo);
        sb.append(", sourceKey=").append(sourceKey);
        sb.append(", operWay=").append(operWay);
        sb.append(", moneyAccount=").append(moneyAccount);
        sb.append(", confirmDate=").append(confirmDate);
        sb.append(", errCode=").append(errCode);
        sb.append(", errDetail=").append(errDetail);
        sb.append(", confirmCause=").append(confirmCause);
        sb.append(", confirmSerialno=").append(confirmSerialno);
        sb.append(", tagetTradeAccount=").append(tagetTradeAccount);
        sb.append(", accountCardNo=").append(accountCardNo);
        sb.append(", specification=").append(specification);
        sb.append(", frozenEndLine=").append(frozenEndLine);
        sb.append(", frozenCause=").append(frozenCause);
        sb.append(", originConfirmNo=").append(originConfirmNo);
        sb.append(", originRequestNo=").append(originRequestNo);
        sb.append(", originConfirmDate=").append(originConfirmDate);
        sb.append(", checker=").append(checker);
        sb.append(", saleCustNo=").append(saleCustNo);
        sb.append(", saleRequestNo=").append(saleRequestNo);
        sb.append(", contractNo=").append(contractNo);
        sb.append(", contactorSerial=").append(contactorSerial);
        sb.append(", saleNetNo=").append(saleNetNo);
        sb.append(", salePayCenterNo=").append(salePayCenterNo);
        sb.append(", saleTradeAccount=").append(saleTradeAccount);
        sb.append(", dealFlag=").append(dealFlag);
        sb.append(", aduitFlag=").append(aduitFlag);
        sb.append(", saleMoneyAccount=").append(saleMoneyAccount);
        sb.append(", busiLicence=").append(busiLicence);
        sb.append(", regBalance=").append(regBalance);
        sb.append(", businLicRegDate=").append(businLicRegDate);
        sb.append(", paidCapital=").append(paidCapital);
        sb.append(", personCount=").append(personCount);
        sb.append(", operateArea=").append(operateArea);
        sb.append(", ownerShipYear=").append(ownerShipYear);
        sb.append(", busiScope=").append(busiScope);
        sb.append(", setupDate=").append(setupDate);
        sb.append(", regAddr=").append(regAddr);
        sb.append(", validDate=").append(validDate);
       sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SaleAccoRequestInfo other = (SaleAccoRequestInfo) that;
        return (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getTano() == null ? other.getTano() == null : this.getTano().equals(other.getTano()))
                && (this.getTradeAccount() == null ? other.getTradeAccount() == null : this.getTradeAccount().equals(other.getTradeAccount()))
                && (this.getFundAccount() == null ? other.getFundAccount() == null : this.getFundAccount().equals(other.getFundAccount()))
                && (this.getAgencyNo() == null ? other.getAgencyNo() == null : this.getAgencyNo().equals(other.getAgencyNo()))
                && (this.getPayCenterNo() == null ? other.getPayCenterNo() == null : this.getPayCenterNo().equals(other.getPayCenterNo()))
                && (this.getNetNo() == null ? other.getNetNo() == null : this.getNetNo().equals(other.getNetNo()))
                && (this.getBusinFlag() == null ? other.getBusinFlag() == null : this.getBusinFlag().equals(other.getBusinFlag()))
                && (this.getCustType() == null ? other.getCustType() == null : this.getCustType().equals(other.getCustType()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName()))
                && (this.getIdentType() == null ? other.getIdentType() == null : this.getIdentType().equals(other.getIdentType()))
                && (this.getIdentNo() == null ? other.getIdentNo() == null : this.getIdentNo().equals(other.getIdentNo()))
                && (this.getZipCode() == null ? other.getZipCode() == null : this.getZipCode().equals(other.getZipCode()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getHomePhone() == null ? other.getHomePhone() == null : this.getHomePhone().equals(other.getHomePhone()))
                && (this.getOfficePhone() == null ? other.getOfficePhone() == null : this.getOfficePhone().equals(other.getOfficePhone()))
                && (this.getFaxNo() == null ? other.getFaxNo() == null : this.getFaxNo().equals(other.getFaxNo()))
                && (this.getMobileNo() == null ? other.getMobileNo() == null : this.getMobileNo().equals(other.getMobileNo()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getWeiXin() == null ? other.getWeiXin() == null : this.getWeiXin().equals(other.getWeiXin()))
                && (this.getWeibo() == null ? other.getWeibo() == null : this.getWeibo().equals(other.getWeibo()))
                && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
                && (this.getCustSex() == null ? other.getCustSex() == null : this.getCustSex().equals(other.getCustSex()))
                && (this.getCityNo() == null ? other.getCityNo() == null : this.getCityNo().equals(other.getCityNo()))
                && (this.getCounty() == null ? other.getCounty() == null : this.getCounty().equals(other.getCounty()))
                && (this.getBankNo() == null ? other.getBankNo() == null : this.getBankNo().equals(other.getBankNo()))
                && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
                && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
                && (this.getBankAcountName() == null ? other.getBankAcountName() == null : this.getBankAcountName().equals(other.getBankAcountName()))
                && (this.getPreStatus() == null ? other.getPreStatus() == null : this.getPreStatus().equals(other.getPreStatus()))
                && (this.getTradeStatus() == null ? other.getTradeStatus() == null : this.getTradeStatus().equals(other.getTradeStatus()))
                && (this.getLastTradeStatus() == null ? other.getLastTradeStatus() == null : this.getLastTradeStatus().equals(
                        other.getLastTradeStatus()))
                && (this.getBirthDay() == null ? other.getBirthDay() == null : this.getBirthDay().equals(other.getBirthDay()))
                && (this.getVocation() == null ? other.getVocation() == null : this.getVocation().equals(other.getVocation()))
                && (this.getEducation() == null ? other.getEducation() == null : this.getEducation().equals(other.getEducation()))
                && (this.getIncoming() == null ? other.getIncoming() == null : this.getIncoming().equals(other.getIncoming()))
                && (this.getCorpVocate() == null ? other.getCorpVocate() == null : this.getCorpVocate().equals(other.getCorpVocate()))
                && (this.getCorpProperty() == null ? other.getCorpProperty() == null : this.getCorpProperty().equals(other.getCorpProperty()))
                && (this.getRegCapital() == null ? other.getRegCapital() == null : this.getRegCapital().equals(other.getRegCapital()))
                && (this.getNational() == null ? other.getNational() == null : this.getNational().equals(other.getNational()))
                && (this.getInvestType() == null ? other.getInvestType() == null : this.getInvestType().equals(other.getInvestType()))
                && (this.getSzAccount() == null ? other.getSzAccount() == null : this.getSzAccount().equals(other.getSzAccount()))
                && (this.getShAccount() == null ? other.getShAccount() == null : this.getShAccount().equals(other.getShAccount()))
                && (this.getTradeDate() == null ? other.getTradeDate() == null : this.getTradeDate().equals(other.getTradeDate()))
                && (this.getTradeTime() == null ? other.getTradeTime() == null : this.getTradeTime().equals(other.getTradeTime()))
                && (this.getOperDate() == null ? other.getOperDate() == null : this.getOperDate().equals(other.getOperDate()))
                && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()))
                && (this.getVoucherNo() == null ? other.getVoucherNo() == null : this.getVoucherNo().equals(other.getVoucherNo()))
                && (this.getCipherText() == null ? other.getCipherText() == null : this.getCipherText().equals(other.getCipherText()))
                && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()))
                && (this.getOperNo() == null ? other.getOperNo() == null : this.getOperNo().equals(other.getOperNo()))
                && (this.getCheckerNo() == null ? other.getCheckerNo() == null : this.getCheckerNo().equals(other.getCheckerNo()))
                && (this.getLawName() == null ? other.getLawName() == null : this.getLawName().equals(other.getLawName()))
                && (this.getLawIdentType() == null ? other.getLawIdentType() == null : this.getLawIdentType().equals(other.getLawIdentType()))
                && (this.getLawIdentNo() == null ? other.getLawIdentNo() == null : this.getLawIdentNo().equals(other.getLawIdentNo()))
                && (this.getContIdentifyType() == null ? other.getContIdentifyType() == null : this.getContIdentifyType().equals(
                        other.getContIdentifyType()))
                && (this.getContName() == null ? other.getContName() == null : this.getContName().equals(other.getContName()))
                && (this.getContIdentType() == null ? other.getContIdentType() == null : this.getContIdentType().equals(other.getContIdentType()))
                && (this.getContIdentNo() == null ? other.getContIdentNo() == null : this.getContIdentNo().equals(other.getContIdentNo()))
                && (this.getContPhone() == null ? other.getContPhone() == null : this.getContPhone().equals(other.getContPhone()))
                && (this.getInstType() == null ? other.getInstType() == null : this.getInstType().equals(other.getInstType()))
                && (this.getPromotion() == null ? other.getPromotion() == null : this.getPromotion().equals(other.getPromotion()))
                && (this.getCustGroup() == null ? other.getCustGroup() == null : this.getCustGroup().equals(other.getCustGroup()))
                && (this.getBranchBank() == null ? other.getBranchBank() == null : this.getBranchBank().equals(other.getBranchBank()))
                && (this.getDeliverWay() == null ? other.getDeliverWay() == null : this.getDeliverWay().equals(other.getDeliverWay()))
                && (this.getDeliverType() == null ? other.getDeliverType() == null : this.getDeliverType().equals(other.getDeliverType()))
                && (this.getTradeMethod() == null ? other.getTradeMethod() == null : this.getTradeMethod().equals(other.getTradeMethod()))
                && (this.getMuiltAcco() == null ? other.getMuiltAcco() == null : this.getMuiltAcco().equals(other.getMuiltAcco()))
                && (this.getCanceled() == null ? other.getCanceled() == null : this.getCanceled().equals(other.getCanceled()))
                && (this.getOrderWay() == null ? other.getOrderWay() == null : this.getOrderWay().equals(other.getOrderWay()))
                && (this.getSignStatus() == null ? other.getSignStatus() == null : this.getSignStatus().equals(other.getSignStatus()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getCustClass() == null ? other.getCustClass() == null : this.getCustClass().equals(other.getCustClass()))
                && (this.getMinor() == null ? other.getMinor() == null : this.getMinor().equals(other.getMinor()))
                && (this.getMinorId() == null ? other.getMinorId() == null : this.getMinorId().equals(other.getMinorId()))
                && (this.getBroker() == null ? other.getBroker() == null : this.getBroker().equals(other.getBroker()))
                && (this.getCustManager() == null ? other.getCustManager() == null : this.getCustManager().equals(other.getCustManager()))
                && (this.getReferral() == null ? other.getReferral() == null : this.getReferral().equals(other.getReferral()))
                && (this.getReferralMobile() == null ? other.getReferralMobile() == null : this.getReferralMobile().equals(other.getReferralMobile()))
                && (this.getAcceptMode() == null ? other.getAcceptMode() == null : this.getAcceptMode().equals(other.getAcceptMode()))
                && (this.getContMobileNo() == null ? other.getContMobileNo() == null : this.getContMobileNo().equals(other.getContMobileNo()))
                && (this.getCertValidDate() == null ? other.getCertValidDate() == null : this.getCertValidDate().equals(other.getCertValidDate()))
                && (this.getContCertValidDate() == null ? other.getContCertValidDate() == null : this.getContCertValidDate().equals(
                        other.getContCertValidDate()))
                && (this.getLawCertValidDate() == null ? other.getLawCertValidDate() == null : this.getLawCertValidDate().equals(
                        other.getLawCertValidDate()))
                && (this.getCorpName() == null ? other.getCorpName() == null : this.getCorpName().equals(other.getCorpName()))
                && (this.getRiskNo() == null ? other.getRiskNo() == null : this.getRiskNo().equals(other.getRiskNo()))
                && (this.getSourceKey() == null ? other.getSourceKey() == null : this.getSourceKey().equals(other.getSourceKey()))
                && (this.getOperWay() == null ? other.getOperWay() == null : this.getOperWay().equals(other.getOperWay()))
                && (this.getMoneyAccount() == null ? other.getMoneyAccount() == null : this.getMoneyAccount().equals(other.getMoneyAccount()))
                && (this.getConfirmDate() == null ? other.getConfirmDate() == null : this.getConfirmDate().equals(other.getConfirmDate()))
                && (this.getErrCode() == null ? other.getErrCode() == null : this.getErrCode().equals(other.getErrCode()))
                && (this.getErrDetail() == null ? other.getErrDetail() == null : this.getErrDetail().equals(other.getErrDetail()))
                && (this.getConfirmCause() == null ? other.getConfirmCause() == null : this.getConfirmCause().equals(other.getConfirmCause()))
                && (this.getConfirmSerialno() == null ? other.getConfirmSerialno() == null : this.getConfirmSerialno().equals(
                        other.getConfirmSerialno()))
                && (this.getTagetTradeAccount() == null ? other.getTagetTradeAccount() == null : this.getTagetTradeAccount().equals(
                        other.getTagetTradeAccount()))
                && (this.getAccountCardNo() == null ? other.getAccountCardNo() == null : this.getAccountCardNo().equals(other.getAccountCardNo()))
                && (this.getSpecification() == null ? other.getSpecification() == null : this.getSpecification().equals(other.getSpecification()))
                && (this.getFrozenEndLine() == null ? other.getFrozenEndLine() == null : this.getFrozenEndLine().equals(other.getFrozenEndLine()))
                && (this.getFrozenCause() == null ? other.getFrozenCause() == null : this.getFrozenCause().equals(other.getFrozenCause()))
                && (this.getOriginConfirmNo() == null ? other.getOriginConfirmNo() == null : this.getOriginConfirmNo().equals(
                        other.getOriginConfirmNo()))
                && (this.getOriginRequestNo() == null ? other.getOriginRequestNo() == null : this.getOriginRequestNo().equals(
                        other.getOriginRequestNo()))
                && (this.getOriginConfirmDate() == null ? other.getOriginConfirmDate() == null : this.getOriginConfirmDate().equals(
                        other.getOriginConfirmDate()))
                && (this.getChecker() == null ? other.getChecker() == null : this.getChecker().equals(other.getChecker()))
                && (this.getSaleCustNo() == null ? other.getSaleCustNo() == null : this.getSaleCustNo().equals(other.getSaleCustNo()))
                && (this.getSaleRequestNo() == null ? other.getSaleRequestNo() == null : this.getSaleRequestNo().equals(other.getSaleRequestNo()))
                && (this.getContactorSerial() == null ? other.getContactorSerial() == null : this.getContactorSerial().equals(
                        other.getContactorSerial()))
                && (this.getSalePayCenterNo() == null ? other.getSalePayCenterNo() == null : this.getSalePayCenterNo().equals(
                        other.getSalePayCenterNo()))
                && (this.getSaleNetNo() == null ? other.getSaleNetNo() == null : this.getSaleNetNo().equals(other.getSaleNetNo()))
                && (this.getSaleTradeAccount() == null ? other.getSaleTradeAccount() == null : this.getSaleTradeAccount().equals(
                        other.getSaleTradeAccount()))
                && (this.getSaleMoneyAccount() == null ? other.getSaleMoneyAccount() == null : this.getSaleMoneyAccount().equals(
                        other.getSaleMoneyAccount()))
                && (this.getDealFlag() == null ? other.getDealFlag() == null : this.getDealFlag().equals(other.getDealFlag()))
        && (this.getBusiLicence() == null ? other.getBusiLicence() == null : this.getBusiLicence().equals(other.getBusiLicence()));
        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getTano() == null) ? 0 : getTano().hashCode());
        result = prime * result + ((getTradeAccount() == null) ? 0 : getTradeAccount().hashCode());
        result = prime * result + ((getFundAccount() == null) ? 0 : getFundAccount().hashCode());
        result = prime * result + ((getAgencyNo() == null) ? 0 : getAgencyNo().hashCode());
        result = prime * result + ((getPayCenterNo() == null) ? 0 : getPayCenterNo().hashCode());
        result = prime * result + ((getNetNo() == null) ? 0 : getNetNo().hashCode());
        result = prime * result + ((getBusinFlag() == null) ? 0 : getBusinFlag().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getIdentType() == null) ? 0 : getIdentType().hashCode());
        result = prime * result + ((getIdentNo() == null) ? 0 : getIdentNo().hashCode());
        result = prime * result + ((getZipCode() == null) ? 0 : getZipCode().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getHomePhone() == null) ? 0 : getHomePhone().hashCode());
        result = prime * result + ((getOfficePhone() == null) ? 0 : getOfficePhone().hashCode());
        result = prime * result + ((getFaxNo() == null) ? 0 : getFaxNo().hashCode());
        result = prime * result + ((getMobileNo() == null) ? 0 : getMobileNo().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getWeiXin() == null) ? 0 : getWeiXin().hashCode());
        result = prime * result + ((getWeibo() == null) ? 0 : getWeibo().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getCustSex() == null) ? 0 : getCustSex().hashCode());
        result = prime * result + ((getCityNo() == null) ? 0 : getCityNo().hashCode());
        result = prime * result + ((getCounty() == null) ? 0 : getCounty().hashCode());
        result = prime * result + ((getBankNo() == null) ? 0 : getBankNo().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankAcountName() == null) ? 0 : getBankAcountName().hashCode());
        result = prime * result + ((getPreStatus() == null) ? 0 : getPreStatus().hashCode());
        result = prime * result + ((getTradeStatus() == null) ? 0 : getTradeStatus().hashCode());
        result = prime * result + ((getLastTradeStatus() == null) ? 0 : getLastTradeStatus().hashCode());
        result = prime * result + ((getBirthDay() == null) ? 0 : getBirthDay().hashCode());
        result = prime * result + ((getVocation() == null) ? 0 : getVocation().hashCode());
        result = prime * result + ((getEducation() == null) ? 0 : getEducation().hashCode());
        result = prime * result + ((getIncoming() == null) ? 0 : getIncoming().hashCode());
        result = prime * result + ((getCorpVocate() == null) ? 0 : getCorpVocate().hashCode());
        result = prime * result + ((getCorpProperty() == null) ? 0 : getCorpProperty().hashCode());
        result = prime * result + ((getRegCapital() == null) ? 0 : getRegCapital().hashCode());
        result = prime * result + ((getNational() == null) ? 0 : getNational().hashCode());
        result = prime * result + ((getInvestType() == null) ? 0 : getInvestType().hashCode());
        result = prime * result + ((getSzAccount() == null) ? 0 : getSzAccount().hashCode());
        result = prime * result + ((getShAccount() == null) ? 0 : getShAccount().hashCode());
        result = prime * result + ((getTradeDate() == null) ? 0 : getTradeDate().hashCode());
        result = prime * result + ((getTradeTime() == null) ? 0 : getTradeTime().hashCode());
        result = prime * result + ((getOperDate() == null) ? 0 : getOperDate().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getVoucherNo() == null) ? 0 : getVoucherNo().hashCode());
        result = prime * result + ((getCipherText() == null) ? 0 : getCipherText().hashCode());
        result = prime * result + ((getIpaddr() == null) ? 0 : getIpaddr().hashCode());
        result = prime * result + ((getOperNo() == null) ? 0 : getOperNo().hashCode());
        result = prime * result + ((getCheckerNo() == null) ? 0 : getCheckerNo().hashCode());
        result = prime * result + ((getLawName() == null) ? 0 : getLawName().hashCode());
        result = prime * result + ((getLawIdentType() == null) ? 0 : getLawIdentType().hashCode());
        result = prime * result + ((getLawIdentNo() == null) ? 0 : getLawIdentNo().hashCode());
        result = prime * result + ((getContIdentifyType() == null) ? 0 : getContIdentifyType().hashCode());
        result = prime * result + ((getContName() == null) ? 0 : getContName().hashCode());
        result = prime * result + ((getContIdentType() == null) ? 0 : getContIdentType().hashCode());
        result = prime * result + ((getContIdentNo() == null) ? 0 : getContIdentNo().hashCode());
        result = prime * result + ((getContPhone() == null) ? 0 : getContPhone().hashCode());
        result = prime * result + ((getInstType() == null) ? 0 : getInstType().hashCode());
        result = prime * result + ((getPromotion() == null) ? 0 : getPromotion().hashCode());
        result = prime * result + ((getCustGroup() == null) ? 0 : getCustGroup().hashCode());
        result = prime * result + ((getBranchBank() == null) ? 0 : getBranchBank().hashCode());
        result = prime * result + ((getDeliverWay() == null) ? 0 : getDeliverWay().hashCode());
        result = prime * result + ((getDeliverType() == null) ? 0 : getDeliverType().hashCode());
        result = prime * result + ((getTradeMethod() == null) ? 0 : getTradeMethod().hashCode());
        result = prime * result + ((getMuiltAcco() == null) ? 0 : getMuiltAcco().hashCode());
        result = prime * result + ((getCanceled() == null) ? 0 : getCanceled().hashCode());
        result = prime * result + ((getOrderWay() == null) ? 0 : getOrderWay().hashCode());
        result = prime * result + ((getSignStatus() == null) ? 0 : getSignStatus().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getCustClass() == null) ? 0 : getCustClass().hashCode());
        result = prime * result + ((getMinor() == null) ? 0 : getMinor().hashCode());
        result = prime * result + ((getMinorId() == null) ? 0 : getMinorId().hashCode());
        result = prime * result + ((getBroker() == null) ? 0 : getBroker().hashCode());
        result = prime * result + ((getCustManager() == null) ? 0 : getCustManager().hashCode());
        result = prime * result + ((getReferral() == null) ? 0 : getReferral().hashCode());
        result = prime * result + ((getReferralMobile() == null) ? 0 : getReferralMobile().hashCode());
        result = prime * result + ((getAcceptMode() == null) ? 0 : getAcceptMode().hashCode());
        result = prime * result + ((getContMobileNo() == null) ? 0 : getContMobileNo().hashCode());
        result = prime * result + ((getCertValidDate() == null) ? 0 : getCertValidDate().hashCode());
        result = prime * result + ((getContCertValidDate() == null) ? 0 : getContCertValidDate().hashCode());
        result = prime * result + ((getLawCertValidDate() == null) ? 0 : getLawCertValidDate().hashCode());
        result = prime * result + ((getCorpName() == null) ? 0 : getCorpName().hashCode());
        result = prime * result + ((getRiskNo() == null) ? 0 : getRiskNo().hashCode());
        result = prime * result + ((getSourceKey() == null) ? 0 : getSourceKey().hashCode());
        result = prime * result + ((getOperWay() == null) ? 0 : getOperWay().hashCode());
        result = prime * result + ((getMoneyAccount() == null) ? 0 : getMoneyAccount().hashCode());
        result = prime * result + ((getConfirmDate() == null) ? 0 : getConfirmDate().hashCode());
        result = prime * result + ((getErrCode() == null) ? 0 : getErrCode().hashCode());
        result = prime * result + ((getErrDetail() == null) ? 0 : getErrDetail().hashCode());
        result = prime * result + ((getConfirmCause() == null) ? 0 : getConfirmCause().hashCode());
        result = prime * result + ((getConfirmSerialno() == null) ? 0 : getConfirmSerialno().hashCode());
        result = prime * result + ((getTagetTradeAccount() == null) ? 0 : getTagetTradeAccount().hashCode());
        result = prime * result + ((getAccountCardNo() == null) ? 0 : getAccountCardNo().hashCode());
        result = prime * result + ((getSpecification() == null) ? 0 : getSpecification().hashCode());
        result = prime * result + ((getFrozenEndLine() == null) ? 0 : getFrozenEndLine().hashCode());
        result = prime * result + ((getFrozenCause() == null) ? 0 : getFrozenCause().hashCode());
        result = prime * result + ((getOriginConfirmNo() == null) ? 0 : getOriginConfirmNo().hashCode());
        result = prime * result + ((getOriginRequestNo() == null) ? 0 : getOriginRequestNo().hashCode());
        result = prime * result + ((getOriginConfirmDate() == null) ? 0 : getOriginConfirmDate().hashCode());
        result = prime * result + ((getChecker() == null) ? 0 : getChecker().hashCode());
        result = prime * result + ((getSaleCustNo() == null) ? 0 : getSaleCustNo().hashCode());
        result = prime * result + ((getSaleRequestNo() == null) ? 0 : getSaleRequestNo().hashCode());
        result = prime * result + ((getContactorSerial() == null) ? 0 : getContactorSerial().hashCode());
        result = prime * result + ((getSalePayCenterNo() == null) ? 0 : getSalePayCenterNo().hashCode());
        result = prime * result + ((getSaleNetNo() == null) ? 0 : getSaleNetNo().hashCode());
        result = prime * result + ((getSaleTradeAccount() == null) ? 0 : getSaleTradeAccount().hashCode());
        result = prime * result + ((getDealFlag() == null) ? 0 : getDealFlag().hashCode());
        result = prime * result + ((getSaleMoneyAccount() == null) ? 0 : getSaleMoneyAccount().hashCode());
        result = prime * result + ((getBusiLicence() == null) ? 0 : getBusiLicence().hashCode());

        return result;
    }

    public void setFundCode(String anStr) {

    }

    public String getFundCode() {

        return ParamNames.IGNORE_FUNDCODE;
    }
}