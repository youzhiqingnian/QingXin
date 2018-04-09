package com.qingxin.medical.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLActivity.VLActivityResultListener;
import com.vlee78.android.vl.VLAsyncHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * 调用系统相册(单选)
 */
public class QingXinLocalPhotoPopupWindow implements OnClickListener {

    private Context mContext;
    private VLAsyncHandler<LoadPhotoResult> mCallBack;
    public static final String IMAGE_FILE_CUT_NAME = "QingXinTmpImg_cut.jpg";// 裁剪后文件名称
    public static final String IMAGE_FILE_TAKE_NAME = "QingXinTakeTmpImg.jpg";// 拍照后保存的文件名称
    private static final int REQUESTCODE_PICK = 0; // 相册选图标记
    public static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
    public static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private CommonCustomPopupWindow mContentPopupWindow;
    private boolean mStartCutAfterTakePhoto;

    /**
     * 加载图片的方式
     */
    public enum PhotoLoadType {

        /**
         * 拍照
         */
        TAKE_PHOTO,

        /**
         * 拍照后剪裁
         */
        TAKE_PHOTO_CUT,

        /**
         * 本地相册
         */
        LOCAL,

        /**
         * 本地相册加载后剪裁
         */
        LOCAL_CUT,
    }

    /**
     * 加载图片结果
     */
    public class LoadPhotoResult implements Serializable {
        /**
         * 加载方式
         */
        PhotoLoadType mLoadType;
        /**
         * 图片剪裁结果
         */
        Bitmap mCutResult;
        /**
         * 加载的图片地址, 拍照或剪裁或本地加载的最终图片地址
         */
        File mLoadPhotoFile;

        public PhotoLoadType getLoadType() {
            return mLoadType;
        }

        public Bitmap getCutResult() {
            return mCutResult;
        }

        public File getLoadPhotoFile() {
            return mLoadPhotoFile;
        }
    }

    /**
     * 创建加载本地图片及拍照的窗口
     *
     * @param startCutAfterTakePhoto 当拍照后, 是否开启剪裁窗口
     */
    public QingXinLocalPhotoPopupWindow(final Context context, boolean startCutAfterTakePhoto, final VLAsyncHandler<LoadPhotoResult> callBack) {
        this.mContext = context;
        this.mStartCutAfterTakePhoto = startCutAfterTakePhoto;
        this.mCallBack = callBack;
        mContentPopupWindow = new CommonCustomPopupWindow(context, R.layout.group_local_img_popupwindow, R.id.main_get_local_photo_lay, R.style.ChooseAlbumPopupAnimation, new ColorDrawable(0x80000000), this);
        // 判断是否是魅族手机，如果是则显示隐藏的空白布局
        if (TextUtils.equals("Meizu", android.os.Build.BRAND)) {
            mContentPopupWindow.mMenuView.findViewById(R.id.empty_view_get_photo).setVisibility(View.VISIBLE);
        }
        // 返回键关闭当前窗口
        mContentPopupWindow.mMenuView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                mContentPopupWindow.dismiss();
            return false;
        });
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        if (onDismissListener != null && mContentPopupWindow != null)
            mContentPopupWindow.setOnDismissListener(onDismissListener);
    }

    /**
     * 创建加载本地图片及拍照的窗口
     */
    public QingXinLocalPhotoPopupWindow(final Context context, final VLAsyncHandler<LoadPhotoResult> callBack) {
        this(context, true, callBack);
    }

    @Override
    public void onClick(View v) {
        mContentPopupWindow.dismiss();
        switch (v.getId()) {
            case R.id.button_take_photo:
                // 拍照
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_TAKE_NAME)));
                ((VLActivity) mContext).startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                ((VLActivity) mContext).setActivityResultListener(onVLSelectPicResultListener);
                break;
            case R.id.button_open_local_photos:
                // 启动相册
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                ((VLActivity) mContext).startActivityForResult(pickIntent, REQUESTCODE_PICK);
                ((VLActivity) mContext).setActivityResultListener(onVLSelectPicResultListener);
                break;
            case R.id.button_cancel_get_photo:
                // 取消
                if (mCallBack != null) {
                    mCallBack.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResCanceled, null);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 头像——拍照、相册选择结果的回调接口
     */
    private VLActivityResultListener onVLSelectPicResultListener = new VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            LoadPhotoResult result = intent != null ? (LoadPhotoResult) intent.getSerializableExtra("loadPhotoResult") : null;
            result = result == null ? new LoadPhotoResult() : result;
            switch (requestCode) {
                case REQUESTCODE_PICK:// 直接从相册获取
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = intent.getData();
                        result.mLoadType = mStartCutAfterTakePhoto ? PhotoLoadType.LOCAL_CUT : PhotoLoadType.LOCAL;
                        result.mLoadPhotoFile = new File(uri.getPath());
                        if (mStartCutAfterTakePhoto)
                            startPhotoZoom(uri, result);
                        else if (mCallBack != null)
                            mCallBack.handlerSuccess(result);
                    }
                    break;

                case REQUESTCODE_TAKE:// 调用相机拍照
                    if (resultCode == Activity.RESULT_OK) {
                        File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_TAKE_NAME);
                        Uri uri = Uri.fromFile(temp);
                        result.mLoadType = mStartCutAfterTakePhoto ? PhotoLoadType.TAKE_PHOTO_CUT : PhotoLoadType.TAKE_PHOTO;
                        result.mLoadPhotoFile = temp;
                        if (uri != null) {
                            if (mStartCutAfterTakePhoto)
                                startPhotoZoom(uri, result);
                            else if (mCallBack != null)
                                mCallBack.handlerSuccess(result);
                        }
                    }
                    break;

                case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                    if (resultCode == Activity.RESULT_OK) {
                        File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_CUT_NAME);
                        Uri uri = Uri.fromFile(temp);
                        if (uri != null) {
                            try {
                                result.mLoadPhotoFile = temp;
                                result.mCutResult = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(Uri.fromFile(temp)));
                                if (mCallBack != null) {
                                    mCallBack.handlerSuccess(result);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 头像——裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri, LoadPhotoResult result) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("return-data", false);
        result.mLoadPhotoFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_CUT_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(result.mLoadPhotoFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        //intent.putExtra("loadPhotoResult", result);
        ((VLActivity) mContext).setActivityResultListener(onVLSelectPicResultListener);
        ((VLActivity) mContext).startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 显示窗口
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        mContentPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示窗口
     */
    public void show(View parent) {
        mContentPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
