/*
 * Created on 8-nov-2004
 * Copyright 2013 Henk van den Berg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * see license.txt
 *
 */
package nl.fountain.xelem.excel.o;

import java.lang.reflect.Method;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nl.fountain.xelem.GIO;
import nl.fountain.xelem.XLUtil;
import nl.fountain.xelem.excel.AbstractXLElement;
import nl.fountain.xelem.excel.DocumentProperties;

/**
 * An implementation of the XLElement DocumentProperties.
 */
public class ODocumentProperties extends AbstractXLElement implements DocumentProperties {

    private String title;
    private String subject;
    private String keywords;
    private String description;
    private String category;
    private String author;
    private String lastAuthor;
    private String manager;
    private String created;
    private String lastsaved;
    private String lastprinted;
    private String company;
    private String hyperlinkbase;
    private String appname;
    private String version;

    /**
     * Creates a new ODocumentProperties.
     * 
     * @see nl.fountain.xelem.excel.Workbook#getDocumentProperties()
     */
    public ODocumentProperties() {}

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    @Override
    public void setManager(String manager) {
        this.manager = manager;
    }

    @Override
    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public void setHyperlinkBase(String hyperlinkbase) {
        this.hyperlinkbase = hyperlinkbase;
    }

    @Override
    public void setAppName(String appname) {
        this.appname = appname;
    }

    @Override
    public void setCreated(Date created) {
        this.created = XLUtil.format(created).substring(0, 16) + "Z";
    }

    /**
     * Method called by 
     * {@link nl.fountain.xelem.lex.ExcelReader}.
     * 
     * @param created the node value of the tag <code>&lt;Created&gt;</code>
     */
    private void setCreated(String created) {
        this.created = created;
    }

    @Override
    public void setLastSaved(Date lastsaved) {
        this.lastsaved = XLUtil.format(lastsaved).substring(0, 16) + "Z";
    }

    /**
     * Method called by 
     * {@link nl.fountain.xelem.lex.ExcelReader}.
     * 
     * @param lastsaved the node value of the tag <code>&lt;LastSaved&gt;</code>
     */
    private void setLastSaved(String lastsaved) {
        this.lastsaved = lastsaved;
    }

    /**
     * Method called by 
     * {@link nl.fountain.xelem.lex.ExcelReader}.
     * 
     * @param lastprinted the node value of the tag <code>&lt;LastPrinted&gt;</code>
     */
    private void setLastPrinted(String lastprinted) {
        this.lastprinted = lastprinted;
    }

    @Override
    public String getTagName() {
        return "DocumentProperties";
    }

    @Override
    public String getNameSpace() {
        return XMLNS_O;
    }

    @Override
    public String getPrefix() {
        return PREFIX_O;
    }

    @Override
    public Element assemble(Element parent, GIO gio) {
        Document doc = parent.getOwnerDocument();
        Element dpe = assemble(doc, gio);

        if (title != null) dpe.appendChild(createElementNS(doc, "Title", title));
        if (subject != null) dpe.appendChild(createElementNS(doc, "Subject", subject));
        if (keywords != null) dpe.appendChild(createElementNS(doc, "Keywords", keywords));
        if (description != null) dpe.appendChild(createElementNS(doc, "Description", description));
        if (category != null) dpe.appendChild(createElementNS(doc, "Category", category));
        if (author != null) dpe.appendChild(createElementNS(doc, "Author", author));
        if (lastAuthor != null) dpe.appendChild(createElementNS(doc, "LastAuthor", lastAuthor));
        if (manager != null) dpe.appendChild(createElementNS(doc, "Manager", manager));
        if (company != null) dpe.appendChild(createElementNS(doc, "Company", company));
        if (hyperlinkbase != null) dpe.appendChild(createElementNS(doc, "HyperlinkBase", hyperlinkbase));
        if (appname != null) dpe.appendChild(createElementNS(doc, "AppName", appname));
        if (created != null) dpe.appendChild(createElementNS(doc, "Created", created));
        if (lastsaved != null) dpe.appendChild(createElementNS(doc, "LastSaved", lastsaved));

        parent.appendChild(dpe);
        return dpe;
    }

    @Override
    public void setChildElement(String localName, String content) {
        invokeMethod(localName, content);
    }

    private void invokeMethod(String name, Object value) {
        Class[] types = new Class[] { value.getClass() };
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod("set" + name, types);
            method.invoke(this, new Object[] { value });
        }
        catch (NoSuchMethodException e) {
            // no big deal
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getKeywords() {
        return keywords;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getLastAuthor() {
        return lastAuthor;
    }

    @Override
    public String getManager() {
        return manager;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public String getHyperlinkBase() {
        return hyperlinkbase;
    }

    @Override
    public String getAppName() {
        return appname;
    }

    @Override
    public Date getCreated() {
        Date date = null;
        if (created != null) {
            date = XLUtil.parse(created);
        }
        return date;
    }

    @Override
    public Date getLastSaved() {
        Date date = null;
        if (lastsaved != null) {
            date = XLUtil.parse(lastsaved);
        }
        return date;
    }

    @Override
    public Date getLastPrinted() {
        Date date = null;
        if (lastprinted != null) {
            date = XLUtil.parse(lastprinted);
        }
        return date;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getVersion() {
        return version;
    }

}
