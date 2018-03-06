package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.qingxin.medical.album.AlbumAdapter;
import com.qingxin.medical.album.AlbumItemData;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishResult;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.upload.UploadResult;
import com.qingxin.medical.widget.indicator.CommonNavigator;
import com.qingxin.medical.widget.indicator.CommonNavigatorAdapter;
import com.qingxin.medical.widget.indicator.IPagerIndicator;
import com.qingxin.medical.widget.indicator.IPagerTitleView;
import com.qingxin.medical.widget.indicator.LinePagerIndicator;
import com.qingxin.medical.widget.indicator.MagicIndicator;
import com.qingxin.medical.widget.indicator.SimplePagerTitleView;
import com.qingxin.medical.widget.indicator.ViewPagerHelper;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */

public class MineDataFragment extends QingXinFragment implements QingXinBroadCastReceiver.OnReceiverCallbackListener, MineDataContract.View {

    private View mRootView;

    private AppBarLayout mAppbar;

    private SimpleDraweeView mUserHeadSdv;
    private ImageView mDefaultHeadIv;

    private List<TextView> mCountTextViewList = new ArrayList();

    private QingXinBroadCastReceiver mReceiver;

    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

    private Bitmap mBeforePhoto;
    private String mBeforePhotoPath;
    private DiaryPublishParams mDiaryPublishParams;
    private MineDataPresenter mPresenter;

    public MineDataFragment() {
    }

    public static MineDataFragment newInstance() {
        return new MineDataFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;
        mRootView = getView();

        initView();
        initBroadcastReceiver();

    }

    private void initView() {

        mUserHeadSdv = mRootView.findViewById(R.id.userHeadSdv);
        mUserHeadSdv.setImageURI(Uri.parse("http://qingxin-assets.awesomes.cn/app/bc4e8580-216b-11e8-b2dd-cb5a2df58a45.jpg"));
        mDefaultHeadIv = mRootView.findViewById(R.id.defaultHeadIv);
        TextView userNicknameTv = mRootView.findViewById(R.id.userNicknameTv);

        if (QingXinApplication.getInstance().getLoginUser() != null) {
            if (!VLUtils.stringIsEmpty(QingXinApplication.getInstance().getLoginUser().getCover())) {
                mDefaultHeadIv.setVisibility(View.GONE);
                mUserHeadSdv.setImageURI(Uri.parse(QingXinApplication.getInstance().getLoginUser().getCover()));
            }
            userNicknameTv.setText(QingXinApplication.getInstance().getLoginUser().getName());
        }

        mPresenter = new MineDataPresenter(this);
        mDiaryPublishParams = new DiaryPublishParams();


        MagicIndicator indicator = mRootView.findViewById(R.id.magicIndicator);
        final VLFragment[] fragments = new VLFragment[]{MyBookedProductListFragment.newInstance(), MyPublishedDiaryListFragment.newInstance(), MyCollectedTabListFragment.newInstance()};
        final String[] titles = new String[]{getResources().getString(R.string.appointment_count), getResources().getString(R.string.diary_count), getResources().getString(R.string.collection_count)};
        QingXinAdapter adapter = new QingXinAdapter(getChildFragmentManager(), fragments, titles);
        final ViewPager viewPager = mRootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        CommonNavigator navigator = new CommonNavigator(getActivity());
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setTextNormalColor(0xffb2b3b4);
                titleView.setTextSelectedColor(0xff3bc5e8);
                titleView.setText(titles[index]);
                titleView.setTextSize(16);
                mCountTextViewList.add(titleView);
                if (QingXinApplication.getInstance().getLoginSession() != null && mCountTextViewList.size() == 3) {
                    Log.i("进去之后填充数量了吗", "填充了哟，三种数量");
                    setCountRefresh();
                }
                titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator lineIndicator = new LinePagerIndicator(context);
                lineIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                lineIndicator.setLineHeight(VLUtils.dip2px(2));
                lineIndicator.setRoundRadius(VLUtils.dip2px(2));
                lineIndicator.setColors(0xff3bc5e8);
                lineIndicator.setYOffset(VLUtils.dip2px(5));
                return lineIndicator;
            }
        });
        indicator.setNavigator(navigator);
        ViewPagerHelper.bind(indicator, viewPager);

        mUserHeadSdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoPopupWindow().show(viewPager);
            }
        });


        mAppbar = mRootView.findViewById(R.id.appbar);
        RelativeLayout titleBarRl = mRootView.findViewById(R.id.titleBarRl);
        TextView topTitleNameTv = mRootView.findViewById(R.id.topTitleNameTv);
        ImageView settingIv = mRootView.findViewById(R.id.settingIv);


        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int toolbarHeight = appBarLayout.getTotalScrollRange();
            int dy = Math.abs(verticalOffset);
            if (dy <= toolbarHeight) {
                float scale = (float) dy / toolbarHeight;
                float alpha = scale * 255;
                titleBarRl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));

                if (dy >= toolbarHeight / 2) {
                    settingIv.setImageResource(R.mipmap.gray_setting_logo);
                    topTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                } else {

                    settingIv.setImageResource(R.mipmap.white_setting_logo);
                    topTitleNameTv.setTextColor(getActivity().getResources().getColor(R.color.white));

                    if (dy >= toolbarHeight / 4) {
                        topTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                    }
                }
            }
        });
    }

    private QingXinLocalPhotoPopupWindow getPhotoPopupWindow() {
        return CommonDialogFactory.createLoadLocalPhotoPopupWindow(getActivity(), true, new VLAsyncHandler<QingXinLocalPhotoPopupWindow.LoadPhotoResult>(null, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (!succeed) return;
                QingXinLocalPhotoPopupWindow.LoadPhotoResult result = getParam();
                if (null == result) return;
                mBeforePhoto = result.getCutResult();
                mBeforePhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
                mDiaryPublishParams.setBeforeFile(new File(mBeforePhotoPath));
                mPresenter.headUpload(mDiaryPublishParams);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        mReceiver = new QingXinBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(REFRESH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        mReceiver.setReceiverListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (intent.getBooleanExtra("refresh", false)) {
            setCountRefresh();
        }
    }


    private void setCountRefresh() {
        String bookCount = QingXinApplication.getInstance().getLoginSession().getMem().getBook_amount() + "";
        String diaryCount = QingXinApplication.getInstance().getLoginSession().getMem().getDiary_amount() + "";
        String collectCount = QingXinApplication.getInstance().getLoginSession().getMem().getCollect_amount() + "";

        if (!VLUtils.stringIsEmpty(bookCount)) {
            mCountTextViewList.get(0).setText("预约 · " + bookCount);
        }
        if (!VLUtils.stringIsEmpty(diaryCount)) {
            mCountTextViewList.get(1).setText("日记 · " + diaryCount);

        }
        if (!VLUtils.stringIsEmpty(collectCount)) {
            mCountTextViewList.get(2).setText("收藏 · " + collectCount);
        }
    }

    @Override
    public void setPresenter(MineDataContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(DiaryPublishResult diaryPublishResult) {

    }

    @Override
    public void onSuccess(UploadResult uploadResultBean) {
        Log.i("上传头像成功了",uploadResultBean.toString());
        if (uploadResultBean != null && !VLUtils.stringIsEmpty(uploadResultBean.getUrl())) {
            mDefaultHeadIv.setVisibility(View.GONE);
            Log.i("上传头像成功了","真的成功了");
            showToast("上传成功");
            mUserHeadSdv.setImageURI(Uri.parse(uploadResultBean.getUrl()));
        }
    }

    @Override
    public void onError(String result) {

    }
}
