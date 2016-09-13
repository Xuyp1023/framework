package com.betterjr.modules.wechat.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import com.betterjr.common.exception.BettjerIOException;

import org.apache.http.conn.ssl.TrustStrategy;
import javax.net.ssl.HostnameVerifier;

import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
/**
 * 链接处理工具类
 * @author zhoucy
 *
 */
public class RemoteConnectionFactory {

    private volatile BasicCookieStore cookieStore;

    private PoolingHttpClientConnectionManager poolConnManager;

    private static RequestConfig requestConfig = null;

    private boolean useCookies = false;

    private Charset enConding;

    private Charset ackEncoding;
    private boolean noneName;
    private String contentType = null;

    public String getContentType() {
        return contentType;
    }

    public Charset getEnConding() {
        return enConding;
    }

    public Charset getAckEncoding() {
        return ackEncoding;
    }

    public CloseableHttpClient getConnection() {
        CloseableHttpClient httpClient = null;
        if (this.useCookies) {
            httpClient = HttpClients.custom().setConnectionManager(poolConnManager).setDefaultCookieStore(cookieStore)
                    .setDefaultRequestConfig(requestConfig).build();
        }
        else {
            httpClient = HttpClients.custom().setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();

        }

        return httpClient;
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {

            return true;
        }
    }

    public static RemoteConnectionFactory createInstance(String anEnCoding, String anAckEncoding) {

        return new RemoteConnectionFactory(anEnCoding, anAckEncoding);
    }

    private RemoteConnectionFactory() {

    }

    private RemoteConnectionFactory(String anEnCoding, String ackEncoding) {
        try {
            enConding = Charset.forName(anEnCoding);
            if (StringUtils.isNotBlank(ackEncoding)) {
                this.ackEncoding = Charset.forName(ackEncoding);
            }
            else {
                this.ackEncoding = enConding;
            }

            // 连接超时时间，默认60秒
            int timeOut = 300;
            timeOut = timeOut * 1000;
            requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut).build();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            // 开阶段，信任所有的服务器，实施后，需要修改服务证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
            TrustAnyHostnameVerifier hostnameVerifier = new TrustAnyHostnameVerifier();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();

            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // max total connection to 200
            int maxTotalPool = 200;
            poolConnManager.setMaxTotal(maxTotalPool);

            // default max connection per route to 20
            int maxConPerRoute = 20;
            poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);
            poolConnManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(enConding).build());
            poolConnManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeOut).build());
            this.useCookies = false;
            this.noneName = false;
            this.contentType = "application/x-www-form-urlencoded";
        }
        catch (KeyManagementException e) {
            throw new RuntimeException("Create KeyManagementException  ", e);
        }
        catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Create NoSuchAlgorithmException  ", e);
        }
        catch (KeyStoreException e) {

            throw new RuntimeException("Create Trust KeyStoreException", e);
        }
    }

    public boolean isNoneName() {
        return noneName;
    }

    public List<NameValuePair> paramsConverter(Map<String, Object> params) {
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Entry<String, Object>> paramsSet = params.entrySet();
        for (Entry<String, Object> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue().toString()));
        }
        return nvps;
    }

    public StringBuilder readStream(InputStream in) {

        return readStream(in, false);
    }

    public StringBuilder readStream(InputStream in, boolean anMock) {
        StringBuilder sb = new StringBuilder(4096);
        if (anMock) {
            try {
                in = new FileInputStream("testFile.txt");
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (in == null) {
            return sb;
        }

        InputStreamReader inReader = null;
        try {
            inReader = new InputStreamReader(in, this.ackEncoding);
            char[] buffer = new char[4096];
            int readLen = 0;
            while ((readLen = inReader.read(buffer)) != -1) {
                sb.append(buffer, 0, readLen);
            }

            return sb;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw BettjerIOException.wrap(e);
        }
        finally {
            IOUtils.closeQuietly(inReader);
        }
    }

    public InputStream doGet(String url) throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponse response = this.doGet(url, null);

        return response != null ? response.getEntity().getContent() : null;
    }

    public StringBuilder doGetForString(String url) throws URISyntaxException, ClientProtocolException, IOException {

        return readStream(this.doGet(url));
    }

    public InputStream doGetForStream(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponse response = this.doGet(url, queryParams);

        return response != null ? response.getEntity().getContent() : null;
    }

    public StringBuilder doGetForString(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {

        return readStream(this.doGetForStream(url, queryParams));
    }

    /**
     * 基本的Get请求
     * 
     * @param url
     *            请求url
     * @param queryParams
     *            请求头的查询参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public CloseableHttpResponse doGet(String url, Map<String, Object> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
        HttpGet gm = new HttpGet();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        gm.setURI(builder.build());

        return getConnection().execute(gm);
    }

    /**
     * 基本的Post请求
     * 
     * @param anUrl
     *            请求url
     * @param anBody
     *            请求头的查询参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public StringBuilder doPost(String anUrl, String anBody) throws URISyntaxException, ClientProtocolException, IOException {
        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(anUrl);
        pm.setURI(builder.build());
        StringEntity params = new StringEntity(anBody, "UTF-8");
        pm.setEntity(params);
        CloseableHttpResponse response = getConnection().execute(pm);
        return readStream(response.getEntity().getContent());
    }

    public HttpResponse doPost(String url, Map<String, Object> queryParams, Map<String, Object> formParams)
            throws URISyntaxException, ClientProtocolException, IOException {
        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        pm.setURI(builder.build());
        // 填入表单参数
        if (formParams != null && !formParams.isEmpty()) {
            pm.setEntity(new UrlEncodedFormEntity(paramsConverter(formParams), enConding));
        }

        return getConnection().execute(pm);
    }

    /**
     * 多块Post请求
     * 
     * @param url
     *            请求url
     * @param queryParams
     *            请求头的查询参数
     * @param formParts
     *            post表单的参数,支持字符串-文件(FilePart)和字符串-字符串(StringPart)形式的参数
     * @param maxCount
     *            最多尝试请求的次数
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws HttpException
     * @throws IOException
     */
    public StringBuilder multipartPost(String anUrl, List<FormBodyPart> anFormParts) throws URISyntaxException, ClientProtocolException, IOException {
        CloseableHttpResponse response = null;
        try {
            response = multipartPost(anUrl, null, anFormParts);
            StringBuilder sb = readStream(response.getEntity().getContent());
            return sb;
        }
        finally {
            if (response != null){
                response.close();
            }
        }

    }

    public CloseableHttpResponse multipartPost(String url, Map<String, Object> queryParams, List<FormBodyPart> formParts)
            throws URISyntaxException, ClientProtocolException, IOException {

        HttpPost pm = new HttpPost();
        URIBuilder builder = new URIBuilder(url);
        // 填入查询参数
        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        pm.setURI(builder.build());

        // 填入表单参数
        if (formParts != null && formParts.size() > 0) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (FormBodyPart formPart : formParts) {
                entityBuilder = entityBuilder.addPart(formPart.getName(), formPart.getBody());
            }
            pm.setEntity(entityBuilder.build());
        }
        return getConnection().execute(pm);
    }

    /**
     * 批量设置Cookie
     * 
     * @param cookies
     *            cookie键值对图
     * @param domain
     *            作用域 不可为空
     * @param path
     *            路径 传null默认为"/"
     * @param useSecure
     *            是否使用安全机制 传null 默认为false
     * @return 是否成功设置cookie
     */
    public boolean setCookie(Map<String, String> cookies, String domain, String path, Boolean useSecure) {
        synchronized (cookieStore) {
            if (domain == null) {
                return false;
            }
            if (path == null) {
                path = "/";
            }
            if (useSecure == null) {
                useSecure = false;
            }
            if (cookies == null || cookies.isEmpty()) {
                return true;
            }
            Set<Entry<String, String>> set = cookies.entrySet();
            String key = null;
            String value = null;
            for (Entry<String, String> entry : set) {
                key = entry.getKey();
                if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                    throw new IllegalArgumentException("cookies key and value both can not be empty");
                }
                BasicClientCookie cookie = new BasicClientCookie(key, value);
                cookie.setDomain(domain);
                cookie.setPath(path);
                cookie.setSecure(useSecure);
                cookieStore.addCookie(cookie);
            }
            return true;
        }
    }

    /**
     * 设置单个Cookie
     * 
     * @param key
     *            Cookie键
     * @param value
     *            Cookie值
     * @param domain
     *            作用域 不可为空
     * @param path
     *            路径 传null默认为"/"
     * @param useSecure
     *            是否使用安全机制 传null 默认为false
     * @return 是否成功设置cookie
     */
    public boolean setCookie(String key, String value, String domain, String path, Boolean useSecure) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put(key, value);
        return setCookie(cookies, domain, path, useSecure);
    }

    class AnyTrustStrategy implements TrustStrategy {

        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }
}
