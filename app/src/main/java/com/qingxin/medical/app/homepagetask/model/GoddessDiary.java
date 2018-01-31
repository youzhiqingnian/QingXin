package com.qingxin.medical.app.homepagetask.model;

import java.util.List;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiary {


    /**
     * code : 200
     * msg : ok
     * content : {"items":[{"id":"29fe42f0-4391-4438-936f-7341bdc84fae","name":"我的第一篇日记二恶","mem_id":"xxx","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/diary/3a3ed760-033f-11e8-9f53-2f701b8b47ec.jpg","created_at":"2018-1-26 23:54:56","summary":"这个真心不错呢","tags":"女人,美容","mem":{"id":"xxx","name":"青歆小妞","cover":"/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg"}}],"count":1}
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
         * items : [{"id":"29fe42f0-4391-4438-936f-7341bdc84fae","name":"我的第一篇日记二恶","mem_id":"xxx","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/diary/3a3ed760-033f-11e8-9f53-2f701b8b47ec.jpg","created_at":"2018-1-26 23:54:56","summary":"这个真心不错呢","tags":"女人,美容","mem":{"id":"xxx","name":"青歆小妞","cover":"/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg"}}]
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
             * id : 29fe42f0-4391-4438-936f-7341bdc84fae
             * name : 我的第一篇日记二恶
             * mem_id : xxx
             * oper_before_photo : http://p36zly2vu.bkt.clouddn.com/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg
             * oper_after_photo : http://p36zly2vu.bkt.clouddn.com/diary/3a3ed760-033f-11e8-9f53-2f701b8b47ec.jpg
             * created_at : 2018-1-26 23:54:56
             * summary : 这个真心不错呢
             * tags : 女人,美容
             * mem : {"id":"xxx","name":"青歆小妞","cover":"/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg"}
             */

            private String id;
            private String name;
            private String mem_id;
            private String oper_before_photo;
            private String oper_after_photo;
            private String created_at;
            private String summary;
            private String tags;
            private MemBean mem;

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

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
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

            public MemBean getMem() {
                return mem;
            }

            public void setMem(MemBean mem) {
                this.mem = mem;
            }

            public static class MemBean {
                /**
                 * id : xxx
                 * name : 青歆小妞
                 * cover : /banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg
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

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", mem_id='" + mem_id + '\'' +
                        ", oper_before_photo='" + oper_before_photo + '\'' +
                        ", oper_after_photo='" + oper_after_photo + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", summary='" + summary + '\'' +
                        ", tags='" + tags + '\'' +
                        ", mem=" + mem +
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
        return "GoddessDiary{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
