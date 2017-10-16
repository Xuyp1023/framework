/*
 * Created on Sep 8, 2004
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
package nl.fountain.xelem.excel.ss;

import java.lang.reflect.Method;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import nl.fountain.xelem.GIO;
import nl.fountain.xelem.XLUtil;
import nl.fountain.xelem.excel.AbstractXLElement;
import nl.fountain.xelem.excel.Cell;
import nl.fountain.xelem.excel.Comment;

/**
 * An implementation of the XLElement Cell.
 * 
 * <P>
 * <h3>The setData-methods</h3>
 * The overloaded setData-methods will set the data displayed in the cell
 * when the xml spreadsheet is opened. These methods will set the Excel
 * datatype according to the java-type of the passed parameter. 
 * <P>
 * The {@link #setData(Object)}-method reflects
 * upon the class of the given object and will delegate to a corresponding
 * setData-method if such a method is available. If no corresponding
 * method is found this method sets the data of this cell to the
 * <code>toString</code>-value of the given object and the datatype to
 * "String".
 * This class may be extended to accommodate setData-methods for java-objects 
 * that might otherwise
 * be displayed as the <code>toString</code>-value of that object.
 * 
 * <P id="nullvalues">
 * <b>Null values</b><br>
 * If the passed parameter has a value of <code>null</code> the resulting
 * xml will have a datatype set to "Error",
 * the formula of the cell will be set to "<code>=#N/A</code>" and the cell will 
 * display "<code>#N/A</code>" when the spreadsheet is opened.
 * 
 * <P id="infinitevalues">
 * <b>Infinite values</b><br>
 * If the passed parameter is of type Double, Float or the primitive representation 
 * of these objects and the method {@link java.lang.Double#isInfinite() isInfinite}
 * results to <code>true</code> the resulting xml will have a datatype set to
 * "String" and the cell will display "Infinite" when the spreadsheet is opened.
 * 
 * <P id="nanvalues">
 * <b>NaN values</b><br>
 * If the passed parameter is of type Double, Float or the primitive representation 
 * of these objects and the method {@link java.lang.Double#isNaN() isNaN}
 * results to <code>true</code> the resulting xml will have a datatype set to
 * "String" and the cell will display "NaN" when the spreadsheet is opened.
 * 
 * @see nl.fountain.xelem.excel.Worksheet#addCell()
 * @see nl.fountain.xelem.excel.Row#addCell()
 */
public class SSCell extends AbstractXLElement implements Cell {

    private int idx;
    private boolean hasdata;
    private String styleID;
    private String formula;
    private String href;
    private String data$;
    private String datatype;
    private int mergeacross;
    private int mergedown;
    private Comment comment;

    /**
     * Creates a new SSCell with an initial datatype of "String" and an
     * empty ("") value.
     * 
     * @see nl.fountain.xelem.excel.Worksheet#addCell()
     */
    public SSCell() {
        datatype = DATATYPE_STRING;
        data$ = "";
    }

    /**
     * Sets the ss:StyleID on this cell. If no styleID is set on a cell,
     * the ss:StyleID-attribute is not deployed in the resulting xml and
     * the Default-style will be employed on the cell.
     * <P>
     * The refered style-definition must be available from the 
     * {@link nl.fountain.xelem.XFactory}.
     * If the definition was not found,
     * the {@link XLWorkbook}-implementation will create an empty ss:Style-definition
     * and adds a UnsupportedStyleException-warning.
     * 
     * @param 	id	the id of the style to employ on this cell.
     * 
     * @see XLWorkbook#getWarnings()
     */
    @Override
    public void setStyleID(String id) {
        styleID = id;
    }

    @Override
    public String getStyleID() {
        return styleID;
    }

    @Override
    public void setFormula(String formula) {
        // this.formula = XLUtil.escapeHTML(formula);
        this.formula = formula;
    }

    @Override
    public String getFormula() {
        return formula;
    }

    @Override
    public void setHRef(String href) {
        this.href = href;
    }

    @Override
    public String getHRef() {
        return href;
    }

    @Override
    public Comment addComment() {
        comment = new SSComment();
        return comment;
    }

    @Override
    public Comment addComment(Comment comment) {
        this.comment = comment;
        return comment;
    }

    @Override
    public Comment addComment(String text) {
        comment = new SSComment();
        comment.setData(text);
        return comment;
    }

    @Override
    public boolean hasComment() {
        return comment != null;
    }

    @Override
    public Comment getComment() {
        return comment;
    }

    @Override
    public void setMergeAcross(int m) {
        mergeacross = m;
    }

    private void setMergeAcross(String s) {
        mergeacross = Integer.parseInt(s);
    }

    @Override
    public int getMergeAcross() {
        return mergeacross;
    }

    @Override
    public void setMergeDown(int m) {
        mergedown = m;
    }

    private void setMergeDown(String s) {
        mergedown = Integer.parseInt(s);
    }

    @Override
    public int getMergeDown() {
        return mergedown;
    }

    @Override
    public String getXLDataType() {
        return datatype;
    }

    @Override
    public void setData(Number data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        setData(data.doubleValue());
    }

    @Override
    public void setData(Integer data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        datatype = DATATYPE_NUMBER;
        setData$(data.toString());
    }

    @Override
    public void setData(Double data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        if (data.isInfinite() || data.isNaN()) {
            datatype = DATATYPE_STRING;
        } else {
            datatype = DATATYPE_NUMBER;
        }
        setData$(data.toString());
    }

    @Override
    public void setData(Long data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        datatype = DATATYPE_NUMBER;
        setData$(data.toString());
    }

    @Override
    public void setData(Float data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        if (data.isInfinite() || data.isNaN()) {
            datatype = DATATYPE_STRING;
        } else {
            datatype = DATATYPE_NUMBER;
        }
        setData$(data.toString());
    }

    @Override
    public void setData(Date data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        datatype = DATATYPE_DATE_TIME;
        setData$(XLUtil.format(data));
    }

    @Override
    public void setData(Boolean data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        setData(data.booleanValue());
    }

    @Override
    public void setData(String data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        datatype = DATATYPE_STRING;
        setData$(data);
    }

    @Override
    public void setData(Object data) {
        if (data == null) {
            setError(ERRORVALUE_NA);
            return;
        }
        Class[] types = new Class[] { data.getClass() };
        Method method = null;
        try {
            method = this.getClass().getMethod("setData", types);
            method.invoke(this, new Object[] { data });
        }
        catch (NoSuchMethodException e) {
            setData(data.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setError(String error_value) {
        datatype = DATATYPE_ERROR;
        setData$(error_value);
        setFormula("=" + error_value);
    }

    @Override
    public void setData(byte data) {
        datatype = DATATYPE_NUMBER;
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(short data) {
        datatype = DATATYPE_NUMBER;
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(int data) {
        datatype = DATATYPE_NUMBER;
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(long data) {
        datatype = DATATYPE_NUMBER;
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(float data) {
        if (Float.isInfinite(data) || Float.isNaN(data)) {
            datatype = DATATYPE_STRING;
        } else {
            datatype = DATATYPE_NUMBER;
        }
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(double data) {
        if (Double.isInfinite(data) || Double.isNaN(data)) {
            datatype = DATATYPE_STRING;
        } else {
            datatype = DATATYPE_NUMBER;
        }
        setData$(String.valueOf(data));
    }

    @Override
    public void setData(char data) {
        setData(String.valueOf(data));
    }

    @Override
    public void setData(boolean data) {
        datatype = DATATYPE_BOOLEAN;
        if (data) {
            setData$("1");
        } else {
            setData$("0");
        }
    }

    private void setData$(String s) {
        data$ = s;
        hasdata = true;
        // System.out.println(data$);
    }

    @Override
    public boolean hasData() {
        return hasdata;
    }

    @Override
    public boolean hasError() {
        return DATATYPE_ERROR.equals(getXLDataType());
    }

    @Override
    public String getData$() {
        return data$;
    }

    @Override
    public Object getData() {
        if (DATATYPE_NUMBER.equals(datatype)) {
            return new Double(data$);
        } else if (DATATYPE_DATE_TIME.equals(datatype)) {
            return XLUtil.parse(data$);
        } else if (DATATYPE_BOOLEAN.equals(datatype)) {
            return new Boolean("1".equals(data$));
        }
        return data$;
    }

    @Override
    public int intValue() {
        try {
            return new Double(data$).intValue();
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public double doubleValue() {
        try {
            return new Double(data$).doubleValue();
        }
        catch (NumberFormatException e) {
            return 0.0D;
        }
    }

    @Override
    public boolean booleanValue() {
        return "1".equals(data$);
    }

    @Override
    public String getTagName() {
        return "Cell";
    }

    @Override
    public String getNameSpace() {
        return XMLNS_SS;
    }

    @Override
    public String getPrefix() {
        return PREFIX_SS;
    }

    @Override
    public Element assemble(Element parent, GIO gio) {
        Document doc = parent.getOwnerDocument();
        Element ce = assemble(doc, gio);

        if (idx != 0) ce.setAttributeNodeNS(createAttributeNS(doc, "Index", idx));
        if (getStyleID() != null) {
            ce.setAttributeNodeNS(createAttributeNS(doc, "StyleID", getStyleID()));
            gio.addStyleID(getStyleID());
        }
        if (formula != null) ce.setAttributeNodeNS(createAttributeNS(doc, "Formula", formula));
        if (href != null) ce.setAttributeNodeNS(createAttributeNS(doc, "HRef", href));
        if (mergeacross > 0) ce.setAttributeNodeNS(createAttributeNS(doc, "MergeAcross", mergeacross));
        if (mergedown > 0) ce.setAttributeNodeNS(createAttributeNS(doc, "MergeDown", mergedown));

        parent.appendChild(ce);

        if (!"".equals(getData$())) {
            Element data = getDataElement(doc);
            ce.appendChild(data);
        }

        if (comment != null) {
            comment.assemble(ce, gio);
        }

        return ce;
    }

    @Override
    public void setAttributes(Attributes attrs) {
        for (int i = 0; i < attrs.getLength(); i++) {
            invokeMethod(attrs.getLocalName(i), attrs.getValue(i));
        }
    }

    @Override
    public void setChildElement(String localName, String content) {
        if ("Data".equals(localName)) {
            setData$(content);
        }
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
    public Element getDataElement(Document doc) {
        Element data = doc.createElement("Data");
        data.setAttributeNodeNS(createAttributeNS(doc, "Type", getXLDataType()));
        data.appendChild(doc.createTextNode(getData$()));
        return data;
    }

    /**
     * @deprecated	as of xelem.2.0 use {@link #setType(String)}
     * @param type	one of Cell's DATATYPE_XXX values
     */
    @Deprecated
    protected void setXLDataType(String type) {
        setType(type);
    }

    /**
     * Sets the value of the ss:Type-attribute of the Data-element.
     * 
     * @param type	Must be one of Cell's DATATYPE_XXX values.
     */
    protected void setType(String type) {
        if (DATATYPE_BOOLEAN.equals(type) || DATATYPE_DATE_TIME.equals(type) || DATATYPE_ERROR.equals(type)
                || DATATYPE_NUMBER.equals(type) || DATATYPE_STRING.equals(type)) {
            datatype = type;
        } else {
            throw new IllegalArgumentException(type + " is not a valid datatype.");
        }
    }

    @Override
    public void setIndex(int index) {
        idx = index;
    }

    @Override
    public int getIndex() {
        return idx;
    }

}
