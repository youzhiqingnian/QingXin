package com.qingxin.medical.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */
public class SearchActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
