package com.qingxin.medical.app.homepagetask;

import android.support.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import java.util.List;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class CityListAdapter extends BaseQuickAdapter<HomeBean.OpencitysBean, BaseViewHolder> {


    CityListAdapter(@Nullable List<HomeBean.OpencitysBean> data) {
        super(R.layout.layout_city_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.OpencitysBean item) {
        TextView mCityNameTv = helper.getView(R.id.cityNameTv);
        mCityNameTv.setText(item.getName());

    }

}
