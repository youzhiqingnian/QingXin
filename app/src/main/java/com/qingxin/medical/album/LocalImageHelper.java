package com.qingxin.medical.album;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.qingxin.medical.base.QingXinApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalImageHelper {

    private static LocalImageHelper instance;

    final List<LocalFile> checkedItems = new ArrayList<>();

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    // 当前选中得图片个数
    private int currentSize;

    // 大图遍历字段
    private static final String[] STORE_IMAGES = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_TAKEN};
    // 小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};

    final List<LocalFile> paths = new ArrayList<>();

    final Map<String, List<LocalFile>> folders = new HashMap<>();

    private LocalImageHelper() {
    }

    public Map<String, List<LocalFile>> getFolderMap() {
        return folders;
    }

    public static LocalImageHelper getInstance() {
        if (instance == null) {
            instance = new LocalImageHelper();
        }
        return instance;
    }

    public boolean isInited() {
        return paths.size() > 0;
    }

    public List<LocalFile> getCheckedItems() {
        return checkedItems;
    }

    private boolean resultOk;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean ok) {
        resultOk = ok;
    }

    public synchronized void loadImage() {
        folders.clear();
        paths.clear();
        clear();
        initImage();
    }

    public synchronized void initImage() {
        if (isInited())
            return;
        // 获取大图的游标
        Cursor cursor = QingXinApplication.getInstance().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 大图URI
                STORE_IMAGES, // 字段
                null, // No where clause
                null, // No where clause
                MediaStore.Images.Media.DATE_ADDED + " DESC"); // 根据时间升序
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);// 大图ID
            String path = cursor.getString(1);// 大图路径
            File file = new File(path);
            // 判断大图是否存在
            if (file.exists()) {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        .buildUpon().appendPath(Integer.toString(id)).build();
                if (null == uri) {
                    continue;
                }

                // 获取目录名
                String folder = file.getParentFile().getName();
                LocalFile localFile = new LocalFile(id);
                localFile.setOriginalUri(uri);
                int degree = cursor.getInt(2);
                if (degree != 0) {
                    degree = degree + 180;
                }
                localFile.setOrientation(360 - degree);

                paths.add(localFile);
                // 判断文件夹是否已经存在
                if (folders.containsKey(folder)) {
                    folders.get(folder).add(localFile);
                } else {
                    List<LocalFile> files = new ArrayList<>();
                    files.add(localFile);
                    folders.put(folder, files);
                }
            }
        }
        folders.put("所有图片", paths);
        cursor.close();
    }

    private String getThumbnail(int id, String path) {
        // 获取大图的缩略图
        Cursor cursor = QingXinApplication.getInstance().getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""}, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                    .buildUpon().appendPath(Integer.toString(thumId)).build()
                    .toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    public List<LocalFile> getFolder(String folder) {
        return folders.get(folder);
    }

    public void clear() {
        checkedItems.clear();
        currentSize = (0);
        String foloder = QingXinApplication.getInstance().getCachePath() + "/PostPicture/";
        File savedir = new File(foloder);
        if (savedir.exists()) {
            deleteFile(savedir);
        }
    }

    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
        }
    }

    public static class LocalFile {
        private Uri originalUri;// 原图URI
        private Uri thumbnailUri;// 缩略图URI
        private int orientation;// 图片旋转角度
        private int mId;

        private Uri getThumbnail(int id) {
            // 获取大图的缩略图
            Cursor cursor = QingXinApplication.getInstance().getContentResolver().query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    THUMBNAIL_STORE_IMAGE,
                    MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                    new String[]{id + ""}, null);
            if (null != cursor && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int thumId = cursor.getInt(0);
                Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                        .buildUpon().appendPath(Integer.toString(thumId)).build();
                cursor.close();
                return uri;
            }
            return null;
        }

        public LocalFile(int id) {
            mId = id;
        }

        public void setThumbnailUri(Uri thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
        }

        public Uri getThumbnailUri() {
            if (thumbnailUri == null) {
                thumbnailUri = getThumbnail(mId);
                if (null == thumbnailUri) {
                    thumbnailUri = originalUri;
                }
            }
            return thumbnailUri;
        }


        public Uri getOriginalUri() {
            return originalUri;
        }

        public void setOriginalUri(Uri originalUri) {
            this.originalUri = originalUri;
        }

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int exifOrientation) {
            orientation = exifOrientation;
        }
    }
}
