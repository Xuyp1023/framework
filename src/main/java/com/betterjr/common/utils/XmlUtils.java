package com.betterjr.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 将XML的结构转换为 MAP结构
 * 
 * @author henry
 *
 */
public class XmlUtils {
    public static String XML_SALT_FLAG = "X@*!X";

    public static String REVOSE_MAP = "REV#$%OSE_M@#$AP";

    public static String DATA_NODE = "req_dataNode";
    public static String HEAD_NODE = "req_headNode";
    public static String ROOT_NODE = "req_rootNode";

    public static Map<String, Object> dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null) return map;
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
            Element e = (Element) iterator.next();
            System.out.println(e.getName());
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), dom2Map(e));
            } else {
                map.put(e.getName(), e.getText());
            }
        }

        return map;
    }

    /**
     * 获取根据节点层级列表，获取XML节点
     * 
     * @param 传入的根节点
     * @param 节点层级信息
     * @return 找到的节点
     */
    public static Element findElement(Element root, List<String> sk) {
        String workNodeName = sk.remove(0);
        List emList = root.elements();
        Element element = null;
        for (int i = 0, k = emList.size(); i < k; i++) {
            element = (Element) emList.get(i);
            if (element.getNodeType() == Node.ELEMENT_NODE) {
                if (workNodeName.equalsIgnoreCase(element.getName())) {
                    if (sk.size() == 0) {
                        return element;
                    } else {
                        return findElement(element, sk);
                    }
                }
            }
        }
        return null;
    };

    public static List<String> split(String anStr) {

        return split(anStr, ";");
    }

    public static List<String> split(String anStr, String anTag) {
        List<String> list = new LinkedList();
        if (StringUtils.isBlank(anStr)) {

            return list;
        }

        StringTokenizer tokenizer = new StringTokenizer(anStr, anTag);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }

        return list;
    }

    /**
     * 
     * 获取字符串的最后一个分隔串信息
     * 
     * @param 字符串信息
     * @return 最后一个字符串
     * @throws 异常情况
     */
    public static String strLastValue(String anStr) {
        if (StringUtils.isNotBlank(anStr)) {
            List<String> data = split(anStr);

            return data.get(data.size() - 1);
        }
        return anStr;
    }

    /**
     * 
     * 根据路径创建一个Map的树形结构
     * 
     * @param 树形结构的字符串，使用‘；’分隔
     * @param1 已经存在的Map
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public static Map<String, Object> createTreeMap(String anNodeStr, Map<String, Object> anMap) {

        return createTreeMap(anNodeStr, anMap, false);
    }

    public static Map<String, Object> createTreeMap(String anNodeStr, Map<String, Object> anMap, boolean hasReturn) {
        if (anNodeStr == null) {

            return anMap;
        }
        Map<String, Object> map = null;
        if (anMap == null) {
            map = new LinkedHashMap();
            anMap = map;
        } else {
            map = anMap;
        }
        List<String> list = split(anNodeStr);
        Object obj;
        Map<String, Object> tmpMap;
        Collection tmpList = null;
        for (String tmpKey : list) {
            obj = map.get(tmpKey);

            // 节点不存在，直接创建节点
            if (obj == null) {
                tmpMap = new LinkedHashMap();
                map.put(tmpKey, tmpMap);
            }
            // 节点是一个Map,直接使用
            else if (obj instanceof Map) {
                tmpMap = (Map) obj;
            }
            // 节点是一个集合，直接使用集合的第一个Map节点，如果没有Map节点，则创建一个Map节点
            else if (obj instanceof Collection) {
                tmpList = (Collection) obj;
                tmpMap = null;
                for (Object subObj : (Collection) obj) {
                    if (subObj instanceof Map) {
                        tmpMap = (Map) subObj;
                        break;
                    }
                }
                if (tmpMap == null) {
                    tmpMap = new LinkedHashMap();
                    tmpList.add(tmpMap);
                }
            }
            // 其他对象，则创建一个列表，将这个对象放在列表中，并创建一个集合
            else {
                tmpMap = new LinkedHashMap();
                tmpList = new LinkedList();
                tmpList.add(tmpMap);
                tmpList.add(obj);
                map.put(tmpKey, tmpList);
            }
            map = tmpMap;
        }
        if (hasReturn) {
            anMap.put(REVOSE_MAP, map);
        }
        return anMap;
    }

    public static Element findElement(Element root, String workNodeName) {
        List<String> list = split(workNodeName);
        if (list.size() == 1) {
            return findElementNode(root, workNodeName);
        } else {
            return findElement(root, list);
        }
    }

    public static Element findElementNode(Element root, String workNodeName) {
        if (workNodeName.equalsIgnoreCase(root.getName())) {
            return root;
        }
        List emList = root.elements();
        Element element = null;
        for (int i = 0, k = emList.size(); i < k; i++) {
            element = (Element) emList.get(i);
            if (element.getNodeType() == Node.ELEMENT_NODE) {
                if (workNodeName.equalsIgnoreCase(element.getName())) {
                    return element;
                } else {
                    element = findElementNode(element, workNodeName);
                    if (element != null) {
                        return element;
                    }
                }
            }
        }
        return null;
    };

    private static void processText(Element iter, Map<String, Object> anMap) {
        Object obj;
        String tmpStr = iter.getText();
        if (StringUtils.isNotBlank(tmpStr)) {
            obj = anMap.get(iter.getName());
            Collection ccList;
            if (obj instanceof Collection) {
                ccList = (Collection) obj;
                ccList.add(tmpStr);
            } else if (obj instanceof Map) {
                ccList = new ArrayList();
                ccList.add(obj);
                ccList.add(tmpStr);
                anMap.put(iter.getName(), ccList);
            }
        }
    }

    public static Map dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            Object obj;
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                obj = map.get(iter.getName());
                if (iter.elements().size() > 0) {
                    Map m = dom2Map(iter);
                    if (obj != null) {
                        if (obj instanceof List) {
                            mapList = (List) obj;
                        } else {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            map.put(iter.getName(), mapList);
                        }
                        mapList.add(m);
                    } else {
                        map.put(iter.getName(), m);
                    }
                    processText(iter, map);/*
                                            * String tmpStr = iter.getText(); if
                                            * (StringUtils.isNotBlank(tmpStr)) { obj =
                                            * map.get(iter.getName()); Collection ccList;
                                            * if (obj instanceof Collection) { ccList =
                                            * (Collection) obj; ccList.add(tmpStr); } else
                                            * if (obj instanceof Map) { ccList = new
                                            * ArrayList(); ccList.add(obj);
                                            * ccList.add(tmpStr); map.put(iter.getName(),
                                            * ccList); } }
                                            */
                } else {
                    if (obj != null) {
                        if (obj instanceof List) {
                            mapList = (List) obj;
                        } else {
                            mapList = new ArrayList();
                            mapList.add(obj);
                        }
                        mapList.add(XML_SALT_FLAG + iter.getText());
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }

            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static Document W3c2Dom4j(org.w3c.dom.Document doc) throws Exception {
        if (doc == null) {
            return (null);
        }
        org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
        return (xmlReader.read(doc));
    }

    public static org.w3c.dom.Document dom4j2W3c(Document doc) {
        if (doc == null) {
            return (null);
        }
        java.io.StringReader reader = new java.io.StringReader(doc.asXML());
        org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
        javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory
                .newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document ddx = documentBuilder.parse(source);
            // System.out.println("this pase :"+ddx);
            return ddx;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
