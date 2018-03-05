package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.app.homepagetask.model.ProductBean;
import com.qingxin.medical.base.BaseBean;
import com.qingxin.medical.user.User;

/**
 *
 * Date 2018-02-05
 * @author zhikuo1
 */

public class DiaryItemBean extends BaseBean{

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
    private User mem;
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

    String getProduct_id() {
        return product_id;
    }

    void setProduct_id(String product_id) {
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

    void setWords(String words) {
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

    public User getMem() {
        return mem;
    }

    public void setMem(User mem) {
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

    @Override
    public String toString() {
        return "DiaryItemBean{" +
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
