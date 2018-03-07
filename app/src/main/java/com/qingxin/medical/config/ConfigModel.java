package com.qingxin.medical.config;

import com.vlee78.android.vl.VLModel;

/**
 * 获取全局的配置信息
 * <p>
 * Date 2018/3/7
 *
 * @author zhikuo
 */
public class ConfigModel extends VLModel {

    public ConfigBean getConfigBean() {
        return mConfigBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.mConfigBean = configBean;
    }

    private ConfigBean mConfigBean;
}
