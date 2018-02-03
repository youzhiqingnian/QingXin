package com.qingxin.medical.app.homepagetask.model;

import com.qingxin.medical.user.User;

import java.util.List;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiaryBean {

    /**
     * code : 200
     * msg : ok
     * content : {"items":[{"id":"29fe42f0-4391-4438-936f-7341bdc84fae","name":"我的第一篇日记二恶","mem_id":"xxx","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/diary/3a3ed760-033f-11e8-9f53-2f701b8b47ec.jpg","created_at":"2018-1-26 23:54:56","summary":"这个真心不错呢","tags":"女人,美容","mem":{"id":"xxx","name":"青歆小妞","cover":"/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg"}}],"count":1}
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
        private User mem;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getMem_id() {
            return mem_id;
        }

        public String getOper_before_photo() {
            return oper_before_photo;
        }

        public String getOper_after_photo() {
            return oper_after_photo;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getSummary() {
            return summary;
        }

        public String getTags() {
            return tags;
        }

        public User getMem() {
            return mem;
        }
    }
}
