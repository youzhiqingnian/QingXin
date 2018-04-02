package com.qingxin.medical.app.homepagetask;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.home.districtsel.StrictSelBean;
import java.util.List;

/**
 * Date 2018-01-30
 *
 * @author zhikuo1
 */
public class RecyclerGridViewAdapter extends BaseQuickAdapter<StrictSelBean, BaseViewHolder> {

    RecyclerGridViewAdapter(@Nullable List<StrictSelBean> data) {
        super(R.layout.group_home_strictsel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StrictSelBean item) {
        SimpleDraweeView mSelectionCoverSdv = helper.getView(R.id.selectionCoverSdv);
        TextView nameTv = helper.getView(R.id.nameTv);
        TextView descrTv = helper.getView(R.id.descrTv);
        String[] cover = item.getCover();
        if (null != cover && cover.length > 0) {
            mSelectionCoverSdv.setImageURI(Uri.parse(cover[0]));
        }
        nameTv.setText(item.getName());
        descrTv.setText(item.getSummary());
    }
}