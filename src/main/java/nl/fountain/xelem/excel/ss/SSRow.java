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
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import nl.fountain.xelem.GIO;
import nl.fountain.xelem.excel.AbstractXLElement;
import nl.fountain.xelem.excel.Cell;
import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.Worksheet;

/**
 * An implementation of the XLElement Row.
 * 
 * 
 */
public class SSRow extends AbstractXLElement implements Row {

    TreeMap<Integer, Cell> cells;
    private int idx;
    private String styleID;
    private double height = -1.0;
    private int span;
    private boolean hidden;
    // private boolean autoFitHeight;

    /**
     * Constructs a new SSRow.
     * 
     * @see nl.fountain.xelem.excel.Worksheet#addRow()
     */
    public SSRow() {
        cells = new TreeMap<Integer, Cell>();
    }

    @Override
    public void setStyleID(String id) {
        styleID = id;
    }

    @Override
    public String getStyleID() {
        return styleID;
    }

    @Override
    public void setHeight(double h) {
        height = h;
    }

    private void setHeight(String s) {
        height = Double.parseDouble(s);
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSpan(int s) {
        span = s;
    }

    private void setSpan(String s) {
        span = Integer.parseInt(s);
    }

    @Override
    public int getSpan() {
        return span;
    }

    @Override
    public void setHidden(boolean hide) {
        hidden = hide;
    }

    private void setHidden(String s) {
        hidden = s.equals("1");
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    // public void setAutoFitHeight(boolean autoFit) {
    // autoFitHeight = autoFit;
    // }

    @Override
    public Cell addCell() {
        return addCellAt(maxCellIndex() + 1, new SSCell());
    }

    @Override
    public Cell addCell(Object data) {
        Cell cell = new SSCell();
        cell.setData(data);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCell(Object data, String styleID) {
        Cell cell = new SSCell();
        cell.setData(data);
        cell.setStyleID(styleID);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCell(double data) {
        Cell cell = new SSCell();
        cell.setData(data);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCell(double data, String styleID) {
        Cell cell = new SSCell();
        cell.setData(data);
        cell.setStyleID(styleID);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCell(int data) {
        Cell cell = new SSCell();
        cell.setData(data);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCell(int data, String styleID) {
        Cell cell = new SSCell();
        cell.setData(data);
        cell.setStyleID(styleID);
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCellAt(int index) {
        return addCellAt(index, new SSCell());
    }

    @Override
    public Cell addCell(Cell cell) {
        return addCellAt(maxCellIndex() + 1, cell);
    }

    @Override
    public Cell addCellAt(int index, Cell cell) {
        if (index < Worksheet.firstColumn || index > Worksheet.lastColumn) {
            throw new IndexOutOfBoundsException("columnIndex = " + index);
        }
        cells.put(new Integer(index), cell);
        return cell;
    }

    @Override
    public Cell removeCellAt(int index) {
        return cells.remove(new Integer(index));
    }

    @Override
    public Collection<Cell> getCells() {
        return cells.values();
    }

    @Override
    public Cell getCellAt(int index) {
        if (!hasCellAt(index)) {
            return addCellAt(index);
        } else {
            return cells.get(new Integer(index));
        }
    }

    @Override
    public boolean hasCellAt(int index) {
        return cells.get(new Integer(index)) != null;
    }

    @Override
    public int size() {
        return cells.size();
    }

    @Override
    public TreeMap<Integer, Cell> getCellMap() {
        return cells;
    }

    @Override
    public int maxCellIndex() {
        int lastKey;
        if (cells.size() == 0) {
            lastKey = 0;
        } else {
            lastKey = cells.lastKey().intValue();
        }
        return lastKey;
    }

    @Override
    public String getTagName() {
        return "Row";
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
    public Iterator<Cell> cellIterator() {
        return new CellIterator();
    }

    @Override
    public Element assemble(Element parent, GIO gio) {
        Document doc = parent.getOwnerDocument();
        Element re = assemble(doc, gio);

        if (idx != 0) re.setAttributeNodeNS(createAttributeNS(doc, "Index", idx));
        if (getStyleID() != null) {
            re.setAttributeNodeNS(createAttributeNS(doc, "StyleID", getStyleID()));
            gio.addStyleID(getStyleID());
        }
        if (span > 0) re.setAttributeNodeNS(createAttributeNS(doc, "Span", span));
        setAdditionalAttributes(doc, re);
        // if (autoFitHeight) re.setAttributeNodeNS(
        // createAttributeNS(doc, "AutoFitHeight", "1"));

        parent.appendChild(re);

        Iterator<Cell> iter = cellIterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();
            cell.assemble(re, gio);
        }

        return re;
    }

    private void setAdditionalAttributes(Document doc, Element rowElement) {
        if (height > 0.0) rowElement.setAttributeNodeNS(createAttributeNS(doc, "Height", "" + height));
        if (hidden) rowElement.setAttributeNodeNS(createAttributeNS(doc, "Hidden", "1"));
    }

    @Override
    public Element createElement(Document doc) {
        GIO gio = new GIO();
        Element rowElement = doc.createElementNS(getNameSpace(), getTagName());
        if (getStyleID() != null) {
            rowElement.setAttributeNodeNS(createAttributeNS(doc, "StyleID", getStyleID()));
        }
        setAdditionalAttributes(doc, rowElement);
        Iterator<Cell> iter = cellIterator();
        while (iter.hasNext()) {
            iter.next().assemble(rowElement, gio);
        }
        return rowElement;
    }

    @Override
    public void setAttributes(Attributes attrs) {
        for (int i = 0; i < attrs.getLength(); i++) {
            invokeMethod(attrs.getLocalName(i), attrs.getValue(i));
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

    /**
     * Sets the value of the ss:Index-attribute of this Row-element. This method is 
     * called by {@link nl.fountain.xelem.excel.Table#rowIterator()} to set the
     * index of this row during assembly.
     */
    @Override
    public void setIndex(int index) {
        idx = index;
    }

    @Override
    public int getIndex() {
        return idx;
    }

    /////////////////////////////////////////////
    private class CellIterator implements Iterator<Cell> {

        private Iterator<Integer> cit;
        private Integer current;
        private int prevIndex;

        protected CellIterator() {
            cit = cells.keySet().iterator();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return cit.hasNext();
        }

        @Override
        public Cell next() {
            current = cit.next();
            int curIndex = current.intValue();
            SSCell c = (SSCell) cells.get(current);
            if (prevIndex + 1 != curIndex) {
                c.setIndex(curIndex);
            } else {
                c.setIndex(0);
            }
            prevIndex = curIndex;
            return c;
        }

    }

}
