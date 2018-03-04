package com.qingxin.medical.home.medicalbeauty;

import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLUtils;

import java.util.List;

/**
 * Date 2018/2/9
 *
 * @author zhikuo
 */
public class MedicalBeautyDetailAdapter extends BaseQuickAdapter<MedicalBeautyDetailBean, BaseViewHolder> {

    MedicalBeautyDetailAdapter(OnClickMedicalBeautyListener onClickMedicalBeautyListener) {
        this(null, onClickMedicalBeautyListener);
    }

    private OnClickMedicalBeautyListener mClickListener;

    private MedicalBeautyDetailAdapter(@Nullable List<MedicalBeautyDetailBean> data,OnClickMedicalBeautyListener onClickMedicalBeautyListener) {
        super(R.layout.group_medical_beauty_detail, data);
        this.mClickListener = onClickMedicalBeautyListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalBeautyDetailBean items) {
        TextView descrTv = helper.getView(R.id.descrTv);
        FlexboxLayout flexboxLayout = helper.getView(R.id.flexboxLayout);
        descrTv.setText(items.getName());
        List<MedicalBeautyListBean> medicalBeautyListBeans = items.getChildren();
        if (null != medicalBeautyListBeans && medicalBeautyListBeans.size() > 0) {
            flexboxLayout.removeAllViews();
            for (MedicalBeautyListBean medicalBeautyListBean : medicalBeautyListBeans) {
                TextView textView = new TextView(mContext);
                textView.setText(medicalBeautyListBean.getName());
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(VLUtils.dip2px(12), 0, VLUtils.dip2px(12), 0);
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(VLUtils.WRAP_CONTENT, VLUtils.dip2px(26));
                params.leftMargin = VLUtils.dip2px(15);
                params.topMargin = VLUtils.dip2px(15);
                textView.setTextColor(0xff464a4c);
                textView.setTextSize(12);
                textView.setBackgroundResource(R.drawable.ripple_medical_beauty_bg);
                flexboxLayout.addView(textView, params);
                textView.setOnClickListener(v -> {
                    if (null != mClickListener){
                        mClickListener.onClick(medicalBeautyListBean);
                    }
                });
            }
        }
    }

    public interface OnClickMedicalBeautyListener{
        void onClick(MedicalBeautyListBean medicalBeautyListBean);
    }
}
