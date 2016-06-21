package com.betterjr.common.selectkey;

import com.betterjr.common.utils.BetterDateUtils;

public class SelectKeyTimeGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public Object getValue(String anKey) {

        return BetterDateUtils.getDate("HHmmss");
    }
}
