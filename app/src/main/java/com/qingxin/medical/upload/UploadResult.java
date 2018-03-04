package com.qingxin.medical.upload;

import com.qingxin.medical.base.BaseBean;

/**
 * Date 2018/3/4
 *
 * @author zhikuo
 */

public class UploadResult extends BaseBean{

   private String url;

   private String filename;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
