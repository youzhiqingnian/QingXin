package com.qingxin.medical.album;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLImagePlayView;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;
import java.util.List;


/**
 * 点击相册的详情页
 */
public class LocalAlbumDetailActivity extends QingXinActivity implements VLImagePlayView.OnSingleTapListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private GridView mGridView;
    private TextView title;//标题
    private View pagerContainer;//图片显示部分
    private TextView finish, headerFinish;
    private AlbumViewPager viewpager;//大图显示pager
    private String folder;
    private TextView mCountView;
    private List<LocalImageHelper.LocalFile> currentFolder = null;

    private ImageView mBackView;
    private View headerBar;
    private CheckBox checkBox;
    private LocalImageHelper helper = LocalImageHelper.getInstance();
    private List<LocalImageHelper.LocalFile> checkedItems;

    /**
     * ----------------add by qingshan--------------
     **/
    private int mMaxChoose = 3;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localalbum_detail);

        /**----------------add by qingshan--------------**/
        if (getIntent().hasExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM)) {
            mMaxChoose = getIntent().getIntExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM, 3);//默认给3
        }
        if (!LocalImageHelper.getInstance().isInited()) {
            finish();
            return;
        }
        //registerMessageIds(I90Constants.MSG_ID_SEND_MOMENT_SUCCESS);
        title = findViewById(R.id.album_title);
        finish = findViewById(R.id.album_finish);
        headerFinish = findViewById(R.id.header_finish);
        mGridView = findViewById(R.id.gridview);
        viewpager = findViewById(R.id.albumviewpager);
        pagerContainer = findViewById(R.id.pagerview);
        mCountView = findViewById(R.id.header_bar_photo_count);
        viewpager.setOnPageChangeListener(pageChangeListener);
        viewpager.setOnSingleTapListener(this);
        mBackView = findViewById(R.id.header_bar_photo_back);
        headerBar = findViewById(R.id.album_item_header_bar);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
        mBackView.setOnClickListener(this);
        finish.setOnClickListener(this);
        headerFinish.setOnClickListener(this);
        findViewById(R.id.album_back).setOnClickListener(this);

        folder = getIntent().getExtras().getString(LocalAlbumActivity.LOCAL_FOLDER_NAME);
        loadImges();
        LocalImageHelper.getInstance().setResultOk(false);
    }


    private void loadImges() {
        VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_HIGH, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        helper.initImage();
                        checkedItems = helper.getCheckedItems();
                        final List<LocalImageHelper.LocalFile> folders = helper.getFolder(folder);
                        VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            protected void process(boolean canceled) {
                                if (folders != null) {
                                    currentFolder = folders;
                                    AlbumAdapter adapter = new AlbumAdapter(folders);
                                    title.setText(folder);
                                    mGridView.setAdapter(adapter);
                                    //设置当前选中数量
                                    if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
                                        finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/" + mMaxChoose + ")");
                                        finish.setEnabled(true);
                                        headerFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/" + mMaxChoose + ")");
                                        headerFinish.setEnabled(true);
                                    } else {
                                        finish.setText("完成");
                                        headerFinish.setText("完成");
                                        finish.setTextColor(0x80ffffff);
                                        headerFinish.setTextColor(0x80ffffff);
                                    }
                                }
                            }
                        });
                    }
                }
        );
    }

    private void showViewPager(int index) {
        pagerContainer.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
        findViewById(R.id.album_title_bar).setVisibility(View.GONE);
        viewpager.setAdapter(viewpager.new LocalViewPagerAdapter(currentFolder));
        viewpager.setCurrentItem(index);
        mCountView.setText(String.format("%s/%s", index + 1, currentFolder.size()));
        //第一次载入第一张图时，需要手动修改
        if (index == 0) {
            checkBox.setTag(currentFolder.get(index));
            checkBox.setChecked(checkedItems.contains(currentFolder.get(index)));
        }
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(300);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
    }

    private void hideViewPager() {
        pagerContainer.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
        findViewById(R.id.album_title_bar).setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
        ((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (viewpager.getAdapter() != null) {
                String text = (position + 1) + "/" + viewpager.getAdapter().getCount();
                mCountView.setText(text);
                checkBox.setTag(currentFolder.get(position));
                checkBox.setChecked(checkedItems.contains(currentFolder.get(position)));
            } else {
                mCountView.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onSingleTap() {
        if (headerBar.getVisibility() == View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            headerBar.startAnimation(animation);
            headerBar.setVisibility(View.GONE);
        } else {
            headerBar.setVisibility(View.VISIBLE);
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);
            headerBar.startAnimation(animation);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_bar_photo_back:
                hideViewPager();
                break;
            case R.id.album_finish:
            case R.id.header_finish:
                //TODO
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
                break;
            case R.id.album_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pagerContainer.getVisibility() == View.VISIBLE) {
            hideViewPager();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            if (checkedItems.contains(compoundButton.getTag())) {
                checkedItems.remove(compoundButton.getTag());
            }
        } else {
            if (!checkedItems.contains(compoundButton.getTag())) {
                if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() >= mMaxChoose) {
                    showToast("最多选择" + mMaxChoose + "张图片");
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
            }
        }
        if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
            finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/" + mMaxChoose + ")");
            finish.setEnabled(true);
            headerFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/" + mMaxChoose + ")");
            headerFinish.setEnabled(true);
            finish.setTextColor(0xffffffff);
            headerFinish.setTextColor(0xffffffff);
        } else {
            finish.setText("完成");
            finish.setEnabled(false);
            headerFinish.setText("完成");
            headerFinish.setEnabled(false);
            finish.setTextColor(0x80ffffff);
            headerFinish.setTextColor(0x80ffffff);
        }
    }

    public class AlbumAdapter extends BaseAdapter {
        List<LocalImageHelper.LocalFile> paths;

        AlbumAdapter(List<LocalImageHelper.LocalFile> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public LocalImageHelper.LocalFile getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            int subW = (VLUtils.getScreenWidth(LocalAlbumDetailActivity.this) - VLUtils.dip2px(20)) / 3;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.simple_list_item, null);
                viewHolder.imageView =convertView.findViewById(R.id.imageView);
                viewHolder.checkBox = convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setOnCheckedChangeListener(LocalAlbumDetailActivity.this);
                viewHolder.imageView.getLayoutParams().width = subW;
                viewHolder.imageView.getLayoutParams().height = subW;
                viewHolder.imageView.setBackgroundColor(0xffffffff);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            LocalImageHelper.LocalFile localFile = paths.get(i);
            String contentUri = localFile.getOriginalUri();
            viewHolder.imageView.setImageURI(Uri.parse(contentUri));
            viewHolder.checkBox.setTag(localFile);
            viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
            viewHolder.imageView.setOnClickListener(view -> showViewPager(i));
            return convertView;
        }

        private class ViewHolder {
            SimpleDraweeView imageView;
            CheckBox checkBox;
        }
    }

    public void onMessage(int msgId, Object msgParam) {
        /*if (msgId == I90Constants.MSG_ID_SEND_MOMENT_SUCCESS) {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {

                @Override
                protected void process(boolean canceled) {
                    LocalImageHelper.getInstance().clear();
                    invalidateOptionsMenu();
                }
            });
        }*/
    }
}