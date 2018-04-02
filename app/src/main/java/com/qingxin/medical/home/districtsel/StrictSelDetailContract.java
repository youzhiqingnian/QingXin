package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.common.QingXinError;

/**
 * 严选详情Presenter
 * Date 2018-02-08
 *
 * @author zhikuo
 */
class StrictSelDetailContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(StrictSelBean strictSelBean);

        void onError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getStrictSelDetail(String id);
    }
}
