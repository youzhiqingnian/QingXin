package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ItemBean;

/**
 * 医美百科详情Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class MedicalBeautyDetailContract {

    interface View extends BaseView<Presenter> {

        void onSucess(ItemBean<MedicalBeautyRealDetailBean> medicalBeautyDetailBeen);

        void onError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getMedicalBeautyDetail(String id);
    }
}
