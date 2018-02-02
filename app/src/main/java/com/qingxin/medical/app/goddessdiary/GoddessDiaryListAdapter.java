package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.CommonAdapter;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import java.util.List;

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
            holder.mAuthoerHeadSdv = convertView.findViewById(R.id.mAuthoerHeadSdv);
            holder.mBeforeCoverSdv = convertView.findViewById(R.id.mBeforeCoverSdv);
            holder.mAfterCoverSdv = convertView.findViewById(R.id.mAfterCoverSdv);
            holder.mAuthorName = convertView.findViewById(R.id.mAuthorName);
            holder.mDiaryContentTv = convertView.findViewById(R.id.mDiaryContentTv);
            holder.mDiaryTagTv = convertView.findViewById(R.id.mDiaryTagTv);
            holder.mScanCountTv = convertView.findViewById(R.id.mScanCountTv);
            holder.mCollectionCountTv = convertView.findViewById(R.id.mCollectionCountTv);
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

}
