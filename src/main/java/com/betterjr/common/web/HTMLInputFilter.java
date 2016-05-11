package com.betterjr.common.web;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLInputFilter {
	/* member class not found */
	class Test {
	}

	public HTMLInputFilter() {
		this(false);
	}

	public HTMLInputFilter(boolean debug) {
		vDebug = debug;
		vAllowed = new HashMap();
		vTagCounts = new HashMap();
		ArrayList a_atts = new ArrayList();
		a_atts.add("href");
		a_atts.add("target");
		vAllowed.put("a", a_atts);
		ArrayList img_atts = new ArrayList();
		img_atts.add("src");
		img_atts.add("width");
		img_atts.add("height");
		img_atts.add("alt");
		vAllowed.put("img", img_atts);
		ArrayList no_atts = new ArrayList();
		vAllowed.put("b", no_atts);
		vAllowed.put("strong", no_atts);
		vAllowed.put("i", no_atts);
		vAllowed.put("em", no_atts);
	}

	protected void reset() {
		vTagCounts = new HashMap();
	}

	protected void debug(String msg) {
		if (vDebug)
			System.out.println(msg);
	}

	public static String chr(int decimal) {
		return String.valueOf((char) decimal);
	}

	public static String htmlSpecialChars(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("'", "& #39;");
		s = s.replaceAll("eval\\((.*)\\)", "");
		s = s.replaceAll("[\\\"\\'][\\s]*(?u)javascript:(.*)[\\\"\\']", "\"\"");
		s = s.replaceAll("script", "");
		return s;
	}

	public synchronized String filter(String input) {
		reset();
		if (input != null && !"".equals(input)) {
			String s = input;
			debug("************************************************");
			debug((new StringBuilder()).append("              INPUT: ")
					.append(input).toString());
			s = htmlSpecialChars(s);
			debug((new StringBuilder()).append("     htmlSpecialChars: ")
					.append(s).toString());
			s = escapeComments(s);
			debug((new StringBuilder()).append("     escapeComments: ")
					.append(s).toString());
			s = balanceHTML(s);
			debug((new StringBuilder()).append("        balanceHTML: ")
					.append(s).toString());
			s = checkTags(s);
			debug((new StringBuilder()).append("          checkTags: ")
					.append(s).toString());
			s = processRemoveBlanks(s);
			debug((new StringBuilder()).append("processRemoveBlanks: ")
					.append(s).toString());
			s = validateEntities(s);
			debug((new StringBuilder()).append("    validateEntites: ")
					.append(s).toString());
			debug("************************************************\n\n");
			return s;
		} else {
			return input;
		}
	}

	protected String escapeComments(String s) {
		Pattern p = Pattern.compile("<!--(.*?)-->", 32);
		Matcher m = p.matcher(s);
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String match = m.group(1);
			m.appendReplacement(buf, (new StringBuilder()).append("<!--")
					.append(htmlSpecialChars(match)).append("-->").toString());
		}
		m.appendTail(buf);
		return buf.toString();
	}

	protected String balanceHTML(String s) {
		s = regexReplace("^>", "", s);
		s = regexReplace("<([^>]*?)(?=<|$)", "<$1>", s);
		s = regexReplace("(^|>)([^<]*?)(?=>)", "$1<$2", s);
		return s;
	}

	protected String checkTags(String s) {
		Pattern p = Pattern.compile("<(.*?)>", 32);
		Matcher m = p.matcher(s);
		StringBuffer buf = new StringBuffer();
		String replaceStr;
		for (; m.find(); m.appendReplacement(buf, replaceStr)) {
			replaceStr = m.group(1);
			replaceStr = processTag(replaceStr);
		}

		m.appendTail(buf);
		s = buf.toString();
		for (Iterator i$ = vTagCounts.keySet().iterator(); i$.hasNext();) {
			String key = (String) i$.next();
			int ii = 0;
			while (ii < ((Integer) vTagCounts.get(key)).intValue()) {
				s = (new StringBuilder()).append(s).append("</").append(key)
						.append(">").toString();
				ii++;
			}
		}

		return s;
	}

	protected String processRemoveBlanks(String s) {
		String arr$[] = vRemoveBlanks;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			String tag = arr$[i$];
			s = regexReplace((new StringBuilder()).append("<").append(tag)
					.append("(\\s[^>]*)?></").append(tag).append(">")
					.toString(), "", s);
			s = regexReplace((new StringBuilder()).append("<").append(tag)
					.append("(\\s[^>]*)?/>").toString(), "", s);
		}

		return s;
	}

	protected String regexReplace(String regex_pattern, String replacement,
			String s) {
		Pattern p = Pattern.compile(regex_pattern);
		Matcher m = p.matcher(s);
		return m.replaceAll(replacement);
	}

	protected String processTag(String s) {
		Pattern p = Pattern.compile("^/([a-z0-9]+)", 34);
		Matcher m = p.matcher(s);
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			if (vAllowed.containsKey(name) && !inArray(name, vSelfClosingTags)
					&& vTagCounts.containsKey(name)) {
				vTagCounts.put(name, Integer.valueOf(((Integer) vTagCounts
						.get(name)).intValue() - 1));
				return (new StringBuilder()).append("</").append(name)
						.append(">").toString();
			}
		}
		p = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", 34);
		m = p.matcher(s);
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			String body = m.group(2);
			String ending = m.group(3);
			if (vAllowed.containsKey(name)) {
				String params = "";
				Pattern p2 = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2", 34);
				Pattern p3 = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)", 34);
				Matcher m2 = p2.matcher(body);
				Matcher m3 = p3.matcher(body);
				List paramNames = new ArrayList();
				List paramValues = new ArrayList();
				for (; m2.find(); paramValues.add(m2.group(3)))
					paramNames.add(m2.group(1));

				for (; m3.find(); paramValues.add(m3.group(3)))
					paramNames.add(m3.group(1));

				for (int ii = 0; ii < paramNames.size(); ii++) {
					String paramName = ((String) paramNames.get(ii))
							.toLowerCase();
					String paramValue = (String) paramValues.get(ii);
					if (!((List) vAllowed.get(name)).contains(paramName))
						continue;
					if (inArray(paramName, vProtocolAtts))
						paramValue = processParamProtocol(paramValue);
					params = (new StringBuilder()).append(params).append(" ")
							.append(paramName).append("=\"").append(paramValue)
							.append("\"").toString();
				}

				if (inArray(name, vSelfClosingTags))
					ending = " /";
				if (inArray(name, vNeedClosingTags))
					ending = "";
				if (ending == null || ending.length() < 1) {
					if (vTagCounts.containsKey(name))
						vTagCounts.put(name, Integer
								.valueOf(((Integer) vTagCounts.get(name))
										.intValue() + 1));
					else
						vTagCounts.put(name, Integer.valueOf(1));
				} else {
					ending = " /";
				}
				return (new StringBuilder()).append("<").append(name)
						.append(params).append(ending).append(">").toString();
			} else {
				return "";
			}
		}
		p = Pattern.compile("^!--(.*)--$", 34);
		m = p.matcher(s);
		if (m.find()) {
			String comment = m.group();
			return "";
		} else {
			return "";
		}
	}

	protected String processParamProtocol(String s) {
		s = decodeEntities(s);
		Pattern p = Pattern.compile("^([^:]+):", 34);
		Matcher m = p.matcher(s);
		if (m.find()) {
			String protocol = m.group(1);
			if (!inArray(protocol, vAllowedProtocols)) {
				s = (new StringBuilder()).append("#")
						.append(s.substring(protocol.length() + 1, s.length()))
						.toString();
				if (s.startsWith("#//"))
					s = (new StringBuilder()).append("#")
							.append(s.substring(3, s.length())).toString();
			}
		}
		return s;
	}

	protected String decodeEntities(String s) {
		StringBuffer buf = new StringBuffer();
		Pattern p = Pattern.compile("&#(\\d+);?");
		Matcher m;
		int decimal;
		for (m = p.matcher(s); m.find(); m.appendReplacement(buf, chr(decimal))) {
			String match = m.group(1);
			decimal = Integer.decode(match).intValue();
		}

		m.appendTail(buf);
		s = buf.toString();
		buf = new StringBuffer();
		p = Pattern.compile("&#x([0-9a-f]+);?");
		for (m = p.matcher(s); m.find(); m.appendReplacement(buf, chr(decimal))) {
			String match = m.group(1);
			decimal = Integer.decode(match).intValue();
		}

		m.appendTail(buf);
		s = buf.toString();
		buf = new StringBuffer();
		p = Pattern.compile("%([0-9a-f]{2});?");
		for (m = p.matcher(s); m.find(); m.appendReplacement(buf, chr(decimal))) {
			String match = m.group(1);
			decimal = Integer.decode(match).intValue();
		}

		m.appendTail(buf);
		s = buf.toString();
		s = validateEntities(s);
		return s;
	}

	protected String validateEntities(String s) {
		Pattern p = Pattern.compile("&([^&;]*)(?=(;|&|$))");
		Matcher m = p.matcher(s);
		if (m.find()) {
			String one = m.group(1);
			String two = m.group(2);
			s = checkEntity(one, two);
		}
		p = Pattern.compile("(>|^)([^<]+?)(<|$)", 32);
		m = p.matcher(s);
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String one = m.group(1);
			String two = m.group(2);
			String three = m.group(3);
			m.appendReplacement(
					buf,
					(new StringBuilder()).append(one)
							.append(two.replaceAll("\"", "&quot;"))
							.append(three).toString());
		}
		m.appendTail(buf);
		return s;
	}

	protected String checkEntity(String preamble, String term) {
		if (!term.equals(";"))
			return (new StringBuilder()).append("&amp;").append(preamble)
					.toString();
		if (isValidEntity(preamble))
			return (new StringBuilder()).append("&").append(preamble)
					.toString();
		else
			return (new StringBuilder()).append("&amp;").append(preamble)
					.toString();
	}

	protected boolean isValidEntity(String entity) {
		return inArray(entity, vAllowedEntities);
	}

	private boolean inArray(String s, String array[]) {
		String arr$[] = array;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			String item = arr$[i$];
			if (item != null && item.equals(s))
				return true;
		}

		return false;
	}

	protected static final boolean ALWAYS_MAKE_TAGS = true;
	protected static final boolean STRIP_COMMENTS = true;
	protected static final int REGEX_FLAGS_SI = 34;
	protected Map vAllowed;
	protected Map vTagCounts;
	protected String vSelfClosingTags[] = { "img" };
	protected String vNeedClosingTags[] = { "a", "b", "strong", "i", "em" };
	protected String vProtocolAtts[] = { "src", "href" };
	protected String vAllowedProtocols[] = { "http", "mailto" };
	protected String vRemoveBlanks[] = { "a", "b", "strong", "i", "em" };
	protected String vAllowedEntities[] = { "amp", "gt", "lt", "quot" };
	protected boolean vDebug;
}
