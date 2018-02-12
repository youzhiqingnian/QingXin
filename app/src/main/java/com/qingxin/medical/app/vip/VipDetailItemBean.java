package com.qingxin.medical.app.vip;

/**
 * Date 2018-02-09
 *
 * @author zhikuo1
 */

public class VipDetailItemBean {

    /**
     * id : 48929048-0bad-4449-80f0-aa651728e29c
     * name : 不错的
     * cover : product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png
     * old_price : 234
     * price : 12
     * hospital : meidi
     * isvip : y
     * order : 12
     * ison : y
     * created_at : 2018-1-31 17:57:47
     * updated_at : 2018-1-31 17:57:47
     * citycode : 028
     * collect_num : 1
     * book_num : 1
     */
    private String id;
    private String name;
    private String cover;
    private int old_price;
    private int price;
    private String hospital;
    private String isvip;
    private int order;
    private String ison;
    private String created_at;
    private String updated_at;
    private String citycode;
    private int collect_num;
    private int book_num;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    @Override
    public String toString() {
        return "VipDetailBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
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
                '}';
    }

}