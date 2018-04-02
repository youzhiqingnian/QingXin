package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
class MyPublishedDiaryListContract {

    interface View extends BaseView<Presenter> {
        void onSuccess(ListBean<DiaryItemBean> diary);

        void onDeleteDiarySuccess(int position);

        void onError(QingXinError error);

        void onDeleteDiaryError(QingXinError error);
    }

    interface Presenter extends BasePresenter {
        void getMyPublishedDiaryList(String author, int limit, int skip);

        void deleteDiary(int position, String diaryId);
    }
}
