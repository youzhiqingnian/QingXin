package com.qingxin.medical.app.goddessdiary.publish;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;

/**
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class DiaryPublishContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(DiaryPublishResult diaryPublishResult);

        void onError(String result);
    }

    public interface Presenter extends BasePresenter {
        void diaryPublish(@NonNull DiaryPublishParams diaryPublishParams);

        void updateDiary(@NonNull DiaryPublishParams diaryPublishParams);
    }
}
