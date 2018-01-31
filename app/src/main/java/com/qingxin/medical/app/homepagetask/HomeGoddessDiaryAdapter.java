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
    private Context context;
    private ItemClickListener itemClickListener;
    List<HomeBean.ContentBean.DiarysBean> mDiaryList;

    HomeGoddessDiaryAdapter(Context context, List<HomeBean.ContentBean.DiarysBean> diaryList) {
        this.context = context;
        this.mDiaryList = diaryList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_home_goddess_diary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /**
         * 得到item的LayoutParams布局参数
         */

        if(mDiaryList.get(position).getMem() != null){
            holder.civ_home_goddess_diary_head.setImageURI(Uri.parse(mDiaryList.get(position).getMem().getCover()));
            holder.tv_goddess_nickname.setText(mDiaryList.get(position).getMem().getName());
        }



        holder.riv_before_cosmetic.setImageURI(Uri.parse(mDiaryList.get(position).getOper_before_photo()));
        holder.riv_after_cosmetic.setImageURI(Uri.parse(mDiaryList.get(position).getOper_after_photo()));
        holder.tv_cosmetic_diary_intro.setText(mDiaryList.get(position).getSummary());
        holder.tv_cosmetic_name.setText(mDiaryList.get(position).getTags());

    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView civ_home_goddess_diary_head;

        TextView tv_goddess_nickname, tv_cosmetic_diary_intro, tv_cosmetic_name, tv_scan_count, tv_collection_count;

        SimpleDraweeView riv_before_cosmetic, riv_after_cosmetic;


        MyViewHolder(final View itemView) {
            super(itemView);
            civ_home_goddess_diary_head = itemView.findViewById(R.id.civ_home_goddess_diary_head);
            riv_before_cosmetic = itemView.findViewById(R.id.riv_before_cosmetic);
            riv_after_cosmetic = itemView.findViewById(R.id.riv_after_cosmetic);
            tv_goddess_nickname = itemView.findViewById(R.id.tv_goddess_nickname);
            tv_cosmetic_diary_intro = itemView.findViewById(R.id.tv_cosmetic_diary_intro);
            tv_cosmetic_name = itemView.findViewById(R.id.tv_cosmetic_name);
            tv_scan_count = itemView.findViewById(R.id.tv_scan_count);
            tv_collection_count = itemView.findViewById(R.id.tv_collection_count);


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
