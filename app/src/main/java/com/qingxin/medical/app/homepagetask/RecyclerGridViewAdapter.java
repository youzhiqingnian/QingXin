package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.widget.indicator.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by user on 2018-01-30.
 */

public class RecyclerGridViewAdapter extends RecyclerView.Adapter<RecyclerGridViewAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    //子view是否充满了手机屏幕
    List<HomeBean.ContentBean.PreferrsBean> mPreferrsList;

    public interface OnRecyclerViewItemListener {
        public void onItemClickListener(View view, int position);

        public void onItemLongClickListener(View view, int position);
    }

    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mOnRecyclerViewItemListener = listener;
    }

    public RecyclerGridViewAdapter(Context mContext,List<HomeBean.ContentBean.PreferrsBean> preferrsList) {
        this.mContext = mContext;
        this.mPreferrsList = preferrsList;
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


        viewHolder.mSelectionCoverSdv.setImageURI(Uri.parse(mPreferrsList.get(i).getThumbnail()));
        viewHolder.mInstituteNameTv.setText(mPreferrsList.get(i).getName());
        viewHolder.mInstituteIntroTv.setText(mPreferrsList.get(i).getSummary());
    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return mPreferrsList.size();
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mSelectionCoverSdv;

        TextView mInstituteNameTv, mInstituteIntroTv;

        //在布局中找到所含有的UI组件
        public ViewHolder(View itemView) {
            super(itemView);
            mSelectionCoverSdv = itemView.findViewById(R.id.selectionCoverSdv);
            mInstituteNameTv = itemView.findViewById(R.id.instituteNameTv);
            mInstituteIntroTv = itemView.findViewById(R.id.instituteIntroTv);

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