package com.ql.util.express.instruction.op;

import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;

public class OperatorKeyValue extends OperatorBase {

    public OperatorKeyValue(String aName) {
        this.name = aName;
    }

    public OperatorKeyValue(String aAliasName, String aName, String aErrorInfo) {
        this.name = aName;
        this.aliasName = aAliasName;
        this.errorInfo = aErrorInfo;
    }

    @Override
    public OperateData executeInner(InstructionSetContext context, OperateData[] list) throws Exception {
        return OperateDataCacheManager.fetchOperateDataKeyValue(list[0], list[1]);
    }
}
