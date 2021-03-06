package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.common.QingXinError;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
class HomePageTaskContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(HomeBean homeBean);
        void onError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getHomeData(String banner_size, String product_size, String diary_size);
    }
}
