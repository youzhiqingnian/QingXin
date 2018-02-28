package com.qingxin.medical.album;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.listener.OnItemClickListener;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 本地相册
 */
public class LocalAlbumActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, LocalAlbumActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView mRecyclerView;
    private LocalImageHelper helper;
    private List<String> folderNames;
    public static final String LOCAL_FOLDER_NAME = "local_folder_name";//跳转到相册页的文件夹名称
    private int mMaxChoose = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album);
        if (getIntent().hasExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM)) {
            mMaxChoose = getIntent().getIntExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM, 3);//默认给3
        }
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, "选择相册");
        QingXinTitleBar.setLeftReturn(titleBar, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = LocalImageHelper.getInstance();
        loadImages();
    }


    private void loadImages() {
        VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_HIGH, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        helper.loadImage();
                        VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                            @Override
                            protected void process(boolean canceled) {
                                if (!isFinishing()) {
                                    initAdapter();
                                }
                            }
                        });
                    }
                }
        );
    }


    public void initAdapter() {
        mRecyclerView.setAdapter(new FolderAdapter(this, helper.getFolderMap(), (localFile, position) -> {
            Intent intent = new Intent(LocalAlbumActivity.this, LocalAlbumDetailActivity.class);
            intent.putExtra(LOCAL_FOLDER_NAME, folderNames.get(position));
            intent.putExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM, mMaxChoose);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        }));
    }

    public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

        private Map<String, List<LocalImageHelper.LocalFile>> folders;
        private Context mContext;
        private OnItemClickListener<LocalImageHelper.LocalFile> mListener;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders, OnItemClickListener<LocalImageHelper.LocalFile> listener) {
            this.folders = folders;
            this.mContext = context;
            this.mListener = listener;
            folderNames = new ArrayList<>();

            for (Object o : folders.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String key = (String) entry.getKey();
                folderNames.add(key);
            }
            //根据文件夹内的图片数量降序显示
            Collections.sort(folderNames, (arg0, arg1) -> {
                Integer num1 = helper.getFolder(arg0).size();
                Integer num2 = helper.getFolder(arg1).size();
                return num2.compareTo(num1);
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_album_folder, parent, false));
        }

        @Override
        public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
            String name = folderNames.get(position);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            holder.titleTv.setText(String.format("%s(%s)", name, files.size()));
            if (files.size() > 0) {
                setController(holder.imageView, files.get(0).getThumbnailUri());
            }
        }

        @Override
        public int getItemCount() {
            return folders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView imageView;
            TextView titleTv;
            LinearLayout albumLl;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.simpleDraweeView);
                titleTv = itemView.findViewById(R.id.titleTv);
                albumLl = itemView.findViewById(R.id.albumLl);
                albumLl.setOnClickListener(v -> {
                    if (null != mListener) {
                        mListener.onItemClick(null, getAdapterPosition());
                    }
                });
            }
        }

        private void setController(@NonNull SimpleDraweeView imageView, @NonNull Uri uri) {
            int size = VLUtils.dip2px(80);
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(ResizeOptions.forSquareSize(size))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(imageView.getController())
                    .setImageRequest(request).build();
            imageView.setController(controller);
        }
    }
}
