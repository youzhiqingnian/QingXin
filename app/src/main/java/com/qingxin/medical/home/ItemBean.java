package com.qingxin.medical.home;

import com.qingxin.medical.base.BaseBean;

/**
 * Date 2018-02-14
 *
 * @author zhikuo1
 */
public class ItemBean<T> extends BaseBean {

    private T item;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
