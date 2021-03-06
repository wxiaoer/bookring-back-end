package com.gyb.bookring.entity;

public class OptionBook {
    private long id;
    private long userId;
    private String name;
    private long allPage;
    private long page;
    private String classify;
    private String netUrlType;
    private String netUrl;
    private long lastReadTime;

    private String email;
    private String password;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getAllPage() {
        return allPage;
    }

    public void setAllPage(long allPage) {
        this.allPage = allPage;
    }


    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }


    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }


    public String getNetUrlType() {
        return netUrlType;
    }

    public void setNetUrlType(String netUrlType) {
        this.netUrlType = netUrlType;
    }


    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }


    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
