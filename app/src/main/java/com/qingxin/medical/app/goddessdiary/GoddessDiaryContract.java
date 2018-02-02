package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
/**
 * Created by zhikuo1 on 2018-01-31.
 */
public class GoddessDiaryContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(GoddessDiaryBean mDiary);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void populateTask();

        void getGoddessDiaryList(String limit,String skip);

        boolean isDataMissing();
    }


}
