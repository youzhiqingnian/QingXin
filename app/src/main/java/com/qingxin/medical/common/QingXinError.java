package com.qingxin.medical.common;

import com.qingxin.medical.base.BaseBean;

/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */

public class QingXinError extends BaseBean {

    private Throwable throwable;

    private String msg;

    private String errorCode;

    public QingXinError(String msg) {
        this(null, msg, null);
    }

    public QingXinError(Throwable throwable) {
        this(throwable, null, null);
    }

    public QingXinError(Throwable throwable, String msg) {
        this(throwable, msg, null);
    }

    public QingXinError(Throwable throwable, String msg, String errorCode) {
        this.throwable = throwable;
        this.msg = msg;
        this.errorCode = errorCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
