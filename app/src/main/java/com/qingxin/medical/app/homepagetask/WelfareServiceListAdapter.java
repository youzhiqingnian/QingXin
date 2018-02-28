package com.qingxin.medical.app.homepagetask;

import android.support.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import java.util.List;
/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class WelfareServiceListAdapter extends BaseQuickAdapter<CoinLogBean, BaseViewHolder> {


    WelfareServiceListAdapter(@Nullable List<CoinLogBean> data) {
        super(R.layout.layout_qingxin_coin_log_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinLogBean item) {
        TextView mCoinProgramTv = helper.getView(R.id.coinProgramTv);
        TextView mDateTimeTv = helper.getView(R.id.dateTimeTv);
        TextView mCoinCountTv = helper.getView(R.id.coinCountTv);
        mCoinProgramTv.setText(item.getUse_alia());

        mDateTimeTv.setText(item.getCreated_at());

        int coinCountColor = mContext.getResources().getColor(R.color.text_color_blue);

        if("-".equals(item.getAction())){
            coinCountColor = mContext.getResources().getColor(R.color.text_color_blue);
        }

        mCoinCountTv.setTextColor(coinCountColor);

        mCoinCountTv.setText(item.getAction()+item.getAmount());

    }

}
