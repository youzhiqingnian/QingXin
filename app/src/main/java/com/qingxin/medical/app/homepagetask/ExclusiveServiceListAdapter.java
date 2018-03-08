package com.qingxin.medical.app.homepagetask;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;

import java.util.List;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class ExclusiveServiceListAdapter extends BaseQuickAdapter<ServiceBean, BaseViewHolder> {

    private OnClickConsultListener mClickListener;

    ExclusiveServiceListAdapter(@Nullable List<ServiceBean> data, OnClickConsultListener onClickConsultListener) {
        super(R.layout.layout_service_item, data);
        this.mClickListener = onClickConsultListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        SimpleDraweeView headSdv = helper.getView(R.id.headSdv);
        TextView nameTv = helper.getView(R.id.nameTv);
        TextView goodAtTv = helper.getView(R.id.goodAtTv);
        ImageView consultTv = helper.getView(R.id.consultTv);
        if (!TextUtils.isEmpty(item.getCover())) {
            headSdv.setImageURI(Uri.parse(item.getCover()));
        }
        nameTv.setText(item.getName());
        goodAtTv.setText(item.getGood_at());

        consultTv.setOnClickListener(view -> {
            if (null != mClickListener) {
                mClickListener.onClickConsult(item.getMobile());
            }
        });
    }

    public interface OnClickConsultListener {
        void onClickConsult(String moblile);
    }
}
