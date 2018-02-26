package com.qingxin.medical.app.homepagetask.model;

/**
 * Date 2018-02-24
 *
 * @author zhikuo1
 */

public class CoinLogBean {

    /**
     * id : 9b7827c6-fa25-4bae-a6c4-df4ead1c37d7
     * mem_id : b7d38cd8-a762-4fcc-bfc7-fde1e2efd424
     * amount : 2
     * action : -
     * use : admin
     * remark : cxxx
     * balance : 36
     * created_at : 2018-1-29 21:00:05
     * updated_at : 2018-1-29 21:00:05
     */

    private String id;
    private String mem_id;
    private int amount;
    private String action;
    private String use;
    private String remark;
    private int balance;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "CoinLogBean{" +
                "id='" + id + '\'' +
                ", mem_id='" + mem_id + '\'' +
                ", amount=" + amount +
                ", action='" + action + '\'' +
                ", use='" + use + '\'' +
                ", remark='" + remark + '\'' +
                ", balance=" + balance +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
