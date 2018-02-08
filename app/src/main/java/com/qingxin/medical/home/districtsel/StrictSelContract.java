package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.home.ListBean;

/**
 * 严选Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class StrictSelContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(ListBean<StrictSelBean> mDiary);

        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getStrictSelList(String type, int limit, int skip);
    }
}
