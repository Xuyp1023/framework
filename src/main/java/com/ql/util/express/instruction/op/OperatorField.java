package com.ql.util.express.instruction.op;

import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;

public class OperatorField extends OperatorBase {

    public OperatorField() {
        this.name = "FieldCall";
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, OperateData[] list) throws Exception {
        Object obj = list[0].getObject(parent);
        String fieldName = list[1].getObject(parent).toString();
        return OperateDataCacheManager.fetchOperateDataField(obj, fieldName);
    }
}