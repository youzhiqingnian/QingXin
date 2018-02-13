package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.base.QingXinModel;
import com.qingxin.medical.home.ListBean;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放医美百科二级列表数据
 * Date 2018/2/13
 *
 * @author zhikuo
 */
public class MedicalBeautyModel extends QingXinModel {

    private Map<String, ListBean<MedicalBeautyDetailBean>> mMedicalBeautyDetailBean;

    @Override
    protected void onCreate() {
        super.onCreate();
        mMedicalBeautyDetailBean = new HashMap<>();
    }

    public void putData(String id, ListBean<MedicalBeautyDetailBean> medicalBeautyDetailBeans) {
        mMedicalBeautyDetailBean.put(id, medicalBeautyDetailBeans);
    }

    public ListBean<MedicalBeautyDetailBean> getData(String id) {
        return mMedicalBeautyDetailBean.get(id);
    }
}
