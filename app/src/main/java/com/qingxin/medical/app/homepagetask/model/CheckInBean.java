package com.qingxin.medical.app.homepagetask.model;

/**
 * Date 2018-02-24
 *
 * @author zhikuo1
 */

public class CheckInBean {


    /**
     * balance : 86
     */

    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CheckInBean{" +
                "balance=" + balance +
                '}';
    }
}
