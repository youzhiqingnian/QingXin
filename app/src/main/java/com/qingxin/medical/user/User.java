package com.qingxin.medical.user;

import com.qingxin.medical.base.BaseBean;

/**
 * 用户数据
 *
 * @author zhikuo
 */
public class User extends BaseBean {
    //昵称
    private String name;
    //用户id
    private String id;
    //手机号
    private String mobile;
    //头像
    private String cover;
    //青歆币
    private String coin;
    //可提现金额
    private String available_coin;
    // 今天是否签到
    private String has_checkin;
    // 预约产品的数量
    private int book_amount;
    // 发布日记的数量
    private int diary_amount;
    // 收藏产品和日记总和的数量
    private int collect_amount;
    private String birthday;
    private String city_id;
    //性别，男：M 女：F
    private String gender;
    private String province_id;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvailable_coin() {
        return available_coin;
    }

    public void setAvailable_coin(String available_coin) {
        this.available_coin = available_coin;
    }

    public String getHas_checkin() {
        return has_checkin;
    }

    public void setHas_checkin(String has_checkin) {
        this.has_checkin = has_checkin;
    }

    public int getBook_amount() {
        return book_amount;
    }

    public void setBook_amount(int book_amount) {
        this.book_amount = book_amount;
    }

    public int getDiary_amount() {
        return diary_amount;
    }

    public void setDiary_amount(int diary_amount) {
        this.diary_amount = diary_amount;
    }

    public int getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(int collect_amount) {
        this.collect_amount = collect_amount;
    }
}
