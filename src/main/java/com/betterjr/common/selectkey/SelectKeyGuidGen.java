package com.betterjr.common.selectkey;

import com.betterjr.common.utils.UUIDUtils;

public class SelectKeyGuidGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "guid";
    }

    @Override
    public Object getValue(String anKey) {

        return "bt" + UUIDUtils.uuid();
    }

}
