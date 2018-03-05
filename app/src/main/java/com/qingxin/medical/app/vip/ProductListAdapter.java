package com.qingxin.medical.app.vip;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.ProductBean;
import java.util.List;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class ProductListAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {

    private int mType = 0;

    private ProductCallbackListener btnCallBackListener;

    public interface ProductCallbackListener{
        void onProductButtonClick(int position, String id);
    }

    public void setBtnCallBackListener(ProductCallbackListener btnCallBackListener) {
        this.btnCallBackListener = btnCallBackListener;
    }

    public ProductListAdapter(@Nullable List<ProductBean> data) {
        super(R.layout.layout_vip_item, data);
    }

    public ProductListAdapter(@Nullable List<ProductBean> data, int type) {
        super(R.layout.layout_my_appointment_item, data);
        mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBean item) {
        SimpleDraweeView mCoverSdv = helper.getView(R.id.coverSdv);
        TextView mNameTv = helper.getView(R.id.nameTv);
        TextView mHospitalNameTv = helper.getView(R.id.hospitalNameTv);
        TextView mPriceTv = helper.getView(R.id.priceTv);
        TextView mOldpriceTv = helper.getView(R.id.oldpriceTv);
        TextView mOrderCountTv = helper.getView(R.id.orderCountTv);
        TextView mContactUsTv;
        if(mType == 1 || mType == 2){
            mContactUsTv = helper.getView(R.id.contactUsTv);
            if(mType == 2){
                mContactUsTv.setText(mContext.getResources().getString(R.string.cancel_collection));
            }
            mContactUsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btnCallBackListener != null){
                        btnCallBackListener.onProductButtonClick(helper.getAdapterPosition(), item.getId());
                    }
                }
            });
        }

        mCoverSdv.setImageURI(Uri.parse(item.getCover()));
        mNameTv.setText(item.getName());
        mHospitalNameTv.setText(item.getHospital());
        mPriceTv.setText(String.valueOf(item.getPrice()));
        mOldpriceTv.setText(String.format("¥%s", item.getOld_price()));
        mOrderCountTv.setText(String.format("%s次预约", item.getOrder()));
    }
}
