package com.qingxin.medical.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.qingxin.medical.listener.OnItemClickListener;
import java.util.List;

/**
 * 公共下弹浮框list. 每行绘制处理
 */
public class CommonDialogAdapter extends RecyclerView.Adapter<CommonDialogAdapter.ViewHolder> implements OnClickListener {

    private Context mContext;
    private List<CommonDialogData> mDatas;
    private OnItemClickListener<CommonDialogData> mClickListener;

    CommonDialogAdapter(@NonNull Context context, List<CommonDialogData> datas, OnItemClickListener<CommonDialogData> listener) {
        this.mContext = context;
        mDatas = datas;
        this.mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.group_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameTv.setText(mDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;

        ViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.nameTv);
            itemView.setOnClickListener(v -> {
                if (null != mClickListener) {
                    mClickListener.onItemClick(mDatas.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    /**
     * 公共list弹框每个元素数据信息
     */
    public interface CommonDialogData {
        /**
         * 获取一行数据id
         */
        int getId();

        /**
         * 获取一行数据name
         */
        String getName();

        /**
         * 获取行携带数据
         */
        Object getTag();
    }
}
