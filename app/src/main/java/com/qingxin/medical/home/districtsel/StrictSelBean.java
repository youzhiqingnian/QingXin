package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.base.BaseBean;

import java.io.Serializable;

/**
 * 严选javabean
 * Date 2018/2/7
 *
 * @author zhikuo
 */
public class StrictSelBean extends BaseBean  implements Serializable {

    private String city_name;

    private String province_name;

    private String[] cover;

    private String id;

    private String name;

    private String type;
    //简介
    private String summary;
    //创建日期
    private String created_at;
    //视频地址
    private String video;
    //次数
    private String order;
    //城市
    private String citycode;

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

    public String[] getCover() {
        return cover;
    }

    public void setCover(String[] cover) {
        this.cover = cover;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
