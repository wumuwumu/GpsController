/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.sciento.wumu.gpscontroller.Model.Model;

import com.sciento.wumu.gpscontroller.Model.BaseEntity;

import java.util.List;


public class Member extends BaseEntity {

    private String uid;

    private String account;

    private String password;

    private String phone;

    private Integer bindNum;

    private String salt;

    private Integer state;

    private String createdAt;

    private String updatedAt;


    private String sort = "";
    private String order = "";
    private List<JsonDevice> jsonDeviceList;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBindNum() {
        return bindNum;
    }

    public void setBindNum(Integer bindNum) {
        this.bindNum = bindNum;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<JsonDevice> getJsonDeviceList() {
        return jsonDeviceList;
    }

    public void setJsonDeviceList(List<JsonDevice> jsonDeviceList) {
        this.jsonDeviceList = jsonDeviceList;
    }

//    @Override
//    public String toString() {
//        return "Member{" +
//                "uid='" + uid + '\'' +
//                ", account='" + account + '\'' +
//                ", password='" + password + '\'' +
//                ", phone='" + phone + '\'' +
//                ", bindNum=" + bindNum +
//                ", salt='" + salt + '\'' +
//                ", state=" + state +
//                ", createdAt='" + createdAt + '\'' +
//                ", updatedAt='" + updatedAt + '\'' +
//                ", sort='" + sort + '\'' +
//                ", order='" + order + '\'' +
//                ", jsonDeviceList=" + jsonDeviceList +
//                '}';
//    }
}
