package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;

/**
 * Created by zhikuo1 on 2018-02-02.
 */

public class DiaryDetailContract {

    interface View extends BaseView<DiaryDetailContract.Presenter> {

        void onSuccess(GoddessDiaryDetailBean mDiary);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void getGoddessDiaryDetail(String id);

    }

}
