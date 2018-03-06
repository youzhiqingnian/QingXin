package com.qingxin.medical.app.vip;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class VipListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(VipListBean vipListBean);
        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getVipList(int limit,int skip,String search);
    }
}
