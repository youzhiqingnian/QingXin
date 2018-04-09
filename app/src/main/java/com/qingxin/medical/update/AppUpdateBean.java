package com.qingxin.medical.update;

import com.qingxin.medical.base.BaseBean;

/**
 * Date 2018/4/9
 *
 * @author zhikuo
 */
public class AppUpdateBean extends BaseBean {

    //是否有更新：Y 有  N 否
    private String hasnew;
    private AppUpdateItem item;

    public String getHasnew() {
        return hasnew;
    }

    public void setHasnew(String hasnew) {
        this.hasnew = hasnew;
    }

    public AppUpdateItem getItem() {
        return item;
    }

    public void setItem(AppUpdateItem item) {
        this.item = item;
    }

    public static class AppUpdateItem extends BaseBean {
        //版本号
        private String version;
        //级别：建议更新 suggest 强制更新 force
        private String level;
        //更新日志
        private String log;
        //apk文件下载地址
        private String url;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
