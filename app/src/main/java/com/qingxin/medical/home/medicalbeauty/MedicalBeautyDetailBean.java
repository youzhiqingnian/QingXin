package com.qingxin.medical.home.medicalbeauty;

/**
 * Date 2018-02-13
 *
 * @author zhikuo1
 */

public class MedicalBeautyDetailBean {


    /**
     * name : 偶们
     * id : 5fb6877b-886f-405a-86d1-75990dc95234
     * order : 0
     * wiki_detail_id : d5b5f409-44e3-46b0-ac12-17795fa2b7dd
     * detail : {"id":"d5b5f409-44e3-46b0-ac12-17795fa2b7dd","cover":"http://p36zly2vu.bkt.clouddn.com/wiki/0194d880-04af-11e8-9152-d984e555bc73.jpg","feature":"<p>zhendexxxxx<\/p >","care":"<p>xxx<\/p >","introduce":"<p>yyyy<\/p >","oper_before_tip":"<p>vvvv<\/p >","oper_after_tip":"<p>ffff<\/p >","icontxt":null,"created_at":"2018-1-28 19:36:16","updated_at":"2018-1-28 19:36:16"}
     */

    private String name;
    private String id;
    private int order;
    private String wiki_detail_id;
    private DetailBean detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getWiki_detail_id() {
        return wiki_detail_id;
    }

    public void setWiki_detail_id(String wiki_detail_id) {
        this.wiki_detail_id = wiki_detail_id;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        /**
         * id : d5b5f409-44e3-46b0-ac12-17795fa2b7dd
         * cover : http://p36zly2vu.bkt.clouddn.com/wiki/0194d880-04af-11e8-9152-d984e555bc73.jpg
         * feature : <p>zhendexxxxx</p >
         * care : <p>xxx</p >
         * introduce : <p>yyyy</p >
         * oper_before_tip : <p>vvvv</p >
         * oper_after_tip : <p>ffff</p >
         * icontxt : null
         * created_at : 2018-1-28 19:36:16
         * updated_at : 2018-1-28 19:36:16
         */

        private String id;
        private String cover;
        private String feature;
        private String care;
        private String introduce;
        private String oper_before_tip;
        private String oper_after_tip;
        private Object icontxt;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getCare() {
            return care;
        }

        public void setCare(String care) {
            this.care = care;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getOper_before_tip() {
            return oper_before_tip;
        }

        public void setOper_before_tip(String oper_before_tip) {
            this.oper_before_tip = oper_before_tip;
        }

        public String getOper_after_tip() {
            return oper_after_tip;
        }

        public void setOper_after_tip(String oper_after_tip) {
            this.oper_after_tip = oper_after_tip;
        }

        public Object getIcontxt() {
            return icontxt;
        }

        public void setIcontxt(Object icontxt) {
            this.icontxt = icontxt;
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
    }
}
