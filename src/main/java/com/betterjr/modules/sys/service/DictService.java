package com.betterjr.modules.sys.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.sys.dao.DictInfoMapper;
import com.betterjr.modules.sys.entity.DictInfo;
import com.betterjr.modules.sys.entity.DictInfoTest;
import com.betterjr.modules.sys.entity.DictItemInfo;

@Service
public class DictService extends BaseService<DictInfoMapper, DictInfo> {

    @Autowired
    private DictItemService dictItemService;

    protected void preInsert(DictInfo anEntity) {
        // anEntity.setId(888888);
    }

    public List<DictInfo> findOutPutScript() {

        return this.selectByProperty("outScipt", new String[] { "1", "3" });
    }

    public List<DictItemInfo> findOutScriptByGroup(int anParentId) {

        return dictItemService.findOutScriptByGroup(anParentId);
    }

    public List<DictItemInfo> findByGroup(int anParentId) {

        return this.dictItemService.findByGroup(anParentId);
    }

    public DictInfo findByCode(String anCode) {
        List<DictInfo> data = this.selectByProperty("dictCode", anCode);
        if (data.size() == 1) {
            return Collections3.getFirst(data);
        }
        else {
            logger.warn("use dictCode is " + anCode + ", not find in define");
            return null;
        }
    }

    public DictItemInfo saveDictItem(String anType, String anKey, String anValue) {
        DictInfo dictInfo = findByCode(anType);
        if (dictInfo == null) {
            return null;
        }

        return this.dictItemService.saveDictItem(dictInfo.getId(), anKey, anValue);
    }

    public DictItemInfo saveDictItem(DictItemInfo anItemInfo) {
        dictItemService.updateByPrimaryKey(anItemInfo);

        return anItemInfo;
    }

    public List<DictInfo> findOutPutApp() {

        return this.selectByProperty("outScipt", new String[] { "2", "3" });
    }

    public List<DictInfo> findByGroup(String groupId) {
        return this.selectByProperty("groupNo", groupId);
    }

    public List<DictInfoTest> findByTest(String groupId) {
        Map<String, Object> map = new HashMap();
        map.put("groupNo", groupId);
        List list = selectByClassProperty(DictInfoTest.class, "groupNo", groupId);
        return list;
    }
}
