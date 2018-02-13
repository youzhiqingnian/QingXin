package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.base.ContentBean;

/**
 * 医美百科详情Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class MedicalBeautyDetailContract {

    interface View extends BaseView<Presenter> {

        void onSucess(MedicalBeautyDetailBean medicalBeautyDetailBeen);

        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getMedicalBeautyDetail(String id);
    }
}
