package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.base.BaseBean;

/**
 * 医美百科列表item
 * Date 2018/2/9
 *
 * @author zhikuo
 */

public class MedicalBeautyListBean extends BaseBean {

    private String id;
    private String name;
    private String order;
    private boolean isSelect;
    private int position;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
