package com.qingxin.medical.app.vip;

import com.qingxin.medical.app.homepagetask.model.ProductBean;
import java.util.List;

/**
 * Date 2018-02-05
 * 歆人专享列表的bean
 * @author zhikuo1
 */

public class VipListBean {

    private List<ProductBean> items;
    private int count;

    public List<ProductBean> getItems() {
        return items;
    }

    public void setItems(List<ProductBean> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "VipListBean{" +
                "items=" + items +
                ", count=" + count +
                '}';
    }
}
