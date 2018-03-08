package com.qingxin.medical.config;

import com.qingxin.medical.base.BaseBean;

import java.util.List;

/**
 * 全局配置bean
 * Date 2018/3/7
 *
 * @author zhikuo
 */
public class ConfigBean extends BaseBean {

    //每日签到
    private String checkin_coin;
    //推荐用户成功
    private String recommend_mem_coin;
    //邀请新人注册成功
    private String recommend_new_coin;
    //发表日记成功
    private String post_diary_coin;
    // 首页产品图片集
    private List<String> homeProductImages;
    // 清新币规则h5连接地址
    private String coin_rule_url;

    public String getCheckin_coin() {
        return checkin_coin;
    }

    public void setCheckin_coin(String checkin_coin) {
        this.checkin_coin = checkin_coin;
    }

    public String getRecommend_mem_coin() {
        return recommend_mem_coin;
    }

    public void setRecommend_mem_coin(String recommend_mem_coin) {
        this.recommend_mem_coin = recommend_mem_coin;
    }

    public String getRecommend_new_coin() {
        return recommend_new_coin;
    }

    public void setRecommend_new_coin(String recommend_new_coin) {
        this.recommend_new_coin = recommend_new_coin;
    }

    public String getPost_diary_coin() {
        return post_diary_coin;
    }

    public void setPost_diary_coin(String post_diary_coin) {
        this.post_diary_coin = post_diary_coin;
    }

    public List<String> getHomeProductImages() {
        return homeProductImages;
    }

    public void setHomeProductImages(List<String> homeProductImages) {
        this.homeProductImages = homeProductImages;
    }

    public String getCoin_rule_url() {
        return coin_rule_url;
    }

    public void setCoin_rule_url(String coin_rule_url) {
        this.coin_rule_url = coin_rule_url;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "checkin_coin='" + checkin_coin + '\'' +
                ", recommend_mem_coin='" + recommend_mem_coin + '\'' +
                ", recommend_new_coin='" + recommend_new_coin + '\'' +
                ", post_diary_coin='" + post_diary_coin + '\'' +
                ", homeProductImages=" + homeProductImages +
                ", coin_rule_url='" + coin_rule_url + '\'' +
                '}';
    }
}
