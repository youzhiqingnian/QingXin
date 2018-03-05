package com.qingxin.medical.app.homepagetask.model;

/**
 * Date 2018-03-05
 *
 * @author zhikuo1
 */

public class WithdrawalsItemBean {


    /**
     * mem_id : xxx
     * amount : 10
     * id : 91899a58-5cee-4ed8-aca7-7f8551a4faa1
     * updated_at : 2018-03-03T04:56:30.760Z
     * created_at : 2018-03-03T04:56:30.760Z
     */

    private String mem_id;
    private String amount;
    private String id;
    private String updated_at;
    private String created_at;

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "WithdrawalsItemBean{" +
                "mem_id='" + mem_id + '\'' +
                ", amount='" + amount + '\'' +
                ", id='" + id + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
