package com.liy.today.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.laiyifen.lyfframework.R;


/**
 * Created by Administrator on 2015/5/16.
 */
public class ProgressDialogUtils {
    private static Dialog dialog;
    private static ProgressDialogUtils mInstance;

    public static ProgressDialogUtils getInstance(){
        if(mInstance == null){
            mInstance = new ProgressDialogUtils();
        }
        return mInstance;
    }


    public void showDialog(Context context, String text) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.progressDialog);
            dialog = builder.create();
            dialog.setCancelable(true);
            if (null != dialog && !dialog.isShowing()) {
                dialog.show();
                dialog.getWindow().setContentView(R.layout.loading_view);
                dialog.findViewById(R.id.loadviewLinear).setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();

            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        }

////        try {
//        if (dialog == null) {
//            dialog = new ProgressDialog(context);
//            if (!TextUtils.isEmpty(text)) {
//                dialog.setMessage(text);
//            } else {
//                dialog.getWindow().setContentView(R.layout.loading_view);
//                dialog.setMessage("加载中。。。。");
//            }
//            dialog.setCancelable(false);
//            dialog.show();
//        } else {
//            if (!dialog.isShowing()) {
//                dialog.show();
//            }
//        }
//        } catch (Exception ex) {
//
//        }


    }

    public  void cancleDialog() {

        try {
            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
