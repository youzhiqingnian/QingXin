package com.qingxin.medical.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.ExclusiveServiceFragment;
import com.qingxin.medical.base.QingXinActivity;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */
public class FragmentToActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context, @NonNull String className) {
        Intent intent = new Intent(context, FragmentToActivity.class);
        intent.putExtra(CLASS_NAME, className);
        context.startActivity(intent);
    }

    private static final String CLASS_NAME = "CLASS_NAME";
    public static final String EXCLUSIVE_SERVICE_FRAGMENT = "EXCLUSIVE_SERVICE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
//        String className = getIntent().getStringExtra(CLASS_NAME);
//        try {
//            VLFragment fragment = (VLFragment) Class.forName(className).newInstance();
//            if (fragment != null && fragment instanceof ExclusiveServiceFragment) {
//                getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
        String className = getIntent().getStringExtra(CLASS_NAME);
        if (EXCLUSIVE_SERVICE_FRAGMENT.equals(className)){
            ExclusiveServiceFragment fragment = ExclusiveServiceFragment.newInstance(true);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
