package com.qingxin.medical.album;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.decoration.SpaceDecoration;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;

import java.util.List;


/**
 * 点击相册的详情页
 */
public class LocalAlbumDetailActivity extends QingXinActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RecyclerView mRecyclerView;
    private TextView title;//标题
    private View pagerContainer;//图片显示部分
    private TextView finish, headerFinish;
    private ViewPager viewpager;//大图显示pager
    private String folder;
    private TextView mCountView;
    private List<LocalImageHelper.LocalFile> currentFolder = null;

    private CheckBox checkBox;
    private LocalImageHelper helper = LocalImageHelper.getInstance();
    private List<LocalImageHelper.LocalFile> checkedItems;

    /**
     * ----------------add by qingshan--------------
     **/
    private int mMaxChoose = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localalbum_detail);

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
        mRecyclerView = findViewById(R.id.recyclerView);
        viewpager = findViewById(R.id.albumviewpager);
        pagerContainer = findViewById(R.id.pagerview);
        mCountView = findViewById(R.id.header_bar_photo_count);
        viewpager.addOnPageChangeListener(pageChangeListener);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
        finish.setOnClickListener(this);
        headerFinish.setOnClickListener(this);
        findViewById(R.id.album_back).setOnClickListener(this);
        findViewById(R.id.header_bar_photo_back).setOnClickListener(this);
        folder = getIntent().getStringExtra(LocalAlbumActivity.LOCAL_FOLDER_NAME);
        loadImges();
        LocalImageHelper.getInstance().setResultOk(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SpaceDecoration itemDecoration = new SpaceDecoration(VLUtils.dip2px(5));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        mRecyclerView.addItemDecoration(itemDecoration);
    }


    private void loadImges() {
        VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_HIGH, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        helper.initImage();
                        checkedItems = helper.getCheckedItems();
                        final List<LocalImageHelper.LocalFile> folders = helper.getFolder(folder);
                        VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                            @Override
                            protected void process(boolean canceled) {
                                if (folders != null) {
                                    currentFolder = folders;
                                    AlbumAdapter adapter = new AlbumAdapter(LocalAlbumDetailActivity.this, folders);
                                    title.setText(folder);
                                    mRecyclerView.setAdapter(adapter);
                                    //设置当前选中数量
                                    if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
                                        finish.setText(String.format("完成(%s/%s)", checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize(), mMaxChoose));
                                        finish.setEnabled(true);
                                        headerFinish.setText(String.format("完成(%s/%s)", checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize(), mMaxChoose));
                                        headerFinish.setEnabled(true);
                                    } else {
                                        finish.setText("完成");
                                        headerFinish.setText("完成");
                                        finish.setTextColor(0x80464a4c);
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
        mRecyclerView.setVisibility(View.GONE);
        findViewById(R.id.album_title_bar).setVisibility(View.GONE);
        viewpager.setAdapter(new LocalViewPagerAdapter(this, currentFolder));
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
        mRecyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.album_title_bar).setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (viewpager.getAdapter() != null) {
                mCountView.setText(String.format("%s/%s", (position + 1), viewpager.getAdapter().getCount()));
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
                    showToast(String.format("最多选择%s张图片", mMaxChoose));
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
            }
        }
        if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
            finish.setText(String.format("完成(%s/%s)", checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize(), mMaxChoose));
            finish.setEnabled(true);
            headerFinish.setText(String.format("完成(%s/%s)", checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize(), mMaxChoose));
            headerFinish.setEnabled(true);
            finish.setTextColor(0xff464a4c);
            headerFinish.setTextColor(0xffffffff);
        } else {
            finish.setText("完成");
            finish.setEnabled(false);
            headerFinish.setText("完成");
            headerFinish.setEnabled(false);
            finish.setTextColor(0x80464a4c);
            headerFinish.setTextColor(0x80ffffff);
        }
    }

    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
        List<LocalImageHelper.LocalFile> paths;
        private Context mContext;

        AlbumAdapter(@NonNull Context context, List<LocalImageHelper.LocalFile> paths) {
            mContext = context;
            this.paths = paths;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.simple_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LocalImageHelper.LocalFile localFile = paths.get(position);
            setController((VLUtils.getScreenWidth(LocalAlbumDetailActivity.this) - VLUtils.dip2px(20)) / 3, holder.imageView, localFile.getThumbnailUri());
            holder.checkBox.setTag(localFile);
            holder.checkBox.setChecked(checkedItems.contains(localFile));
            holder.imageView.setOnClickListener(view -> showViewPager(position));
        }

        @Override
        public int getItemCount() {
            return paths.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView imageView;
            CheckBox checkBox;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                checkBox = itemView.findViewById(R.id.checkbox);
                int subW = (VLUtils.getScreenWidth(LocalAlbumDetailActivity.this) - VLUtils.dip2px(20)) / 3;
                imageView.getLayoutParams().width = subW;
                imageView.getLayoutParams().height = subW;
                checkBox.setOnCheckedChangeListener(LocalAlbumDetailActivity.this);
                imageView.setBackgroundColor(0xffffffff);
            }
        }

        private void setController(int size, @NonNull SimpleDraweeView imageView, @NonNull Uri uri) {
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(ResizeOptions.forSquareSize(size))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(imageView.getController())
                    .setImageRequest(request).build();
            imageView.setController(controller);
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