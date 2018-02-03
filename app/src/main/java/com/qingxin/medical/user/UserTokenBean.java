package com.qingxin.medical.user;

import com.qingxin.medical.base.BaseBean;

/**
 * @author zhikuo
 */
public class UserTokenBean extends BaseBean {

    private User mem;
    private String token;

    public User getMem() {
        return mem;
    }

    public void setMem(User mem) {
        this.mem = mem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
