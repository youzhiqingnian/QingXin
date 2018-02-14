package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.home.ListBean;

/**
 * 医美百科Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class MedicalBeautyContract {

    interface View extends BaseView<Presenter> {
        void onSucess(ListBean<MedicalBeautyListBean> medicalBeautyListBeen);

        void onGetSecondarySuccess(ListBean<MedicalBeautyDetailBean> medicalBeautyDetailBeen);

        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getMedicalBeautyList(String id);

        void getMedicalBeautySecondList(String id);
    }
}
