package com.hais.hais1000.comm;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
    private int curPage;             //当前页
    private int pageCount;           //总页数
    private int rowsCount = 0;           //总行数
    private int pageSize = 10;         //每页多少行

    public PageBean() {

    }

    public PageBean(int rows, int pageSize) {
        this.pageSize = pageSize;
        this.setRowsCount(rows);
        if (this.rowsCount % this.pageSize == 0) {
            this.pageCount = this.rowsCount / this.pageSize;
        } else if (rows < this.pageSize) {
            this.pageCount = 1;
        } else {
            this.pageCount = this.rowsCount / this.pageSize + 1;
        }
    }

    public List<T> pageInfo(List<T> list, int page, int pageSize) {
        if(null == list || list.isEmpty()) {
            this.pageSize = pageSize;
            this.curPage = page;
            this.pageCount = 0;
            this.rowsCount = 0;
            return new ArrayList<>();
        }
        PageBean pagebean = new PageBean(list.size(), pageSize);//初始化PageBean对象
        //设置当前页
        pagebean.setCurPage(page); //这里page是从页面上获取的一个参数，代表页数
        //获得分页大小
        int pagesize = pagebean.getPageSize();
        //获得分页数据在list集合中的索引
        int firstIndex = (page - 1) * pagesize;
        int toIndex = page * pagesize;
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        if (firstIndex > toIndex) {
            firstIndex = 0;
            pagebean.setCurPage(1);
        }
        //截取数据集合，获得分页数据
        List courseList = list.subList(firstIndex, toIndex);

        this.pageSize = pagebean.getPageSize();
        this.curPage = pagebean.getCurPage();
        this.pageCount = pagebean.getPageCount();
        this.rowsCount = pagebean.getRowsCount();
        return courseList;
    }


    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }
}

