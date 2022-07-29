package com.mediumsitompul.maps_query_odp_onsite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class Maps_ProgressBarMarker {
    public Context context;
    ProgressDialog progressDialog;
    public int progressStatus = 0;
    public android.os.Handler handler = new Handler();
    public Maps_ProgressBarMarker(Context ctx){

        progressDialog = new ProgressDialog(ctx);
    }

    public void showProgressDialog(String title,String info){
        progressDialog.setTitle(title);
        progressDialog.setMessage(info);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(progressStatus < 100){
//                    try {
//                        Thread.sleep(200);
//                        progressStatus += 5;
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.setProgress(progressStatus);
//                        }
//                    });
//                }
//                hideProgressDialog();
//            }
//        }).start();

    }
    public void hideProgressDialog(){
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
}

