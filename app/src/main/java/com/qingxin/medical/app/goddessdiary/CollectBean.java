package com.qingxin.medical.app.goddessdiary;

/**
 * Created by zhikuo1 on 2018-02-03.
 */

public class CollectBean {


    /**
     * code : 200
     * msg : ok
     * content : {"is_collect":"n","amount":0}
     */
    private String is_collect;
    private int amount;

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "is_collect='" + is_collect + '\'' +
                ", amount=" + amount +
                '}';
    }
}
