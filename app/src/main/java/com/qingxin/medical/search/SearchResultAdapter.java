package com.qingxin.medical.search;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingxin.medical.R;

import java.util.List;

/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */

public class SearchResultAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    SearchResultAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_search_result_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView searchResultTv = helper.getView(R.id.searchResultTv);
        searchResultTv.setText(item);
    }
}
