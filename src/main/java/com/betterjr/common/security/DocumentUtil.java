package com.betterjr.common.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.soap.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.betterjr.common.exception.BettjerNestedException;

public class DocumentUtil {

    private final static String[] ids = new String[] { "id", "Id", "ID" };

    public DocumentUtil() {
    }

    public static Document getDocFromInputStream(InputStream is) {
        try {
            return getDocBuilder().parse(is);
        }
        catch (SAXException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39001, " SAXException", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39002, " IOException", e);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39003, " ParserConfigurationException", e);
        }
    }

    public static Document getDocFromFile(File f) {
        try {
            return getDocBuilder().parse(f);
        }
        catch (SAXException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39001, " SAXException", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39002, " IOException", e);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39003, " ParserConfigurationException", e);
        }
    }

    public static DocumentBuilder getDocBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder;
    }

    private static boolean setNodeAttribId(Element anParent) {
        NodeList nodes = anParent.getChildNodes();
        Element node = null;
        for (int i = 0, k = nodes.getLength(); i < k; i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                node = (Element) nodes.item(i);
                for (int j = 0; j < 3; j++) {
                    String tmpStr = node.getAttribute(ids[j]);
                    if (StringUtils.isNotBlank(tmpStr) && tmpStr.equalsIgnoreCase(node.getNodeName())) {
                        node.setIdAttributeNS(null, ids[j], true);
                        return true;
                    }
                }
                if (setNodeAttribId(node)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Document getDocFromString(String xml) {
        InputStream is;
        try {
            is = IOUtils.toInputStream(xml, "UTF-8");
            Document doc = getDocFromInputStream(is);
            setNodeAttribId(doc.getDocumentElement());
            return doc;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BettjerNestedException(39004, " IOException", e);
        }
    }
}
