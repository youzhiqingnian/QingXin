package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;

/**
 *
 * Date 2018-02-02
 * @author zhikuo1
 */

public class DiaryDetailContract {

    interface View extends BaseView<DiaryDetailContract.Presenter> {

        void onSuccess(GoddessDiaryDetailBean mDiary);

        void onSuccess(CollectBean mCollectBean);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void getGoddessDiaryDetail(String id);

        void collectDiary(String id);

    }
}
