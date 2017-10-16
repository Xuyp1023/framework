package com.betterjr.common.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MimeTypesHelper {

    private static Map<String, String> mimeMap = new HashMap();
    static {
        InputStream in = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ClassPathResource cc = new ClassPathResource("mimeMapping.xml");
            in = cc.getInputStream();
            Document document = db.parse(in);
            Element root = document.getDocumentElement();
            NodeList nList = root.getElementsByTagName("mime-mapping");
            for (int i = 0, k = nList.getLength(); i < k; i++) {
                Node nn = nList.item(i);
                if (nn instanceof Element) {
                    Element mm = (Element) nn;
                    mimeMap.put(mm.getElementsByTagName("extension").item(0).getTextContent(),
                            mm.getElementsByTagName("mime-type").item(0).getTextContent());
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Load MimeMapping Has Error :mimeMapping.xml");
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static String getMimeType(String anFileType) {
        if (StringUtils.isBlank(anFileType)) {
            return "application/octet-stream";
        }
        return mimeMap.get(anFileType);
    }
}
