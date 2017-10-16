package com.betterjr.common.selectkey;

import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.service.SelectKeyGenService;

public class SelectKeyAutoIDGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Autowired
    private SelectKeyGenIDService selectKeyGenIDService = null;

    /*
    public SekectKeyGenIDService getAutoIDService() {
        return selectKeyGenIDService;
    }
    
    public void setAutoIDService(SekectKeyGenIDService anAutoIDService) {
    	selectKeyGenIDService = anAutoIDService;
    }*/
    public void init() {
        SelectKeyGenService.regKeyGen(this);
    }

    @Override
    public String getName() {
        return "id";
    }

    @Override
    public Object getValue(String anKey) {

        return selectKeyGenIDService.getLongValue(anKey);
    }

}
