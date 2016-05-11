package com.betterjr.common.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.betterjr.common.exception.ServiceException;
import com.betterjr.common.mapper.CustDecimalJsonSerializer;
import com.betterjr.common.utils.DictUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * FreemarkerService 服务引擎，负责根据提供的参数，产生需要的内容。
 * 
 * @author zhoucy
 *
 */

public class FreemarkerService extends Configuration {

    private final static Logger logger = LoggerFactory.getLogger(FreemarkerService.class);

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    public FreemarkerService() {
        this.setDefaultEncoding("UTF-8");
        this.setTemplateLoader(stringTemplateLoader);
    }

    public String processTemplate(String templateName, long version, String templateContents, Map<String, Object> dataMap) {
        Assert.notNull(templateName);
        Assert.notNull(version);
        if (StringUtils.isBlank(templateContents)) {
            return null;
        }
        Object templateSource = stringTemplateLoader.findTemplateSource(templateName);
        if (templateSource == null) {
            logger.debug("Init freemarker template: {}", templateName);
            stringTemplateLoader.putTemplate(templateName, templateContents, version);
        }
        else {
            long ver = stringTemplateLoader.getLastModified(templateSource);
            if (version > ver) {
                logger.debug("Update freemarker template: {}", templateName);
                stringTemplateLoader.putTemplate(templateName, templateContents, version);
            }
        }
        return processTemplateByName(templateName, dataMap);
    }

    private String processTemplateByName(String templateName, Map<String, Object> dataMap) {
        StringWriter strWriter = new StringWriter();
        try {
            this.getTemplate(templateName).process(dataMap, strWriter);
            strWriter.flush();
        }
        catch (TemplateException e) {
            throw new ServiceException("error.freemarker.template.process", e);
        }
        catch (IOException e) {
            throw new ServiceException("error.freemarker.template.process", e);
        }
        return strWriter.toString();
    }

    public String processTemplateByContents(String templateContents, Map<String, Object> dataMap) {
        String templateName = "_" + templateContents.hashCode();
        return processTemplate(templateName, 0, templateContents, dataMap);
    }
    
    @Deprecated
    public String processTemplateByFileName(String templateFileName, Map<String, Object> dataMap) {
        String moduleName="sale";
        return this.processTemplateByFileNameUnderModule(templateFileName, dataMap, moduleName);
    }

    public String processTemplateByFileNameUnderModule(String templateFileName, Map<String, Object> dataMap,String moduleName) {
        dataMap.put("numberFormater",  new CustDecimalJsonSerializer());
        dataMap.put("dictUtils",  new DictUtils());
        String subPath=File.separator + "templates" + File.separator + "modules"+File.separator+moduleName+File.separator;
        String templateDir = System.getProperty("java.io.tmpdir") +subPath;
        File targetTemplateFile = new File(templateDir + File.separator + templateFileName + ".ftl");
        if ((targetTemplateFile.exists() && targetTemplateFile.isFile() && targetTemplateFile.length() > 10) == false) {
            try {
                // 从classpath加载文件处理写入临时文件
                InputStream source = this.getClass().getResourceAsStream("/templates/modules/"+moduleName +"/" + templateFileName + ".ftl");
                FileUtils.copyInputStreamToFile(source, targetTemplateFile);
            }
            catch (IOException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
        logger.warn("Processing freemarker template file: {}", targetTemplateFile.getAbsolutePath());
        long fileVersion = targetTemplateFile.lastModified();
        Object templateSource = stringTemplateLoader.findTemplateSource(templateFileName);
        long templateVersion = 0;
        if (templateSource != null) {
            templateVersion = stringTemplateLoader.getLastModified(templateSource);
        }
        if (fileVersion > templateVersion) {
            try {
                String contents = FileUtils.readFileToString(targetTemplateFile);
                return processTemplate(templateFileName, fileVersion, contents, dataMap);
            }
            catch (IOException e) {
                throw new ServiceException("error.freemarker.template.process", e);
            }
        }
        else {
            return processTemplateByName(templateFileName, dataMap);
        }
    }
}
