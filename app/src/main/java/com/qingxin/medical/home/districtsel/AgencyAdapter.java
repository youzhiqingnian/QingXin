package com.qingxin.medical.home.districtsel;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import java.util.List;

/**
 * 机构数据适配器
 * Date 2018/2/7
 *
 * @author zhikuo
 */
public class AgencyAdapter extends BaseQuickAdapter<StrictSelBean,BaseViewHolder>{

    AgencyAdapter(@Nullable List<StrictSelBean> data) {
        super(R.layout.adapter_agency, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StrictSelBean item) {
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.simpleDraweeView);
        TextView playCountTv = helper.getView(R.id.playCountTv);
        TextView nameTv = helper.getView(R.id.nameTv);
        TextView descrTv = helper.getView(R.id.descrTv);

        simpleDraweeView.setImageURI(Uri.parse(item.getThumbnail()));
        playCountTv.setText(String.format("%s 次播放", item.getOrder()));
        nameTv.setText(item.getName());
        descrTv.setText(item.getSummary());
    }
}
