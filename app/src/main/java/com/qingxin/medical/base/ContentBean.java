package com.qingxin.medical.base;

/**
 *  封装的请求javabean
 * Date 2018-02-05
 * @author zhikuo1
 */
public class ContentBean<T> extends BaseBean {

    private String code;

    private String msg;

    private T content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
