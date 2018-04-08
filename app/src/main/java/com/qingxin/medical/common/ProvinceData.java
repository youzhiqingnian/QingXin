package com.qingxin.medical.common;

import java.util.List;

/**
 * Date 2018/4/8
 *
 * @author zhikuo
 */
public class ProvinceData {

    private String id;
    private String citycode;
    private String adcode;
    private String name;
    private String level;
    private String parent;
    private int index;
    private List<DistrictItemData> cities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<DistrictItemData> getCities() {
        return cities;
    }

    public void setCities(List<DistrictItemData> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return name;
    }
}
