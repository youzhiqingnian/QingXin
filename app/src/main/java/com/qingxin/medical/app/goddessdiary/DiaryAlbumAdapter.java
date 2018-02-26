package com.qingxin.medical.app.goddessdiary;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingxin.medical.R;

import java.util.List;

/**
 * Date 2018/2/26
 *
 * @author zhikuo
 */

public class DiaryAlbumAdapter extends BaseQuickAdapter {

    public DiaryAlbumAdapter(@Nullable List data) {
        super(R.layout.adapter_diary_album, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
