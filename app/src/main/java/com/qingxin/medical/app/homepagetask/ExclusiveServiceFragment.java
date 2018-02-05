package com.qingxin.medical.app.homepagetask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingxin.medical.R;
import com.vlee78.android.vl.VLFragment;

/**
 * 歆人专享
 * Date 2018-02-05
 * @author zhikuo1
 */
public class ExclusiveServiceFragment extends VLFragment{

    public ExclusiveServiceFragment() {
    }

    public static ExclusiveServiceFragment newInstance() {
        return new ExclusiveServiceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exclusive_service, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;


    }
}
