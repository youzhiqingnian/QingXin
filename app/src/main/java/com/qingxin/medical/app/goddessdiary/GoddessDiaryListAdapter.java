package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.CommonAdapter;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.service.manager.NetRequestListManager;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiaryListAdapter extends CommonAdapter<GoddessDiaryBean.ContentBean.ItemsBean> {

    public GoddessDiaryListAdapter(Context contex, List<GoddessDiaryBean.ContentBean.ItemsBean> mDatas){
        super(contex,mDatas);
    }

    public void refresh(List<GoddessDiaryBean.ContentBean.ItemsBean> datas){
        mData = datas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View
                    .inflate(mContext,R.layout.layout_home_goddess_diary_item, null);
            holder.mAuthoerHeadSdv = convertView.findViewById(R.id.authoerHeadSdv);
            holder.mBeforeCoverSdv = convertView.findViewById(R.id.beforeCoverSdv);
            holder.mAfterCoverSdv = convertView.findViewById(R.id.afterCoverSdv);
            holder.mAuthorName = convertView.findViewById(R.id.authorName);
            holder.mDiaryContentTv = convertView.findViewById(R.id.diaryContentTv);
            holder.mDiaryTagTv = convertView.findViewById(R.id.diaryTagTv);
            holder.mScanCountTv = convertView.findViewById(R.id.scanCountTv);
            holder.mCollectionCountTv = convertView.findViewById(R.id.collectionCountTv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(mData.get(position).getMem() != null){
            holder.mAuthoerHeadSdv.setImageURI(Uri.parse(mData.get(position).getMem().getCover()));
            holder.mAuthorName.setText(mData.get(position).getMem().getName());
        }



        holder.mBeforeCoverSdv.setImageURI(Uri.parse(mData.get(position).getOper_before_photo()));
        holder.mAfterCoverSdv.setImageURI(Uri.parse(mData.get(position).getOper_after_photo()));
        holder.mDiaryContentTv.setText(mData.get(position).getSummary());
        holder.mDiaryTagTv.setText(mData.get(position).getTags());


        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView mAuthoerHeadSdv;

        TextView mAuthorName, mDiaryContentTv, mDiaryTagTv, mScanCountTv, mCollectionCountTv;

        SimpleDraweeView mBeforeCoverSdv, mAfterCoverSdv;
    }

    /**
     * Created by zhikuo1 on 2018-01-31.
     */

    public static class GoddessDiaryPresenter implements DiaryListContract.Presenter {

        @NonNull
        private final DiaryListContract.View mGoddessDiaryView;

        @NonNull
        private CompositeSubscription mCompositeSubscription;

        private GoddessDiaryBean mDiary;

        GoddessDiaryPresenter(DiaryListContract.View goddessDiaryView){
            mGoddessDiaryView = goddessDiaryView;
            mCompositeSubscription = new CompositeSubscription();
            mGoddessDiaryView.setPresenter(this);
        }

        @Override
        public void subscribe() {

        }

        @Override
        public void unsubscribe() {
            if (mCompositeSubscription.hasSubscriptions()){
                mCompositeSubscription.unsubscribe();
            }
        }

        @Override
        public void populateTask() {

        }

        @Override
        public void getGoddessDiaryList(String limit, String skip) {
            mCompositeSubscription.add(NetRequestListManager.getGoddessDiary(limit,skip)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GoddessDiaryBean>() {
                        @Override
                        public void onCompleted() {
                            if (mDiary != null){
                                mGoddessDiaryView.onSuccess(mDiary);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mGoddessDiaryView.onError("请求失败！！");
                        }

                        @Override
                        public void onNext(GoddessDiaryBean diary) {
                            mDiary = diary;
                        }
                    })
            );
        }

        @Override
        public boolean isDataMissing() {
            return false;
        }
    }
}
