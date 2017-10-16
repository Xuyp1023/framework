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

    /**
     * 处理FreeMarker的模板信息
     * @param templateName 模板的名字
     * @param version 版本号
     * @param templateContents 模板的内容
     * @param dataMap 模板处理的上下文数据
     * @return
     */
    public StringBuffer processTemplate(String templateName, long version, String templateContents,
            Map<String, Object> dataMap) {
        Assert.notNull(templateName);
        Assert.notNull(version);
        if (StringUtils.isBlank(templateContents)) {

            return new StringBuffer();
        }

        Object templateSource = stringTemplateLoader.findTemplateSource(templateName);
        if (templateSource == null) {
            logger.debug("Init freemarker template: {}", templateName);
            stringTemplateLoader.putTemplate(templateName, templateContents, version);
        } else {
            long ver = stringTemplateLoader.getLastModified(templateSource);
            if (version > ver) {
                logger.debug("Update freemarker template: {}", templateName);
                stringTemplateLoader.putTemplate(templateName, templateContents, version);
            }
        }
        return processTemplateByName(templateName, dataMap);
    }

    private StringBuffer processTemplateByName(String templateName, Map<String, Object> dataMap) {
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
        return strWriter.getBuffer();
    }

    public StringBuffer processTemplateByContents(String templateContents, Map<String, Object> dataMap) {
        String templateName = "_" + templateContents.hashCode();

        return processTemplate(templateName, 0, templateContents, dataMap);
    }

    @Deprecated
    public StringBuffer processTemplateByFileName(String templateFileName, Map<String, Object> dataMap) {
        String moduleName = "sale";
        return this.processTemplateByFileNameUnderModule(templateFileName, dataMap, moduleName);
    }

    /**
     * 处理前海拜特定义的模板信息
     * @param templateFileName 模板文件名称
     * @param dataMap 上下文数据
     * @param moduleName 模板所在的功能模块
     * @return
     */
    public StringBuffer processTemplateByFileNameUnderModule(String templateFileName, Map<String, Object> dataMap,
            String moduleName) {
        dataMap.put("numberFormater", new CustDecimalJsonSerializer());
        dataMap.put("dictUtils", new DictUtils());
        String subPath = File.separator + "templates" + File.separator + "modules" + File.separator + moduleName
                + File.separator;
        String templateDir = System.getProperty("java.io.tmpdir") + subPath;
        File targetTemplateFile = new File(templateDir + File.separator + templateFileName + ".ftl");
        if ((targetTemplateFile.exists() && targetTemplateFile.isFile() && targetTemplateFile.length() > 10) == false) {
            try {
                // 从classpath加载文件处理写入临时文件
                InputStream source = this.getClass()
                        .getResourceAsStream("/templates/modules/" + moduleName + "/" + templateFileName + ".ftl");
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
        } else {
            return processTemplateByName(templateFileName, dataMap);
        }
    }

    public StringBuffer processTemplateByFactory(String templateFileName, Map<String, Object> dataMap,
            String moduleName, InputStream anSource) {
        dataMap.put("numberFormater", new CustDecimalJsonSerializer());
        dataMap.put("dictUtils", new DictUtils());
        String subPath = File.separator + "templates" + File.separator + "modules" + File.separator + moduleName
                + File.separator;
        String templateDir = System.getProperty("java.io.tmpdir") + subPath;
        File targetTemplateFile = new File(templateDir + File.separator + templateFileName + ".ftl");
        try {
            FileUtils.copyInputStreamToFile(anSource, targetTemplateFile);
        }
        catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
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
        } else {
            return processTemplateByName(templateFileName, dataMap);
        }

    }
}
