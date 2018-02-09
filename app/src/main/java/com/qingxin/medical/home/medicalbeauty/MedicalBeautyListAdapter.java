package com.qingxin.medical.home.medicalbeauty;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingxin.medical.R;
import java.util.List;

/**
 * Date 2018/2/9
 *
 * @author zhikuo
 */

public class MedicalBeautyListAdapter extends BaseQuickAdapter<MedicalBeautyListBean, BaseViewHolder> {

    public MedicalBeautyListAdapter(){
        this(null);
    }

    public MedicalBeautyListAdapter(@Nullable List<MedicalBeautyListBean> data) {
        super(R.layout.group_medical_beauty_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalBeautyListBean item) {
        TextView nameTv = helper.getView(R.id.nameTv);
        View lineView = helper.getView(R.id.lineView);
        nameTv.setText(item.getName());
        if (item.isSelect()) {
            helper.itemView.setBackgroundColor(0xffffffff);
            nameTv.setTextColor(0xff3bc5e8);
            if (lineView.getVisibility() == View.GONE) {
                lineView.setVisibility(View.VISIBLE);
            }
        } else {
            helper.itemView.setBackgroundColor(0xffECF0F3);
            nameTv.setTextColor(0xff464a4c);
            if (lineView.getVisibility() == View.VISIBLE) {
                lineView.setVisibility(View.GONE);
            }
        }
    }
}
