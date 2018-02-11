package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
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

    private Context mContext;

    public ExclusiveServiceListAdapter(@Nullable List<ServiceBean> data,Context context) {
        super(R.layout.layout_service_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        SimpleDraweeView headSdv = helper.getView(R.id.headSdv);
        TextView nameTv = helper.getView(R.id.nameTv);
        TextView goodAtTv = helper.getView(R.id.goodAtTv);
        TextView consultTv = helper.getView(R.id.consultTv);

        headSdv.setImageURI(Uri.parse(item.getThumbnail()));
        nameTv.setText(item.getName());
        goodAtTv.setText(mContext.getResources().getString(R.string.good_at) + item.getSummary());

        consultTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
