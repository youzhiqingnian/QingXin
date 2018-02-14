package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.base.BaseBean;
import java.util.List;
/**
 * 医美二级列表
 * <p>
 * Date 2018/2/9
 *
 * @author zhikuo
 */
public class MedicalBeautyDetailBean extends BaseBean {

    private String name;
    private String id;
    private String order;
    private List<MedicalBeautyListBean> children;

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


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<MedicalBeautyListBean> getChildren() {
        return children;
    }

    public void setChildren(List<MedicalBeautyListBean> children) {
        this.children = children;
    }
}
