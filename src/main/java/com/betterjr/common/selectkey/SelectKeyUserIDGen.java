package com.betterjr.common.selectkey;

 import com.betterjr.common.utils.UserUtils;

public class SelectKeyUserIDGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "userid";
    }

    @Override
    public Object getValue(String anKey) {

        return UserUtils.getUser().getId();
    }

}
