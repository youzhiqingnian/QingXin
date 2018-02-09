package com.qingxin.medical.home.medicalbeauty;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.qingxin.medical.R;
import com.qingxin.medical.home.ListBean;

import java.util.List;

/**
 * Date 2018/2/9
 *
 * @author zhikuo
 */
public class MedicalBeautyDetailAdapter extends BaseQuickAdapter<ListBean<MedicalBeautyListBean>, BaseViewHolder> {

    public MedicalBeautyDetailAdapter(){
        this(null);
    }

    public MedicalBeautyDetailAdapter(@Nullable List<ListBean<MedicalBeautyListBean>> data) {
        super(R.layout.group_medical_beauty_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean<MedicalBeautyListBean> items) {
        TextView descrTv = helper.getView(R.id.descrTv);
        FlexboxLayout flexboxLayout = helper.getView(R.id.flexboxLayout);


    }
}
