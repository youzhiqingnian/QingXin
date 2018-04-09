package com.qingxin.medical.config;

import android.support.annotation.NonNull;
import com.qingxin.medical.common.DistrictItemData;
import com.qingxin.medical.common.ProvinceData;
import com.vlee78.android.vl.VLModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取全局的配置信息
 * <p>
 * Date 2018/3/7
 *
 * @author zhikuo
 */
public class ConfigModel extends VLModel {

    private ConfigBean mConfigBean;

    private List<ProvinceData> proviceData;

    private Map<String, ProvinceData> provinceDataMap;

    private Map<String, DistrictItemData> citiesDataMap;

    public Map<String, ProvinceData> getProvinceDataMap() {
        return provinceDataMap;
    }

    public void setProvinceDataMap(Map<String, ProvinceData> provinceDataMap) {
        this.provinceDataMap = provinceDataMap;
    }

    public Map<String, DistrictItemData> getCitiesDataMap() {
        return citiesDataMap;
    }

    public void setCitiesDataMap(Map<String, DistrictItemData> citiesDataMap) {
        this.citiesDataMap = citiesDataMap;
    }

    public List<ProvinceData> getProviceData() {
        return proviceData;
    }

    public void setProviceData(List<ProvinceData> proviceData) {
        this.proviceData = proviceData;
    }

    public ConfigBean getConfigBean() {
        return mConfigBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.mConfigBean = configBean;
    }

    public void resetDistrictData(@NonNull List<DistrictItemData> districtBeans) {
        List<ProvinceData> proviceDatas = new ArrayList<>();
        Map<String, ProvinceData> provinceDataMap = new HashMap<>();
        Map<String, DistrictItemData> cityDataMap = new HashMap<>();
        int index = 0;
        for (DistrictItemData districtItemData : districtBeans) {
            if ("province".equals(districtItemData.getLevel())) {
                districtItemData.setIndex(index);
                ProvinceData provinceData = new ProvinceData();
                provinceData.setId(districtItemData.getId());
                provinceData.setAdcode(districtItemData.getAdcode());
                provinceData.setCitycode(districtItemData.getCitycode());
                provinceData.setParent(districtItemData.getParent());
                provinceData.setLevel(districtItemData.getLevel());
                provinceData.setName(districtItemData.getName());
                provinceData.setIndex(index);
                proviceDatas.add(provinceData);
                index++;
            }
        }

        int cityIndex = 0;
        for (ProvinceData provinceData : proviceDatas) {
            List<DistrictItemData> cities = new ArrayList<>();
            for (DistrictItemData districtItemData : districtBeans) {
                if (provinceData.getId().equals(districtItemData.getParent()) && "city".equals(districtItemData.getLevel())) {
                    cities.add(districtItemData);
                    districtItemData.setProvinceData(provinceData);
                    districtItemData.setIndex(cityIndex);
                    cityDataMap.put(districtItemData.getId(), districtItemData);
                    cityIndex++;
                }
            }
            provinceData.setCities(cities);
            provinceDataMap.put(provinceData.getId(), provinceData);
        }
        setCitiesDataMap(cityDataMap);
        setProviceData(proviceDatas);
        setProvinceDataMap(provinceDataMap);
    }
}
