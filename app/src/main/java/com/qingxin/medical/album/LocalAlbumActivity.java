package com.qingxin.medical.album;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 本地相册
 */
public class LocalAlbumActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context){
        Intent intent = new Intent(context,LocalAlbumActivity.class);
        context.startActivity(intent);
    }

    private ListView mListView;
    private LocalImageHelper helper;
    private List<String> folderNames;
    public static final String LOCAL_FOLDER_NAME = "local_folder_name";//跳转到相册页的文件夹名称
    private boolean isDestroy;
    private int mMaxChoose = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album);
        if (getIntent().hasExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM)) {
            mMaxChoose = getIntent().getIntExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM, 3);//默认给3
        }
        isDestroy = false;
        mListView = findViewById(R.id.local_album_list);
        VLTitleBar mLocalAlbumTitleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(mLocalAlbumTitleBar, "选择相册");
        QingXinTitleBar.setLeftReturn(mLocalAlbumTitleBar, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = LocalImageHelper.getInstance();
        mListView.setAdapter(null);
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
                                if (!isDestroy) {
                                    initAdapter();
                                    mListView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
        );
    }


    public void initAdapter() {
        mListView.setAdapter(new FolderAdapter(this, helper.getFolderMap()));
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(LocalAlbumActivity.this, LocalAlbumDetailActivity.class);
            intent.putExtra(LOCAL_FOLDER_NAME, folderNames.get(i));
            /**----------------add by qingshan--------------**/
            intent.putExtra(AlbumUtils.MAX_CHOOSE_PIC_NUM, mMaxChoose);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });
    }

    @SuppressWarnings("rawtypes")
    public class FolderAdapter extends BaseAdapter {
        Map<String, List<LocalImageHelper.LocalFile>> folders;
        Context context;
        Display display;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders) {
            this.folders = folders;
            this.context = context;
            folderNames = new ArrayList<>();
            if (display == null) {
                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                display = windowManager.getDefaultDisplay();
            }

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
        public int getCount() {
            return folders.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_album_folder, parent, false);
                viewHolder.imageView = convertView.findViewById(R.id.simpleDraweeView);
                viewHolder.titleTv = convertView.findViewById(R.id.titleTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = folderNames.get(position);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            viewHolder.titleTv.setText(String.format("%s(%s)", name, files.size()));
            if (files.size() > 0) {
                String uri = files.get(0).getOriginalUri();
                viewHolder.imageView.setImageURI(Uri.parse(uri));
            }
            return convertView;
        }

        private class ViewHolder {
            SimpleDraweeView imageView;
            TextView titleTv;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
