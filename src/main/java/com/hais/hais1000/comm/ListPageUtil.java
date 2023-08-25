package com.hais.hais1000.comm;

import com.github.pagehelper.PageInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svs:
 * @version 创建时间：2016-12-16 下午02:28:23
 * 类说明
 */
@Data
public class ListPageUtil<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 响应请求分页数据
     */
    public ListPageUtil(List<?> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo(list);
        this.list = list;
        this.totalCount = pageInfo.getTotal();
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getPageNum();
        this.totalPage = pageInfo.getPages();
    }

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public ListPageUtil(List<T> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = (long) totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public ListPageUtil() {}


}
