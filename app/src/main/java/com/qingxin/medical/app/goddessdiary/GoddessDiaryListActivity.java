package com.qingxin.medical.app.goddessdiary;

import android.os.Bundle;
import android.util.Log;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.indicator.view.RefreshListView;
import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by zhikuo1 on 2018-01-31.
 */
public class GoddessDiaryListActivity extends QingXinActivity implements GoddessDiaryContract.View{

    private GoddessDiaryContract.Presenter mPresenter;

    private GoddessDiary mDiary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_list);

        GoddessDiaryPresenter mGoddessDiaryPresenter = new GoddessDiaryPresenter(this,this);

        mPresenter.getGoddessDiaryList("10","");

    }

    @Override
    public void setPresenter(GoddessDiaryContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.subscribe();
    }

    @Override
    public void onSuccess(GoddessDiary diary) {
        mDiary = diary;
        Log.i("女神日记列表=",mDiary.toString());
        setData();
    }

    private void setData() {

        RefreshListView lv_goddess_diary_list = findViewById(R.id.lv_goddess_diary_list);

    }

    @Override
    public void onError(String result) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
