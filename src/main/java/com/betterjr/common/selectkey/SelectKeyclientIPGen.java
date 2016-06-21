package com.betterjr.common.selectkey;

import com.betterjr.common.web.Servlets;
 
public class SelectKeyclientIPGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "clientip";
    }

    @Override
    public Object getValue(String anKey) {

        return Servlets.getRemoteAddr();
    }

}
