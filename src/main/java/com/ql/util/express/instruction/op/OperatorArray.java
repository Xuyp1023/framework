package com.ql.util.express.instruction.op;

import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;

public class OperatorArray extends OperatorBase {
    public OperatorArray(String aName) {
        this.name = aName;
    }

    public OperatorArray(String aAliasName, String aName, String aErrorInfo) {
        this.name = aName;
        this.aliasName = aAliasName;
        this.errorInfo = aErrorInfo;
    }

    @Override
    public OperateData executeInner(InstructionSetContext context, OperateData[] list) throws Exception {
        if (list[0] == null || list[0].getObject(context) == null) {
            throw new Exception("对象为null,不能执行数组相关操作");
        }
        Object tmpObject = list[0].getObject(context);
        if (tmpObject.getClass().isArray() == false) {
            throw new Exception("对象:" + tmpObject.getClass() + "不是数组,不能执行相关操作");
        }
        int index = ((Number) list[1].getObject(context)).intValue();
        OperateData result = OperateDataCacheManager.fetchOperateDataArrayItem(list[0], index);
        return result;
    }
}
