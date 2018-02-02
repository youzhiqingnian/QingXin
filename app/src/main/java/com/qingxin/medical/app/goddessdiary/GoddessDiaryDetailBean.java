package com.qingxin.medical.app.goddessdiary;

/**
 * Created by zhikuo1 on 2018-02-02.
 */

public class GoddessDiaryDetailBean {


    /**
     * code : 200
     * msg : ok
     * content : {"item":{"id":"17373a5e-1d90-422a-a139-fc6427d40804","name":"xxxx","product_id":"48929048-0bad-4449-80f0-aa651728e29c","mem_id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/diary/2e2044c0-036f-11e8-a325-cbf4f3071fd9.png","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/null","summary":"xxxx","tags":"xxx","words":"","ison":"y","created_at":"2018-1-27 22:34:41","updated_at":"2018-01-27T14:34:41.000Z","visit_num":9,"collect_num":0,"mem":{"id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","name":"黄小琥","cover":"http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg"},"product":{"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的","cover":"http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png","old_price":234,"price":12,"hospital":"meidi","isvip":"y","order":12,"ison":"y","created_at":"2018-1-31 17:57:47","updated_at":"2018-1-31 17:57:47"},"is_collect":"n"}}
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
         * item : {"id":"17373a5e-1d90-422a-a139-fc6427d40804","name":"xxxx","product_id":"48929048-0bad-4449-80f0-aa651728e29c","mem_id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/diary/2e2044c0-036f-11e8-a325-cbf4f3071fd9.png","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/null","summary":"xxxx","tags":"xxx","words":"","ison":"y","created_at":"2018-1-27 22:34:41","updated_at":"2018-01-27T14:34:41.000Z","visit_num":9,"collect_num":0,"mem":{"id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","name":"黄小琥","cover":"http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg"},"product":{"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的","cover":"http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png","old_price":234,"price":12,"hospital":"meidi","isvip":"y","order":12,"ison":"y","created_at":"2018-1-31 17:57:47","updated_at":"2018-1-31 17:57:47"},"is_collect":"n"}
         */

        private ItemBean item;

        public ItemBean getItem() {
            return item;
        }

        public void setItem(ItemBean item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * id : 17373a5e-1d90-422a-a139-fc6427d40804
             * name : xxxx
             * product_id : 48929048-0bad-4449-80f0-aa651728e29c
             * mem_id : b7d38cd8-a762-4fcc-bfc7-fde1e2efd424
             * oper_before_photo : http://p36zly2vu.bkt.clouddn.com/diary/2e2044c0-036f-11e8-a325-cbf4f3071fd9.png
             * oper_after_photo : http://p36zly2vu.bkt.clouddn.com/null
             * summary : xxxx
             * tags : xxx
             * words :
             * ison : y
             * created_at : 2018-1-27 22:34:41
             * updated_at : 2018-01-27T14:34:41.000Z
             * visit_num : 9
             * collect_num : 0
             * mem : {"id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","name":"黄小琥","cover":"http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg"}
             * product : {"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的","cover":"http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png","old_price":234,"price":12,"hospital":"meidi","isvip":"y","order":12,"ison":"y","created_at":"2018-1-31 17:57:47","updated_at":"2018-1-31 17:57:47"}
             * is_collect : n
             */

            private String id;
            private String name;
            private String product_id;
            private String mem_id;
            private String oper_before_photo;
            private String oper_after_photo;
            private String summary;
            private String tags;
            private String words;
            private String ison;
            private String created_at;
            private String updated_at;
            private int visit_num;
            private int collect_num;
            private MemBean mem;
            private ProductBean product;
            private String is_collect;

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

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getMem_id() {
                return mem_id;
            }

            public void setMem_id(String mem_id) {
                this.mem_id = mem_id;
            }

            public String getOper_before_photo() {
                return oper_before_photo;
            }

            public void setOper_before_photo(String oper_before_photo) {
                this.oper_before_photo = oper_before_photo;
            }

            public String getOper_after_photo() {
                return oper_after_photo;
            }

            public void setOper_after_photo(String oper_after_photo) {
                this.oper_after_photo = oper_after_photo;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
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

            public int getVisit_num() {
                return visit_num;
            }

            public void setVisit_num(int visit_num) {
                this.visit_num = visit_num;
            }

            public int getCollect_num() {
                return collect_num;
            }

            public void setCollect_num(int collect_num) {
                this.collect_num = collect_num;
            }

            public MemBean getMem() {
                return mem;
            }

            public void setMem(MemBean mem) {
                this.mem = mem;
            }

            public ProductBean getProduct() {
                return product;
            }

            public void setProduct(ProductBean product) {
                this.product = product;
            }

            public String getIs_collect() {
                return is_collect;
            }

            public void setIs_collect(String is_collect) {
                this.is_collect = is_collect;
            }

            public static class MemBean {
                /**
                 * id : b7d38cd8-a762-4fcc-bfc7-fde1e2efd424
                 * name : 黄小琥
                 * cover : http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg
                 */

                private String id;
                private String name;
                private String cover;

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

                @Override
                public String toString() {
                    return "MemBean{" +
                            "id='" + id + '\'' +
                            ", name='" + name + '\'' +
                            ", cover='" + cover + '\'' +
                            '}';
                }
            }

            public static class ProductBean {
                /**
                 * id : 48929048-0bad-4449-80f0-aa651728e29c
                 * name : 不错的
                 * cover : http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png
                 * old_price : 234
                 * price : 12
                 * hospital : meidi
                 * isvip : y
                 * order : 12
                 * ison : y
                 * created_at : 2018-1-31 17:57:47
                 * updated_at : 2018-1-31 17:57:47
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
                    return "ProductBean{" +
                            "id='" + id + '\'' +
                            ", name='" + name + '\'' +
                            ", cover='" + cover + '\'' +
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
                return "ItemBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", product_id='" + product_id + '\'' +
                        ", mem_id='" + mem_id + '\'' +
                        ", oper_before_photo='" + oper_before_photo + '\'' +
                        ", oper_after_photo='" + oper_after_photo + '\'' +
                        ", summary='" + summary + '\'' +
                        ", tags='" + tags + '\'' +
                        ", words='" + words + '\'' +
                        ", ison='" + ison + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        ", visit_num=" + visit_num +
                        ", collect_num=" + collect_num +
                        ", mem=" + mem +
                        ", product=" + product +
                        ", is_collect='" + is_collect + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "item=" + item +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GoddessDiaryDetailBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
