package com.qingxin.medical.home;

import com.qingxin.medical.base.BaseBean;
import java.util.List;

/**
 * 列表javabean
 * Date 2018/2/8
 *
 * @author zhikuo
 */
public class ListBean<T> extends BaseBean {

    private int count;

    private String balance;

    private List<T> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "ListBean{" +
                "count=" + count +
                ", items=" + items +
                '}';
    }
}
