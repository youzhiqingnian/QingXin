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
    private String mobile;
    private String cover;
    private String coin;
    private String gender;
    private String birthday;
    private String province_id;
    private String city_id;
    private String city;
    private String province;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "MemBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cover='" + cover + '\'' +
                ", coin='" + coin + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", province_id='" + province_id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
