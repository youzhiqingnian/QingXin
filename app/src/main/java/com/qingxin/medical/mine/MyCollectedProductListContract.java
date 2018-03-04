package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class MyCollectedProductListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(VipListBean vipListBean);
        void onSuccess(CollectBean collectBean);
        void onError(String result);
    }

    interface Presenter extends BasePresenter {
        void getMyCollectProductList(int limit, int skip, String type, String actyp);
        void cancelCollect(String id);
    }
}
