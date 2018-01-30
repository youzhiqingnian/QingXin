package com.qingxin.medical.app.homepagetask;

/**
 * Created by user on 2018-01-30.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.widget.indicator.view.CircleImageView;
import com.qingxin.medical.widget.indicator.view.RoundCornerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w_x on 2016/6/17.
 * 设置RecyclerView的适配器
 */
public class HomeGoddessDiaryAdapter extends RecyclerView.Adapter<HomeGoddessDiaryAdapter.MyViewHolder> {
    private Context context;
    private ItemClickListener itemClickListener;

    HomeGoddessDiaryAdapter(Context context) {
        this.context = context;
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
//        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
//        params.height = height.get(position);//把随机的高度赋予item布局
//        holder.itemView.setLayoutParams(params);//把params设置item布局

//        holder.textView.setText(list.get(position));//为控件绑定数据
//        //为TextView添加监听回调
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemSubViewClick(holder.textView, position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView civ_home_goddess_diary_head;

        TextView tv_goddess_nickname, tv_cosmetic_diary_intro, tv_cosmetic_name, tv_scan_count, tv_collection_count;

        RoundCornerImageView riv_before_cosmetic, riv_after_cosmetic;


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
