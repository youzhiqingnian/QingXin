package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.qingxin.medical.R;
import com.qingxin.medical.app.DataBaseHelper;
import com.qingxin.medical.base.QingXinActivity;

/**
 * 女神日记列表
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class GoddessDiaryListActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, GoddessDiaryListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container, GoddessDiaryListFragment.newInstance(true)).commit();
    }
}
