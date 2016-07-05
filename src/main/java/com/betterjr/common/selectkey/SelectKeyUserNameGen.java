package com.betterjr.common.selectkey;

 import com.betterjr.common.utils.UserUtils;

public class SelectKeyUserNameGen implements ISelectKeyGenFace {
    private static final long serialVersionUID = -3037670584051795787L;

    @Override
    public String getName() {
        return "username";
    }

    @Override
    public Object getValue(String anKey) {

        return UserUtils.getUser().getName();
 }

}
