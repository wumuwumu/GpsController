/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.sciento.wumu.gpscontroller.Model.Model;


/**
 * 基础信息
 */

public class BaseEntity {


    private Integer offset = 0;


    private Integer limit = 10;


    private String search = "";


    private String cross = "";
    private Integer count = 0;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCross() {
        return cross;
    }

    public void setCross(String cross) {
        this.cross = cross;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
