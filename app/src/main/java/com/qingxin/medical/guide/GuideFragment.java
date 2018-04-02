package com.qingxin.medical.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingxin.medical.app.homepagetask.HomeActivity;
import com.qingxin.medical.base.QingXinFragment;
import com.vlee78.android.vl.VLUtils;

/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */
public class GuideFragment extends QingXinFragment {

    public static GuideFragment newInstance(int index) {
        GuideFragment guideFragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        guideFragment.setArguments(bundle);
        return guideFragment;
    }

    public GuideFragment() {
    }

    private static final String INDEX = "INDEX";
    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        mImageView.setLayoutParams(VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        return mImageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == mImageView) return;
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int index = getArguments().getInt(INDEX);
        int id = getResources().getIdentifier(String.format("ic_guide%s", index), "mipmap", getActivity().getPackageName());
        mImageView.setImageDrawable(getResources().getDrawable(id));
        if (index == 4) {
            mImageView.setOnClickListener(v -> {
                HomeActivity.startSelf(getActivity(), 0);
                getActivity().finish();
            });
        }
    }
}
