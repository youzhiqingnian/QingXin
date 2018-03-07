package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class MyCollectedDiaryListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(ListBean<DiaryItemBean> diary);
        void onSuccess(CollectBean collectBean);
        void onError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getMyCollectDiaryList(int limit, int skip, String type, String actyp);
        void cancelCollectDiary(String id);
    }
}
