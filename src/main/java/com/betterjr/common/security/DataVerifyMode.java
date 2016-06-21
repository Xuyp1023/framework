package com.betterjr.common.security;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.exception.BytterValidException;

/**
 * 数据签名验证的模式， <BR>
 * XML_VERIFY:标准的XML签名验证模式 <BR>
 * NORMAL_VERIFY:简单的文件摘要模式 <BR>
 * PUBKEY_VERIFY：私钥签名，使用公钥的验证模式 <BR>
 * DIGEST_VERIFY：对数据做摘要后的签名验证模式
 * 
 * @author zhoucy
 *
 */
public enum DataVerifyMode {
    XML_VERIFY, NORMAL_VERIFY, PUBKEY_VERIFY, DIGEST_VERIFY;
    public static DataVerifyMode checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return DataVerifyMode.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        throw new BytterValidException(1231231, "DataVerifyMode not find in declear :" + anWorkType);
    }
}
