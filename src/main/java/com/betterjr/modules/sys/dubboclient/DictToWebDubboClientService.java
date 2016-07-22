package com.betterjr.modules.sys.dubboclient;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.sys.dubbo.interfaces.IDictToWebService;

@Service
public class DictToWebDubboClientService implements IDictToWebService {
    
    @Reference(interfaceClass=IDictToWebService.class)
    private IDictToWebService dubboService;

    @Override
    public String initWebCreateDictJS() {
        // TODO Auto-generated method stub
        return this.dubboService.initWebCreateDictJS();
    }

}
