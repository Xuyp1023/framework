package com.betterjr.modules.wechat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.selectkey.SerialGenerator;

/**
 * Http工具包，用于访问API，上传或下载微信素材
 * 
 * @author zhoucy
 */
public class HttpTool {

    private static final Logger log = LoggerFactory.getLogger(HttpTool.class);

    private static final int CONNECT_TIME_OUT = 30 * 1000;
    private static final String FILE_NAME_FLAG = "filename=";
    private static final RemoteConnectionFactory connFactory = RemoteConnectionFactory.createInstance("UTF-8", "UTF-8");

    public static String get(String anUrl) {
        if (log.isDebugEnabled()) {
            log.debug("Request url: %s", anUrl);
        }
        try {
            return connFactory.doGetForString(anUrl).toString();
        }
        catch (Exception e) {
            throw BytterException.unchecked(e);
        }
    }

    public static String post(String anUrl, String anBody) {
        if (log.isDebugEnabled()) {
            log.debug("Request url: %s, post data: %s, default timeout: %d", anUrl, anBody, CONNECT_TIME_OUT);
        }

        try {
            String content = connFactory.doPost(anUrl, anBody).toString();
            if (log.isInfoEnabled()) {
                log.info("POST Request success. Response content: %s", content);
            }
            return content;
        }
        catch (Exception e) {
            throw BytterException.unchecked(e);
        }
    }

    public static String upload(String anUrl, File anFile) {
        if (log.isDebugEnabled()) {
            log.debug("Upload url: %s, file name: %s, default timeout: %d", anUrl, anFile.getName(), CONNECT_TIME_OUT);
        }

        try {
            List<FormBodyPart> formParts = new ArrayList();
            formParts.add(FormBodyPartBuilder.create("media", new FileBody(anFile)).build());
            String content = connFactory.multipartPost(anUrl, formParts).toString();
            if (log.isInfoEnabled()) {
                log.info("upload Request success. Response content: %s", content);
            }
            return content;
        }
        catch (Exception e) {
            throw BytterException.unchecked(e);
        }
    }

    public static File download(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Download url: %s, default timeout: %d", url, CONNECT_TIME_OUT);
        }
        CloseableHttpResponse resp = null;
        try {
            resp = connFactory.doGet(url, null);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300){
                Header hh = resp.getFirstHeader("Content-disposition");
                if (log.isInfoEnabled()) {
                    log.info("Get download file info: %s", hh);
                }
                String cd = "";
                if (hh == null) {
                    cd = SerialGenerator.uuid();
                }
                else{
                    cd = hh.getValue();
                    cd = cd.substring(cd.indexOf(FILE_NAME_FLAG) + FILE_NAME_FLAG.length());
                }
                String tmp = cd.startsWith("\"") ? cd.substring(1) : cd;
                tmp = tmp.endsWith("\"") ? cd.replace("\"", "") : cd;
                String filename = tmp.substring(0, tmp.lastIndexOf("."));
                String fileext = tmp.substring(tmp.lastIndexOf("."));
                if (log.isInfoEnabled()) {
                    log.info("Download file name: %s", filename);
                    log.info("Download file ext: %s", fileext);
                }

                File tmpfile = File.createTempFile(filename, fileext);
                InputStream is = resp.getEntity().getContent();
                OutputStream os = new FileOutputStream(tmpfile);
                IOUtils.copy(is, os);
                return tmpfile;
            }
            String statusMessage = resp.getStatusLine().getReasonPhrase();
            throw new BytterValidException(String.format("Download file [%s] failed. status: %d, content: %s", url, statusCode, statusMessage));
        }
        catch (Exception e) {
            throw BytterException.unchecked(e);
        }
        finally{
            if (resp != null) {
                EntityUtils.consumeQuietly(resp.getEntity());
            }
        }
    }
    public static void main(String[] args){
       System.out.println(SerialGenerator.uuid()); 
    }
}
