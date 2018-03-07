package com.qingxin.medical.app.goddessdiary.publish;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.common.QingXinError;

/**
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class DiaryPublishContract {

    public interface PublishView extends BaseView<Presenter> {

        void onPublishSuccess(DiaryPublishResult diaryPublishResult);

        void onUpdateSuccess(DiaryItemBean diaryItemBean);

        void onPublishFailed(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void diaryPublish(@NonNull DiaryPublishParams diaryPublishParams);

        void diaryUpdate(@NonNull DiaryPublishParams diaryPublishParams);
    }
}
