package com.qingxin.medical.app.vip;

import java.util.List;

/**
 * Date 2018-02-09
 *
 * @author zhikuo1
 */
public class VipDetailItemBean {

    private String id;
    private String name;
    private String old_price;
    private String price;
    private String hospital;
    private String isvip;
    private int order;
    private String ison;
    private String created_at;
    private String updated_at;
    private String citycode;
    private int collect_num;
    private int book_num;
    private String mobile;
    private String is_collect;
    private String is_book;
    private String province_name;
    private String city_name;
    private String about;
    private List<String> cover;

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

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIson() {
        return ison;
    }

    public void setIson(String ison) {
        this.ison = ison;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getBook_num() {
        return book_num;
    }

    public void setBook_num(int book_num) {
        this.book_num = book_num;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_book() {
        return is_book;
    }

    public void setIs_book(String is_book) {
        this.is_book = is_book;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getCover() {
        return cover;
    }

    public void setCover(List<String> cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "VipDetailItemBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", old_price=" + old_price +
                ", price=" + price +
                ", hospital='" + hospital + '\'' +
                ", isvip='" + isvip + '\'' +
                ", order=" + order +
                ", ison='" + ison + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", citycode='" + citycode + '\'' +
                ", collect_num=" + collect_num +
                ", book_num=" + book_num +
                ", mobile='" + mobile + '\'' +
                ", is_collect='" + is_collect + '\'' +
                ", is_book='" + is_book + '\'' +
                ", province_name='" + province_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", about='" + about + '\'' +
                ", cover=" + cover +
                '}';
    }
}
