package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.widget.indicator.view.RoundCornerImageView;

/**
 * Created by user on 2018-01-30.
 */

public class RecyclerGridViewAdapter extends RecyclerView.Adapter<RecyclerGridViewAdapter.ViewHolder> {
    private Context mContext;
    //    private String[] data;
//    private int[] imgdata;
    private LayoutInflater mInflater;
    //子view是否充满了手机屏幕
    private boolean isCompleteFill = false;

    public interface OnRecyclerViewItemListener {
        public void onItemClickListener(View view, int position);

        public void onItemLongClickListener(View view, int position);
    }

    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mOnRecyclerViewItemListener = listener;
    }

    public RecyclerGridViewAdapter(Context mContext) {
        this.mContext = mContext;
//        this.data = data;
//        this.imgdata = imgdata;
        mInflater = LayoutInflater.from(mContext);

    }


    //RecyclerView显示的子View
    //该方法返回是ViewHolder，当有可复用View时，就不再调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.layout_home_strict_selection_famous_doctor_institute, viewGroup, false);
        return new ViewHolder(v);
    }

    //将数据绑定到子View，会自动复用View
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder == null) {
            return;
        }
        if (mOnRecyclerViewItemListener != null) {
            itemOnClick(viewHolder);
            itemOnLongClick(viewHolder);
        }
//        viewHolder.textView.setText(data[i]);
//        viewHolder.imageView.setBackgroundResource(imgdata[i]);
    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return 2;
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mSelectionCoverSdv;

        TextView mInstituteNameTv, mInstituteIntroTv;

        //在布局中找到所含有的UI组件
        public ViewHolder(View itemView) {
            super(itemView);
            mSelectionCoverSdv = itemView.findViewById(R.id.mSelectionCoverSdv);
            mInstituteNameTv = itemView.findViewById(R.id.mInstituteNameTv);
            mInstituteIntroTv = itemView.findViewById(R.id.mInstituteIntroTv);

        }
    }

    //单机事件
    private void itemOnClick(final RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnRecyclerViewItemListener.onItemClickListener(holder.itemView, position);
            }
        });
    }

    //长按事件
    private void itemOnLongClick(final RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                mOnRecyclerViewItemListener.onItemLongClickListener(holder.itemView, position);
                //返回true是为了防止触发onClick事件
                return true;
            }
        });
    }

}