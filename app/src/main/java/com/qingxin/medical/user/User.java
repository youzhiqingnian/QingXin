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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cover='" + cover + '\'' +
                ", coin='" + coin + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
