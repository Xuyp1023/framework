/**
 * 
 */
package com.betterjr.common.data;

/**
 * @author hewei
 *
 */
public enum WebServiceErrorCode {
    E0000(0000,"业务受理成功"),
    E1000(1000,"PartnerCode 不存在"),
    E1001(1001,"token 验证失败"),
    E1002(1002,"签名验证失败"),
    E1003(1003,"调用的功能没有定义"),
    E1004(1004,"申请单信息不存在！"),
    E1005(1005,"返回结果中缺少必要的数据！"),
    E1006(1006,"客户信息不存在！"),
    E1007(1007,"解密失败！"),
    E1008(1008,"电子合同已经正在签署，不允许更改！"),
    E1010(1010,"电子合同缺少签约方，不能签约"),
    E9999(9999,"系统内部异常");
    
    private final int code;
    private final String msg;
    WebServiceErrorCode(int status, String msg){
        this.code=status;
        this.msg=msg;
    }
    
    public String getStrCode(){
        if (this.code==0){
            return "0000";
        }
       return Integer.toString(this.code); 
    }
    public int getCode() {
        
        return this.code;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public String toString(){
        return code+","+msg;
    }
}
