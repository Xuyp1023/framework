package com.betterjr.modules.sys.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.sys.dubbo.interfaces.IDictToWebService;

@Service(interfaceClass = IDictToWebService.class)
public class DictToWebDubboService implements IDictToWebService {

    @Override
    public String initWebCreateDictJS() {
        // TODO Auto-generated method stub
        return DictUtils.createOutScript();
    }

}
