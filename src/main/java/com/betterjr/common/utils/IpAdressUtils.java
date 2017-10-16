package com.betterjr.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.betterjr.common.mapper.JsonMapper;

public class IpAdressUtils {
    private static String sinaURL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=xml&ip=";
    private static String taobaoURL = "http://ip.taobao.com/service/getIpInfo.php?ip=";
    private static String qqURL = "http://ip.qq.com/cgi-bin/searchip";
    private static String ip138URL = "http://www.ip138.com/ips138.asp?ip=";

    public static long ip2int(String ip) {
        String[] items = ip.split("\\.");
        System.out.println(items.length);
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8
                | Long.valueOf(items[3]);
    }

    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append((ipInt >> 24) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append(ipInt & 0xFF);
        return sb.toString();
    }

    public static String getAddressByIP(String ip) {
        try {
            URL url = new URL(ip138URL + ip);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            ip = result.substring(result.indexOf("主数据：") + 4, result.indexOf("</li><li>参考数据"));
            char[] ipStr = ip.toCharArray();
            System.out.println(ipStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String qqIpLookup(String ip) {
        String tmpStr = null;
        try {
            Document doc = Jsoup.connect(qqURL).cookie("ipqqcom_user_id", "955125")
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)").data("searchip1", ip)
                    .post();
            tmpStr = doc.data();
            // Object obj = JSON.parse(tmpStr);
            // System.out.println(obj);
        }
        catch (IOException ex) {}
        return tmpStr;
    }

    public static IP2LocationInfo taobaoIpLookup(String ip) {
        String tmpStr = null;
        try {
            tmpStr = Jsoup.connect(taobaoURL + ip)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)").execute().body();
            TaobaoIpLocation ipL = JsonMapper.getInstance().readValue(tmpStr, TaobaoIpLocation.class);
            if (ipL.getCode() == 0) {
                System.out.println(ipL.getData());
                return ipL.getData();
            }
        }
        catch (IOException ex) {}
        return null;
    }

    public static String sinaIpLookup(String ip) {
        String tmpStr = null;
        try {
            tmpStr = Jsoup.connect(sinaURL + ip)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)").execute().body();
            String arrStr[] = tmpStr.split("\t");
            for (String tmp : arrStr) {
                System.out.println(tmp);
            }
        }
        catch (IOException ex) {}
        return tmpStr;
    }

    public static boolean isIpAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        String[] fields = address.split("\\.");
        if (fields.length == 4) {
            // IPV4
            for (String field : fields) {
                try {
                    int value = Integer.parseInt(field);
                    if (value < 0 || value > 255) {
                        return false;
                    }
                }
                catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
        // TODO IPV6?
        return false;
    }

}
