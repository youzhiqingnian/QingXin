package com.qingxin.medical.app.homepagetask.model;


import java.io.Serializable;
import java.util.List;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class VipProductBean  implements Serializable {


    /**
     * id : 0c792be5-59fb-483b-a88d-4d94e7598a11
     * name : 美容针
     * cover : ["http://p3u20cqo9.bkt.clouddn.com/product/46acb4c0-0cce-11e8-9a80-a72b786a38c9.jpg"]
     * old_price : 3888
     * price : 1288
     * hospital : 北京清华长庚医院
     * isvip : y
     * order : 9
     * ison : y
     * created_at : 2018-2-6 23:53:46
     * updated_at : 2018-2-6 23:53:46
     * citycode : 0311
     * collect_num : 1
     * book_num : 0
     * mobile : null
     */

    private String id;
    private String name;
    private int old_price;
    private int price;
    private String hospital;
    private String isvip;
    private int order;
    private String ison;
    private String created_at;
    private String updated_at;
    private String citycode;
    private String price_unit;
    private String city_name;
    private String province_name;
    private int collect_num;
    private int book_num;
    private String mobile;
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

    public int getOld_price() {
        return old_price;
    }

    public void setOld_price(int old_price) {
        this.old_price = old_price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public List<String> getCover() {
        return cover;
    }

    public void setCover(List<String> cover) {
        this.cover = cover;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    @Override
    public String toString() {
        return "VipProductBean{" +
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
                ", price_unit='" + price_unit + '\'' +
                ", city_name='" + city_name + '\'' +
                ", province_name='" + province_name + '\'' +
                ", collect_num=" + collect_num +
                ", book_num=" + book_num +
                ", mobile='" + mobile + '\'' +
                ", cover=" + cover +
                '}';
    }
}
