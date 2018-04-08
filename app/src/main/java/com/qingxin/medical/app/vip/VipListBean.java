package com.qingxin.medical.app.vip;

import com.qingxin.medical.app.homepagetask.model.VipProductBean;

import java.util.List;

/**
 * Date 2018-02-05
 * 歆人专享列表的bean
 * @author zhikuo1
 */

public class VipListBean {

    private List<VipProductBean> items;
    private int count;

    public List<VipProductBean> getItems() {
        return items;
    }

    public void setItems(List<VipProductBean> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
