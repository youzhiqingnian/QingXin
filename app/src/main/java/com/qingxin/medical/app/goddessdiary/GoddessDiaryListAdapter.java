package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.CommonAdapter;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;

import java.util.List;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiaryListAdapter extends CommonAdapter<GoddessDiary.ContentBean.ItemsBean> {

    public GoddessDiaryListAdapter(Context contex, List<GoddessDiary.ContentBean.ItemsBean> mDatas){
        super(contex,mDatas);
    }

    public void refresh(List<GoddessDiary.ContentBean.ItemsBean> datas){
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
            holder.civ_home_goddess_diary_head = convertView.findViewById(R.id.civ_home_goddess_diary_head);
            holder.riv_before_cosmetic = convertView.findViewById(R.id.riv_before_cosmetic);
            holder.riv_after_cosmetic = convertView.findViewById(R.id.riv_after_cosmetic);
            holder.tv_goddess_nickname = convertView.findViewById(R.id.tv_goddess_nickname);
            holder.tv_cosmetic_diary_intro = convertView.findViewById(R.id.tv_cosmetic_diary_intro);
            holder.tv_cosmetic_name = convertView.findViewById(R.id.tv_cosmetic_name);
            holder.tv_scan_count = convertView.findViewById(R.id.tv_scan_count);
            holder.tv_collection_count = convertView.findViewById(R.id.tv_collection_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(mData.get(position).getMem() != null){
            holder.civ_home_goddess_diary_head.setImageURI(Uri.parse(mData.get(position).getMem().getCover()));
            holder.tv_goddess_nickname.setText(mData.get(position).getMem().getName());
        }



        holder.riv_before_cosmetic.setImageURI(Uri.parse(mData.get(position).getOper_before_photo()));
        holder.riv_after_cosmetic.setImageURI(Uri.parse(mData.get(position).getOper_after_photo()));
        holder.tv_cosmetic_diary_intro.setText(mData.get(position).getSummary());
        holder.tv_cosmetic_name.setText(mData.get(position).getTags());


        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView civ_home_goddess_diary_head;

        TextView tv_goddess_nickname, tv_cosmetic_diary_intro, tv_cosmetic_name, tv_scan_count, tv_collection_count;

        SimpleDraweeView riv_before_cosmetic, riv_after_cosmetic;
    }

}
