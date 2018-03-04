package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.vip.VipListBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class MyBookedProductListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(VipListBean vipListBean);
        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getMyBookedProductList(int limit, int skip, String type, String actyp);
    }
}