package com.qingxin.medical.app.vip;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.CollectBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class VipDetailContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(VipDetailBean vipDetailBean);

        void onSuccess(CollectBean collectBean);

        void onSuccess(AmountBean amountBean);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void getVipDetail(String id);

        void collect(String id);

        void book(String id);

    }

}
