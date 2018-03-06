package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishResult;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public class MineDataContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(DiaryPublishResult diaryPublishResult);

        void onError(String result);
    }

    public interface Presenter extends BasePresenter {
        void diaryPublish(@NonNull DiaryPublishParams diaryPublishParams);

        void updateDiary(@NonNull DiaryPublishParams diaryPublishParams);
    }

}
