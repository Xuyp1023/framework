package com.betterjr.modules.sys.service;

import java.util.List;
import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.DictInfoMapper;
import com.betterjr.modules.sys.entity.DictInfo;
import com.betterjr.modules.sys.entity.DictInfoTest;

@Service
public class DictService extends BaseService<DictInfoMapper, DictInfo> {

    protected void preInsert(DictInfo anEntity) {
        // anEntity.setId(888888);
    }

    public List<DictInfo> findOutPutScript() {

        return this.selectByProperty("outScipt", new String[] { "1", "3" });
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
