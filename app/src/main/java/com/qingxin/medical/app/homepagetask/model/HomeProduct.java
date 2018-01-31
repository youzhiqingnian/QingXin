package com.qingxin.medical.app.homepagetask.model;

import java.util.List;

/**
 * Created by user on 2018-01-31.
 */

public class HomeProduct {


    /**
     * code : 200
     * msg : ok
     * content : {"items":[{"id":"1534e158-d60b-486d-a75e-4e3cc7ee7602","name":"没脸","cover":null,"old_price":234,"price":120,"hospital":"仁和医院","isvip":"y","order":12,"ison":"y","created_at":"2018-1-26 22:16:27","updated_at":"2018-1-26 22:16:27"}],"count":1}
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
        /**
         * items : [{"id":"1534e158-d60b-486d-a75e-4e3cc7ee7602","name":"没脸","cover":null,"old_price":234,"price":120,"hospital":"仁和医院","isvip":"y","order":12,"ison":"y","created_at":"2018-1-26 22:16:27","updated_at":"2018-1-26 22:16:27"}]
         * count : 1
         */

        private int count;
        private List<ItemsBean> items;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * id : 1534e158-d60b-486d-a75e-4e3cc7ee7602
             * name : 没脸
             * cover : null
             * old_price : 234
             * price : 120
             * hospital : 仁和医院
             * isvip : y
             * order : 12
             * ison : y
             * created_at : 2018-1-26 22:16:27
             * updated_at : 2018-1-26 22:16:27
             */

            private String id;
            private String name;
            private String cover;
            private int old_price;
            private int price;
            private String hospital;
            private String isvip;
            private int order;
            private String ison;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getOld_price() {
                return old_price;
            }

            public void setOld_price(int old_price) {
                this.old_price = old_price;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getHospital() {
                return hospital;
            }

            public void setHospital(String hospital) {
                this.hospital = hospital;
            }

            public String getIsvip() {
                return isvip;
            }

            public void setIsvip(String isvip) {
                this.isvip = isvip;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getIson() {
                return ison;
            }

            public void setIson(String ison) {
                this.ison = ison;
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
                return "ItemsBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", cover=" + cover +
                        ", old_price=" + old_price +
                        ", price=" + price +
                        ", hospital='" + hospital + '\'' +
                        ", isvip='" + isvip + '\'' +
                        ", order=" + order +
                        ", ison='" + ison + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "count=" + count +
                    ", items=" + items +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HomeProduct{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
