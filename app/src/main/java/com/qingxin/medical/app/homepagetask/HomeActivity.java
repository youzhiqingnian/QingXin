package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.mine.MineFragment;
import com.qingxin.medical.mine.login.LoginFragment;
import com.qingxin.medical.update.QingXinAppUpdateMode;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

/**
 * HomeActivity
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class HomeActivity extends QingXinActivity implements LoginFragment.OnLoginSuccessListener {

    public static void startSelf(Context context, int index) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(INDEX, index);
        context.startActivity(intent);
    }

    private VLPagerView mFragmentPager;
    private VLStatedButtonBar mButtonBar;
    private static final String INDEX = "INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        mFragmentPager = findViewById(R.id.mainPager);
        mButtonBar = findViewById(R.id.mainBottomBar);
        mFragmentPager.setOffscreenPageLimit(4);
        //将4个fragment添加至activity中
        VLFragment[] fragments = new VLFragment[]{HomeFragment.newInstance(), WelFareFragment.newInstance(), ExclusiveServiceFragment.newInstance(), MineFragment.newInstance()};
        mFragmentPager.setFragmentPages(getSupportFragmentManager(), fragments);
        //滑动切换界面 false不可滑动
        mFragmentPager.setScrollable(false);
        mButtonBar.setStatedButtonBarDelegate(new MainBottomBarDelegate(this));
        mFragmentPager.setPageChangeListener(position -> mButtonBar.setChecked(position));
        mButtonBar.setChecked(getIntent().getIntExtra(INDEX, 0));
    }

    @Override
    public void loginSuccess() {
        //do nothing
    }

    private class MainBottomBarDelegate implements VLStatedButtonBar.VLStatedButtonBarDelegate {
        private Context mContext;

        MainBottomBarDelegate(Context context) {
            mContext = context;
        }

        private class MainBottomBarButtonDelegate implements VLStatedButtonBar.VLStatedButton.VLStatedButtonDelegate {
            private ImageView mButtonImage;
            private TextView mButtonText;
            private View red;
            private String mText;
            private int mNormalResId;
            private int mCheckedResId;

            MainBottomBarButtonDelegate(String text, int normalResId, int checkedResId) {
                mText = text;
                mNormalResId = normalResId;
                mCheckedResId = checkedResId;
            }

            @Override
            public void onStatedButtonCreated(VLStatedButtonBar.VLStatedButton button, LayoutInflater inflater) {
                View view = inflater.inflate(R.layout.group_main_bottom_bar_button, button);
                mButtonImage = view.findViewById(R.id.mainBottomBarButtonImage);
                mButtonText = view.findViewById(R.id.mainBottomBarButtonText);
                red = view.findViewById(R.id.red);
                mButtonText.setTextSize(11);
                mButtonText.setText(mText);
            }

            @Override
            public void onStatedButtonChanged(VLStatedButtonBar.VLStatedButton button, VLStatedButtonBar.VLStatedButton.VLButtonState buttonState, int userState) {
                //设置底部按钮选中及未选择状态
                if (buttonState == VLStatedButtonBar.VLStatedButton.VLButtonState.StateChecked) {
                    mButtonImage.setImageResource(mCheckedResId);
                    mButtonText.setTextColor(getResources().getColor(R.color.text_color_blue));
                } else {
                    mButtonImage.setImageResource(mNormalResId);
                    mButtonText.setTextColor(getResources().getColor(R.color.text_color_hint));
                }
                if (userState == 1) {
                    // 红点显示
                    red.setVisibility(View.VISIBLE);
                } else if (userState == 2) {
                    // 红点消失
                    red.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onStatedButtonBarCreated(VLStatedButtonBar buttonBar) {
            VLStatedButtonBar.VLStatedButton button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate("首页", R.mipmap.home_tab_uncheck, R.mipmap.home_tab_check));
            button.setPadding(VLUtils.dip2px(8), 0, VLUtils.dip2px(8), 0);
            buttonBar.addStatedButton(button);

            button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate("福利社", R.mipmap.welfare_service_tab_uncheck, R.mipmap.welfare_service_tab_check));
            button.setPadding(VLUtils.dip2px(8), 0, VLUtils.dip2px(8), 0);
            buttonBar.addStatedButton(button);

            button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate("专属服务", R.mipmap.exclusive_service_tab_uncheck, R.mipmap.exclusive_service_tab_check));
            button.setPadding(VLUtils.dip2px(8), 0, VLUtils.dip2px(8), 0);
            buttonBar.addStatedButton(button);

            button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate("我的", R.mipmap.my_tab_uncheck, R.mipmap.my_tab_check));
            button.setPadding(VLUtils.dip2px(8), 0, VLUtils.dip2px(8), 0);
            buttonBar.addStatedButton(button);
        }

        @Override
        public void onStatedButtonBarChanged(VLStatedButtonBar buttonBar, int position) {
            mFragmentPager.gotoPage(position, false);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (VLUtils.isShouldHideInput(getCurrentFocus(), ev)) {
                hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isFirst = true;

    @Override
    public void onBackPressed() {
        if (isFirst) {
            isFirst = false;
            ToastUtils.showToast("再按一次退出应用!");
            VLScheduler.instance.schedule(2500, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    isFirst = true;
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getModel(QingXinAppUpdateMode.class).releaseComposite();
        Process.killProcess(Process.myPid());
    }
}
