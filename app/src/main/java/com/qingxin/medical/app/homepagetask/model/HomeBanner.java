package com.qingxin.medical.app.homepagetask.model;

import java.util.List;

/**
 * Created by user on 2018-01-30.
 */

public class HomeBanner {


    /**
     * code : 200
     * msg : ok
     * content : {"items":[{"cover":"http://p36zly2vu.bkt.clouddn.com/banner/6d462860-030e-11e8-a396-918c763c8dc1.jpg","link":"https://developer.qiniu.com/fusion"},{"cover":"http://p36zly2vu.bkt.clouddn.com/banner/b219b440-030c-11e8-ab41-65a272bf64f3.jpg","link":"https://developer.qiniu.com/kodo/sdk/1289/nodejs"},{"cover":"http://p36zly2vu.bkt.clouddn.com/banner/9abec860-030e-11e8-a396-918c763c8dc1.jpg","link":"https://support.qiniu.com/tickets/101069"}]}
     */

    private String code;
    private String msg;
    private ContentBean content;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * cover : http://p36zly2vu.bkt.clouddn.com/banner/6d462860-030e-11e8-a396-918c763c8dc1.jpg
             * link : https://developer.qiniu.com/fusion
             */

            private String cover;
            private String link;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "cover='" + cover + '\'' +
                        ", link='" + link + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "items=" + items +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HomeBanner{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
