package com.qingxin.medical.app.homepagetask.model;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public class MemBean {

    /**
     * id : b7d38cd8-a762-4fcc-bfc7-fde1e2efd424
     * name : 黄晓鸿
     * updated_at : 2018-02-06T12:42:24.161Z
     */

    private String id;
    private String name;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "MemBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
