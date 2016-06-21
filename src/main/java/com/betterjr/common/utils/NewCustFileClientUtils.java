package com.betterjr.common.utils;

import com.betterjr.common.selectkey.SerialGenerator;

public class NewCustFileClientUtils {

    public static Long findBatchNo(){
        
        return SerialGenerator.getLongValue("CustFileInfo.id");
    }
}
