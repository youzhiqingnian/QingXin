package com.qingxin.medical.user;

import com.qingxin.medical.base.BaseBean;

/**
 * @author zhikuo
 */
public class UserTokenBean extends BaseBean {

    private String status;
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
