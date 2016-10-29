package com.betterjr.modules.cert.dubboclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.cert.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Service
public class CustCertDubboClientService {
    private static final Logger logger = LoggerFactory.getLogger(CustCertDubboClientService.class);

    @Reference(interfaceClass = ICustCertService.class)
    private ICustCertService custCertService;

    public CustCertInfo checkValidity(final X509Certificate anX509) {
        return custCertService.checkValidity(anX509);
    }

    public CustCertInfo findFirstCertInfoByOperOrg(final String anOperOrg) {
        return custCertService.findFirstCertInfoByOperOrg(anOperOrg);
    }

    public String findCustCertificate(final String anSerialNo) {
        return custCertService.webFindCustCertificate(anSerialNo);
    }

    public String queryCustCertificate(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        return custCertService.webQueryCustCertificate(anParam, anFlag, anPageNum, anPageSize);
    }

    public String addCustCertificate(final Map<String, Object> anParam) {
        return custCertService.webAddCustCertificate(anParam);
    }

    public String modifyCustCertificate(final String anSerialNo, final Map<String, Object> anParam) {
        return custCertService.webSaveCustCertificate(anSerialNo, anParam);
    }

    public String modifyWechatCustCertificate(final String anSerialNo, final String anOrginSerialNo, final Map<String, Object> anParam) {
        return custCertService.webSaveCustCertificate(anSerialNo, anOrginSerialNo, anParam);
    }

    public String publishCustCertificate(final String anSerialNo, final String anPublishMode) {
        return custCertService.webPublishCustCertificate(anSerialNo, anPublishMode);
    }

    public String cancelCustCertificate(final String anSerialNo) {
        return custCertService.webCancelCustCertificate(anSerialNo);
    }

    public String revokeCustCertificate(final String anSerialNo, final String anReason) {
        return custCertService.webRevokeCustCertificate(anSerialNo, anReason);
    }

    public void downloadCertificate(final String anToken, final HttpServletResponse anResponse) {
        final byte[] data = custCertService.webDownloadCertificate(anToken);
        if (data.length != 0) {
            fileDownloadWithFileName(anResponse, data, "cert.p12");
        }
    }

    public void fileDownloadWithFileName(final HttpServletResponse anResponse, final  byte[] anData, final String anFileName) {
        String msg = null;
        OutputStream os = null;
        try (InputStream is = new ByteArrayInputStream(anData)){
            final StringBuilder sb = new StringBuilder(100);
            sb.append("attachment").append("; ").append("filename=").append(java.net.URLEncoder.encode(anFileName, "UTF-8"));
            os = anResponse.getOutputStream();
            anResponse.setHeader("Content-Disposition", sb.toString());
            anResponse.setContentType("application/x-pkcs12");
            IOUtils.copy(is, os);
            return;
        }
        catch (final IOException e) {
            logger.error("下载文件失败，请检查；" + anFileName, e);
            msg = "出现IO异常，请稍后再试!";
        }
        finally {
            if (msg != null) {
                anResponse.reset();
                anResponse.setContentType("text/html;UTF-8");
                try {
                    anResponse.getWriter().append(msg);
                }
                catch (final IOException e) {
                    logger.error("关闭文件流失败；" + anFileName, e);
                }
            }
            IOUtils.closeQuietly(os);
        }
    }

}
