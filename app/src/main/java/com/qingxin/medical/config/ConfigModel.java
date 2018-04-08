package com.qingxin.medical.config;

import com.qingxin.medical.common.DistrictItemData;
import com.qingxin.medical.common.ProvinceData;
import com.vlee78.android.vl.VLModel;

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
}
