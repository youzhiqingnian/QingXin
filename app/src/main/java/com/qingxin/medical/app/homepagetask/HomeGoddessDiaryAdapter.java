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
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.app.homepagetask.model.HomeBean;

import java.util.List;


public class HomeGoddessDiaryAdapter extends RecyclerView.Adapter<HomeGoddessDiaryAdapter.MyViewHolder> {
    private Context mContext;
    private ItemClickListener itemClickListener;
    List<HomeBean.ContentBean.DiarysBean> mDiaryList;

    HomeGoddessDiaryAdapter(Context context, List<HomeBean.ContentBean.DiarysBean> diaryList) {
        this.mContext = context;
        this.mDiaryList = diaryList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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

        if(mDiaryList.get(position).getMem() != null){
            holder.mAuthoerHeadSdv.setImageURI(Uri.parse(mDiaryList.get(position).getMem().getCover()));
            holder.mAuthorName.setText(mDiaryList.get(position).getMem().getName());
        }



        holder.mBeforeCoverSdv.setImageURI(Uri.parse(mDiaryList.get(position).getOper_before_photo()));
        holder.mAfterCoverSdv.setImageURI(Uri.parse(mDiaryList.get(position).getOper_after_photo()));
        holder.mDiaryContentTv.setText(mDiaryList.get(position).getSummary());
        holder.mDiaryTagTv.setText(mDiaryList.get(position).getTags());

    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mAuthoerHeadSdv, 
                mBeforeCoverSdv, 
                mAfterCoverSdv;
        
        TextView mAuthorName, mDiaryContentTv, mDiaryTagTv, mScanCountTv, mCollectionCountTv;

        MyViewHolder(final View itemView) {
            super(itemView);
            mAuthoerHeadSdv = itemView.findViewById(R.id.mAuthoerHeadSdv);
            mBeforeCoverSdv = itemView.findViewById(R.id.mBeforeCoverSdv);
            mAfterCoverSdv = itemView.findViewById(R.id.mAfterCoverSdv);
            mAuthorName = itemView.findViewById(R.id.mAuthorName);
            mDiaryContentTv = itemView.findViewById(R.id.mDiaryContentTv);
            mDiaryTagTv = itemView.findViewById(R.id.mDiaryTagTv);
            mScanCountTv = itemView.findViewById(R.id.mScanCountTv);
            mCollectionCountTv = itemView.findViewById(R.id.mCollectionCountTv);


            //为item添加普通点击回调
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(itemView, getPosition());
                }
            });
        }
    }

    public interface ItemClickListener {

        /**
         * Item的普通点击
         */
        public void onItemClick(View view, int position);

        /**
         * Item长按
         */
        public void onItemLongClick(View view, int position);
    }

}
