package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;

/**
 * 严选列表Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class StrictSelListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(ListBean<StrictSelBean> strictSelListBean);

        void onError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getStrictSelList(String type, int limit, int skip);
    }
}
