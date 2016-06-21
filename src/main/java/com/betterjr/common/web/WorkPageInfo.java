package com.betterjr.common.web;

import com.betterjr.mapper.pagehelper.Page;

public class WorkPageInfo {
    private int pageNum =-1;
    private int pageSize =-1;
    private int pages =-1;
    private long total =-1;

    public WorkPageInfo() {

    }

    public WorkPageInfo(Page anPage) {
        this.pageNum = anPage.getPageNum();
        this.pageSize = anPage.getPageSize();
        this.pages = anPage.getPages();
        this.total = anPage.getTotal();
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int anPageNum) {
        this.pageNum = anPageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int anPageSize) {
        this.pageSize = anPageSize;
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int anPages) {
        this.pages = anPages;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long anTotal) {
        this.total = anTotal;
    }

}
