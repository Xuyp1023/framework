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
import nl.fountain.xelem.excel.Column;
import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.Table;
import nl.fountain.xelem.excel.Worksheet;

/**
 * An implementation of the XLElement Table.
 */
public class SSTable extends AbstractXLElement implements Table {

    TreeMap<Integer, Column> columns;
    TreeMap<Integer, Row> rows;
    private String styleID;
    private double rowheight;
    private double columnwidth;
    private int expandedcolumncount;
    private int expandedrowcount;

    /**
     * Constructs a new SSTable.
     * 
     * @see nl.fountain.xelem.excel.Worksheet#getTable()
     */
    public SSTable() {
        rows = new TreeMap<Integer, Row>();
        columns = new TreeMap<Integer, Column>();
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
    public void setDefaultRowHeight(double points) {
        rowheight = points;
    }

    @Override
    public void setDefaultColumnWidth(double points) {
        columnwidth = points;
    }

    @Override
    public Column addColumn() {
        return addColumnAt(maxColumnIndex() + 1, new SSColumn());
    }

    @Override
    public Column addColumnAt(int index) {
        return addColumnAt(index, new SSColumn());
    }

    @Override
    public Column addColumn(Column column) {
        return addColumnAt(maxColumnIndex() + 1, column);
    }

    @Override
    public Column addColumnAt(int index, Column column) {
        if (index < Worksheet.firstColumn || index > Worksheet.lastColumn) {
            throw new IndexOutOfBoundsException("columnIndex = " + index);
        }
        columns.put(new Integer(index), column);
        return column;
    }

    @Override
    public Column removeColumnAt(int columnIndex) {
        return columns.remove(new Integer(columnIndex));
    }

    @Override
    public Column getColumnAt(int columnIndex) {
        Column column = columns.get(new Integer(columnIndex));
        if (column == null) {
            column = addColumnAt(columnIndex);
        }
        return column;
    }

    @Override
    public boolean hasColumnAt(int index) {
        return columns.get(new Integer(index)) != null;
    }

    @Override
    public Collection<Column> getColumns() {
        return columns.values();
    }

    @Override
    public Row addRow() {
        return addRowAt(maxRowIndex() + 1, new SSRow());
    }

    @Override
    public Row addRowAt(int index) {
        return addRowAt(index, new SSRow());
    }

    @Override
    public Row addRow(Row row) {
        return addRowAt(maxRowIndex() + 1, row);
    }

    @Override
    public Row addRowAt(int index, Row row) {
        if (index < Worksheet.firstRow || index > Worksheet.lastRow) {
            throw new IndexOutOfBoundsException("rowIndex = " + index);
        }
        rows.put(new Integer(index), row);
        return row;
    }

    @Override
    public Row removeRowAt(int rowIndex) {
        Row row = rows.remove(new Integer(rowIndex));
        return row;
    }

    @Override
    public Collection<Row> getRows() {
        return rows.values();
    }

    @Override
    public TreeMap<Integer, Row> getRowMap() {
        return rows;
    }

    @Override
    public Row getRowAt(int rowIndex) {
        Row row = rows.get(new Integer(rowIndex));
        if (row == null) {
            row = addRowAt(rowIndex);
        }
        return row;
    }

    @Override
    public boolean hasRowAt(int index) {
        return rows.get(new Integer(index)) != null;
    }

    @Override
    public int rowCount() {
        return rows.size();
    }

    @Override
    public int columnCount() {
        return columns.size();
    }

    @Override
    public boolean hasChildren() {
        return (columns.size() + rows.size()) > 0;
    }

    @Override
    public int maxCellIndex() {
        int max = 0;
        for (Row r : rows.values()) {
            if (r.maxCellIndex() > max) max = r.maxCellIndex();
        }
        return max;
    }

    @Override
    public int maxRowIndex() {
        int lastKey;
        if (rows.size() == 0) {
            lastKey = 0;
        } else {
            lastKey = rows.lastKey().intValue();
        }
        return lastKey;
    }

    @Override
    public int maxColumnIndex() {
        int lastKey;
        if (columns.size() == 0) {
            lastKey = 0;
        } else {
            lastKey = columns.lastKey().intValue();
        }
        return lastKey;
    }

    @Override
    public Iterator<Row> rowIterator() {
        return new RowIterator();
    }

    @Override
    public Iterator<Column> columnIterator() {
        return new ColumnIterator();
    }

    @Override
    public String getTagName() {
        return "Table";
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
        Element te = assemble(doc, gio);

        if (getStyleID() != null) {
            te.setAttributeNodeNS(createAttributeNS(doc, "StyleID", getStyleID()));
            gio.addStyleID(getStyleID());
        }
        if (rowheight > 0.0) te.setAttributeNodeNS(createAttributeNS(doc, "DefaultRowHeight", "" + rowheight));
        if (columnwidth > 0.0) te.setAttributeNodeNS(createAttributeNS(doc, "DefaultColumnWidth", "" + columnwidth));

        parent.appendChild(te);

        Iterator<Column> iterC = columnIterator();
        while (iterC.hasNext()) {
            iterC.next().assemble(te, gio);
        }

        Iterator<Row> iterR = rowIterator();
        while (iterR.hasNext()) {
            iterR.next().assemble(te, gio);
        }
        return te;
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

    @Override
    public int getExpandedColumnCount() {
        return expandedcolumncount;
    }

    // method called by ExcelReader
    private void setExpandedColumnCount(String s) {
        expandedcolumncount = Integer.parseInt(s);
    }

    @Override
    public int getExpandedRowCount() {
        return expandedrowcount;
    }

    // method called by ExcelReader
    private void setExpandedRowCount(String s) {
        expandedrowcount = Integer.parseInt(s);
    }

    /////////////////////////////////////////////
    private class RowIterator implements Iterator<Row> {

        private Iterator<Integer> rit;
        private Integer current;
        private int prevIndex;

        protected RowIterator() {
            rit = rows.keySet().iterator();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return rit.hasNext();
        }

        @Override
        public Row next() {
            current = rit.next();
            int curIndex = current.intValue();
            SSRow r = (SSRow) rows.get(current);
            if (prevIndex + 1 != curIndex) {
                r.setIndex(curIndex);
            } else {
                r.setIndex(0);
            }
            prevIndex = curIndex;
            return r;
        }

    }

    private class ColumnIterator implements Iterator<Column> {

        private Iterator<Integer> cit;
        private Integer current;
        private int prevIndex;
        private int maxSpan;

        protected ColumnIterator() {
            cit = columns.keySet().iterator();
        }

        // @see java.util.Iterator#remove()
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        // @see java.util.Iterator#hasNext()
        @Override
        public boolean hasNext() {
            return cit.hasNext();
        }

        // @see java.util.Iterator#next()
        @Override
        public Column next() {
            current = cit.next();
            int curIndex = current.intValue();
            SSColumn c = (SSColumn) columns.get(current);
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
