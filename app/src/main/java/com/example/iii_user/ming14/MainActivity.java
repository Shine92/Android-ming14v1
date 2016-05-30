package com.example.iii_user.ming14;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private File sdroot,approot;
    private ProgressDialog progressDialog;
    private UIHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new UIHandler();
        textView = (TextView)findViewById(R.id.textView);

        sdroot = Environment.getExternalStorageDirectory(); //傳回sd卡位置
        approot= new File(sdroot,"Android/data/"+getPackageName()+"/"); //創資料夾

        if(!approot.exists()){
            approot.mkdirs();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("DownLoad...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void test1(View view){
        progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                http1("http://bahamut.com.tw/");
            }
        }.start();
    }
    public void test2(View view){
        new Thread(){
            @Override
            public void run() {

                getYahoo();

            }
        }.start();
    }
    public void test3(View view){
        new Thread(){
            @Override
            public void run() {
                dotest3();
            }
        }.start();
    }
    private void dotest3(){
        try {
            MultipartUtility multipartUtility = new MultipartUtility("http://10.2.24.135/Lab101/Lab10/mb1.php","UTF-8");
            List<String> ret =  multipartUtility.finish();
            Log.i("ming","line："+ret.size()); //回傳頁面原始碼
        }catch (Exception e){

        }
    }
    private void getYahoo(){
        try {
            MultipartUtility multipartUtility = new MultipartUtility("https://tw.yahoo.com","UTF-8");

          List<String> ret = multipartUtility.finish(); //不適合抓圖片,因為是字串
            for(String line : ret){
                Log.i("ming",line);
            }
        } catch (Exception e) {

        }
    }
    private void http1(String urlString){
        try {
            URL url = new URL("http://pdfmyurl.com/?url=" +urlString); //get
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect(); //連線
            FileOutputStream fout = new FileOutputStream(new File(approot,"ming.pdf"));
            byte[] buf = new byte[4096];int len;
            InputStream in = connection.getInputStream(); //灌進來
            while((len = in.read(buf))!=-1){
                fout.write(buf,0,len);
            }

            in.close();

            fout.flush();
            fout.close();
            Log.i("ming","OK");
        }catch (Exception e){
            Log.i("ming",e.toString());
        }
        handler.sendEmptyMessage(0);

    }
    private class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(progressDialog.isShowing()){
                progressDialog.dismiss(); //有在顯示就把它關掉
            }
        }
    }
}
