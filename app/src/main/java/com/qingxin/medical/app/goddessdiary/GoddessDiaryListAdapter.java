package com.qingxin.medical.app.goddessdiary;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLUtils;

import java.util.List;
/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */

public class GoddessDiaryListAdapter extends BaseQuickAdapter<DiaryItemBean, BaseViewHolder> {

    private int mType = 0;

    private DeleteDiaryListener deleteDiaryListener;

    private EditDiaryListener editDiaryListener;

    public interface DeleteDiaryListener{
        void deleteDiary(String id);
    }

    public interface EditDiaryListener{
        void editDiary(DiaryItemBean item);
    }

    public void setDeleteDiaryListener(DeleteDiaryListener deleteDiaryListener) {
        this.deleteDiaryListener = deleteDiaryListener;
    }

    public void setEditDiaryListener(EditDiaryListener editDiaryListener) {
        this.editDiaryListener = editDiaryListener;
    }

    public GoddessDiaryListAdapter(@Nullable List<DiaryItemBean> data) {
        super(R.layout.layout_home_goddess_diary_item, data);
    }

    public GoddessDiaryListAdapter(@Nullable List<DiaryItemBean> data, int type) {
        super(R.layout.layout_my_diary_item, data);
        mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiaryItemBean item) {
        SimpleDraweeView authorHeadSdv;
        SimpleDraweeView beforeCoverSdv = helper.getView(R.id.beforeCoverSdv);
        SimpleDraweeView afterCoverSdv = helper.getView(R.id.afterCoverSdv);
        TextView contentTv = helper.getView(R.id.diaryContentTv);
        TextView tagTv = helper.getView(R.id.diaryTagTv);
        TextView authorName, scanCountTv, collectionCountTv,deleteTv,editTv;
        if (mType == 0) {
            authorHeadSdv = helper.getView(R.id.authoerHeadSdv);
            authorName = helper.getView(R.id.authorName);
            scanCountTv = helper.getView(R.id.scanCountTv);
            collectionCountTv = helper.getView(R.id.collectionCountTv);

            if (null != item.getMem()) {
                authorHeadSdv.setImageURI(Uri.parse(item.getMem().getCover()));
                authorName.setText(item.getMem().getName());
            }
            scanCountTv.setText(String.valueOf(item.getVisit_num()));
            collectionCountTv.setText(String.valueOf(item.getCollect_num()));
        }else{
            deleteTv = helper.getView(R.id.deleteTv);
            editTv = helper.getView(R.id.editTv);

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDiaryListener.deleteDiary(item.getId());
                }
            });
            editTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDiaryListener.editDiary(item);
                }
            });

        }

        if(!VLUtils.stringIsEmpty(item.getOper_before_photo())){
            beforeCoverSdv.setImageURI(Uri.parse(item.getOper_before_photo()));
        }
        if(!VLUtils.stringIsEmpty(item.getOper_after_photo())){
            afterCoverSdv.setImageURI(Uri.parse(item.getOper_after_photo()));
        }
        contentTv.setText(item.getSummary());

        tagTv.setText(item.getTags());
    }
}
