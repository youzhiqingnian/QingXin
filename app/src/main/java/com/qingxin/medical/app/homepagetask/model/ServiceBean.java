package com.qingxin.medical.app.homepagetask.model;

/**
 * Date 2018-02-11
 *
 * @author zhikuo1
 */

public class ServiceBean {


    /**
     * id : xxx
     * type : doctor
     * name : 黄小琥
     * video : null
     * thumbnail : http://p36zly2vu.bkt.clouddn.com/product/8a97eb90-082e-11e8-960c-591dc0e0fd80.png
     * summary : 不错哦这个艺术
     * order : 11
     * created_at : 2018-1-27 22:16:02
     * updated_at : 2018-1-27 22:16:02
     * citycode : 028
     */
    private String id;
    private String type;
    private String name;
    private Object video;
    private String thumbnail;
    private String summary;
    private int order;
    private String created_at;
    private String updated_at;
    private String citycode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    @Override
    public String toString() {
        return "ServiceBean{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", video=" + video +
                ", thumbnail='" + thumbnail + '\'' +
                ", summary='" + summary + '\'' +
                ", order=" + order +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", citycode='" + citycode + '\'' +
                '}';
    }
}
