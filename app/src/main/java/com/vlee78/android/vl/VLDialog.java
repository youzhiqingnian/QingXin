package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.i90s.app.frogs.R;


public class VLDialog {

    public static void showOkCancelDialog(Context context, String title, CharSequence message, String okLabel, String cancelLabel, boolean cancelable, final VLResHandler resHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        if (VLUtils.stringIsNotEmpty(title))
            dialog.setTitle(title);
        if (message != null && message.length() > 0)
            dialog.setMessage(message);
        if (VLUtils.stringIsEmpty(okLabel))
            okLabel = "确定";
        if (VLUtils.stringIsEmpty(cancelLabel))
            cancelLabel = "取消";
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (resHandler != null)
                    resHandler.handlerError(VLResHandler.CANCEL, "");
            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (resHandler != null)
                    resHandler.handlerSuccess();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (resHandler != null)
                    resHandler.handlerError(VLResHandler.CANCEL, "");
            }
        });
        dialog.show();
    }

    public static void showAlertDialog(Context context, String title, CharSequence message, boolean cancelable, final VLResHandler resHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        if (title != null && title.length() > 0)
            dialog.setTitle(title);
        if (message != null && message.length() > 0)
            dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (resHandler != null)
                    resHandler.handlerSuccess();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (resHandler != null)
                    resHandler.handlerSuccess();
            }
        });
        dialog.show();
    }

    public static final ProgressDialog showOkCancelProgressDialog(Context context, String title, CharSequence message, String okLabel, String cancelLabel, boolean cancelable, final VLResHandler resHandler) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (title != null && title.length() > 0)
            progressDialog.setTitle(title);
        if (message != null && message.length() > 0)
            progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (resHandler != null)
                    resHandler.handlerError(VLResHandler.CANCEL, "");
            }
        });
        if (okLabel != null) {
            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, okLabel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (resHandler != null)
                        resHandler.handlerSuccess();
                }
            });
        }
        if (cancelLabel != null) {
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelLabel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (resHandler != null)
                        resHandler.handlerError(VLResHandler.CANCEL, "");
                }
            });
        }
        progressDialog.show();
        return progressDialog;
    }

    @SuppressLint("InflateParams")
    public static final ProgressDialog showProgressDialog(Context context, String title, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.loading_dialog);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(false);
        if (title != null && title.length() > 0)
            progressDialog.setTitle(title);
        if (message != null && message.length() > 0)
            progressDialog.setMessage(message);
        progressDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.group_loading_transparent, null);
        ImageView img = (ImageView) view.findViewById(R.id.loadingImage);
        VLAnimation.rotate(img, 1000);
        progressDialog.setContentView(view);
        return progressDialog;
    }

    public static final void updateProgressDialog(ProgressDialog progressDialog, String title, String message) {
        if (title != null && title.length() > 0)
            progressDialog.setTitle(title);
        if (message != null && message.length() > 0)
            progressDialog.setMessage(message);
    }

    public static final void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.hide();
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
