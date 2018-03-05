package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.base.BaseBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class DiaryItemBean extends BaseBean {


    /**
     * id : 25e2cc72-4a84-4bf5-adc0-dac91e158693
     * name : 面部年轻唤起
     * product_id : e12d0a20-1a05-445f-92a3-a911af3a74bf
     * mem_id : 9ca209a8-41a6-441f-92cf-7508570b2765
     * oper_before_photo : http://qingxin-assets.awesomes.cn/app/0e05c4f0-1ca9-11e8-baed-179e2dc32e46
     * oper_after_photo : http://qingxin-assets.awesomes.cn/app/11645940-1ca9-11e8-baed-179e2dc32e46
     * summary : 京东快递就到家的时候就想着我说你不是因为有个人们都在想你
     * tags : null
     * words : <p>京东快递就到家的时候就想着我说你不是因为有个人们都在想你</p>
     * ison : n
     * created_at : 2018-3-1 01:01:52
     * updated_at : 2018-02-28T17:01:52.000Z
     * visit_num : 109
     * collect_num : 3
     * state : pass
     * wiki_id : null
     * mem : {"id":"9ca209a8-41a6-441f-92cf-7508570b2765","name":"小倩","cover":"http://qingxin-assets.awesomes.cn/banner/c63b79d0-065a-11e8-b745-8dcdb0f2d3c3.jpg"}
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
    private String state;
    private String wiki_id;
    private MemBean mem;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWiki_id() {
        return wiki_id;
    }

    public void setWiki_id(String wiki_id) {
        this.wiki_id = wiki_id;
    }

    public MemBean getMem() {
        return mem;
    }

    public void setMem(MemBean mem) {
        this.mem = mem;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public static class MemBean {
        /**
         * id : 9ca209a8-41a6-441f-92cf-7508570b2765
         * name : 小倩
         * cover : http://qingxin-assets.awesomes.cn/banner/c63b79d0-065a-11e8-b745-8dcdb0f2d3c3.jpg
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
    }
}
