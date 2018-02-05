package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.HomeBean;

import java.util.List;


public class HomeGoddessDiaryAdapter extends RecyclerView.Adapter<HomeGoddessDiaryAdapter.MyViewHolder> {
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    List<HomeBean.DiarysBean> mDiaryList;

    HomeGoddessDiaryAdapter(Context context, List<HomeBean.DiarysBean> diaryList) {
        this.mContext = context;
        this.mDiaryList = diaryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_home_goddess_diary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /**
         * 得到item的LayoutParams布局参数
         */

        if (mDiaryList.get(position).getMem() != null) {
            holder.mAuthoerHeadSdv.setImageURI(Uri.parse(mDiaryList.get(position).getMem().getCover()));
            holder.mAuthorName.setText(mDiaryList.get(position).getMem().getName());
        }


        holder.mBeforeCoverSdv.setImageURI(Uri.parse(mDiaryList.get(position).getOper_before_photo()));
        holder.mAfterCoverSdv.setImageURI(Uri.parse(mDiaryList.get(position).getOper_after_photo()));
        holder.mDiaryContentTv.setText(mDiaryList.get(position).getSummary());
        holder.mDiaryTagTv.setText(mDiaryList.get(position).getTags());
        holder.mScanCountTv.setText(String.valueOf(mDiaryList.get(position).getVisit_num()));
        holder.mCollectionCountTv.setText(String.valueOf(mDiaryList.get(position).getCollect_num()));

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView mAuthoerHeadSdv,
                mBeforeCoverSdv,
                mAfterCoverSdv;

        TextView mAuthorName, mDiaryContentTv, mDiaryTagTv, mScanCountTv, mCollectionCountTv;

        MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAuthoerHeadSdv = itemView.findViewById(R.id.authoerHeadSdv);
            mBeforeCoverSdv = itemView.findViewById(R.id.beforeCoverSdv);
            mAfterCoverSdv = itemView.findViewById(R.id.afterCoverSdv);
            mAuthorName = itemView.findViewById(R.id.authorName);
            mDiaryContentTv = itemView.findViewById(R.id.diaryContentTv);
            mDiaryTagTv = itemView.findViewById(R.id.diaryTagTv);
            mScanCountTv = itemView.findViewById(R.id.scanCountTv);
            mCollectionCountTv = itemView.findViewById(R.id.collectionCountTv);

        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view, (int) view.getTag());
            }
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
