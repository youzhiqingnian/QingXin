package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.common.QingXinError;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
class MyCollectedProductListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(ProductListBean ProductListBean);

        void onCollectSuccess(CollectBean collectBean);

        void onError(QingXinError error);

        void onCollectError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getMyCollectProductList(int limit, int skip, String type, String actyp);

        void cancelCollect(String id);
    }
}
