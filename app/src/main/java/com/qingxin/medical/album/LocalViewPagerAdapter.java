package com.qingxin.medical.album;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qingxin.medical.R;
import com.qingxin.medical.fresco.zoomable.ZoomableDraweeView;
import com.vlee78.android.vl.VLUtils;
import java.util.List;

public class LocalViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<LocalImageHelper.LocalFile> paths;//大图地址 如果为网络图片 则为大图url

    LocalViewPagerAdapter(@NonNull Context context, List<LocalImageHelper.LocalFile> paths) {
        this.mContext = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        //注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
        //因为直接加到viewGroup下会导致返回的view为viewGroup
        View imageLayout = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_pager, viewGroup, false);
        viewGroup.addView(imageLayout);
        ZoomableDraweeView zoomableDraweeView = imageLayout.findViewById(R.id.image);
        LocalImageHelper.LocalFile path = paths.get(position);
        VLUtils.setControllerListener(zoomableDraweeView, path.getOriginalUri());
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int arg1, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}