package com.qingxin.medical.app.goddessdiary;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import java.util.List;

/**
 *
 * Date 2018-02-03
 * @author zhikuo1
 */

public class GoddessDiaryListAdapter extends BaseQuickAdapter<DiaryItemBean, BaseViewHolder> {

    GoddessDiaryListAdapter(@Nullable List<DiaryItemBean> data) {
        super(R.layout.layout_home_goddess_diary_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiaryItemBean item) {
        SimpleDraweeView authorHeadSdv = helper.getView(R.id.authoerHeadSdv);
        SimpleDraweeView beforeCoverSdv = helper.getView(R.id.beforeCoverSdv);
        SimpleDraweeView afterCoverSdv = helper.getView(R.id.afterCoverSdv);
        TextView authorName = helper.getView(R.id.authorName);
        TextView contentTv = helper.getView(R.id.diaryContentTv);
        TextView tagTv = helper.getView(R.id.diaryTagTv);
        TextView scanCountTv = helper.getView(R.id.scanCountTv);
        TextView collectionCountTv = helper.getView(R.id.collectionCountTv);

        if (null != item.getMem()) {
            authorHeadSdv.setImageURI(Uri.parse(item.getMem().getCover()));
            authorName.setText(item.getMem().getName());
        }
        beforeCoverSdv.setImageURI(Uri.parse(item.getOper_before_photo()));
        afterCoverSdv.setImageURI(Uri.parse(item.getOper_after_photo()));
        contentTv.setText(item.getSummary());
        tagTv.setText(item.getTags());
    }
}
