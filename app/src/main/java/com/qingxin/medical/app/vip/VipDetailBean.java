package com.qingxin.medical.app.vip;

/**
 * Date 2018-02-09
 *
 * @author zhikuo1
 */

public class VipDetailBean {

    private VipDetailItemBean items;

    public VipDetailItemBean getItems() {
        return items;
    }

    public void setItems(VipDetailItemBean items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "VipDetailBean{" +
                "items=" + items +
                '}';
    }
}
