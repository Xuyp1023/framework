package com.betterjr.common.selectkey;

public class SelectKeyGuidGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "guid";
    }

    @Override
    public Object getValue(String anKey) {

        return "bt" + SerialGenerator.uuid();
    }

}
