package com.dennis.simplealbum.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import com.dennis.simplealbum.R;
import com.dennis.simplealbum.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EntryPreviewer extends Activity {
    private ArrayList<String> selectedImgs;
    private ArrayList<String> selectedImgsPaths;
    private GridView gridView;
    private EditText editText;
    private LayoutInflater inflater;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrypreviewer);
        gridView = findViewById(R.id.id_selected_girdview);
        editText = findViewById(R.id.id_message);
        inflater = LayoutInflater.from(this);
    }

    public void selectFromImageLoader(View v) {
        Intent intent = new Intent();
        intent.setClassName(this, "com.dennis.simplealbum.activity.ImageChooser");
        startActivityForResult(intent, 0);
    }

    public void backToMain(View v) {
        if (selectedImgs != null)
            selectedImgs.clear();
        if (selectedImgsPaths != null)
            selectedImgsPaths.clear();
        Intent back = new Intent();
        back.setClassName(this, "com.dennis.simplealbum.activity.AlbumActivity");
        startActivity(back);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("resultcode", resultCode + "");
        if (resultCode == 0) {
            Bundle bundle = data.getExtras();
            selectedImgs = (ArrayList<String>) bundle.getSerializable("selectedImgs");
            SelectedAdapter adapter = new SelectedAdapter();
            gridView.setAdapter(adapter);
            //改路径格式再存入集合
            selectedImgsPaths = new ArrayList<String>();
            for (String url : selectedImgs) {
                String path = url.substring(6);
                selectedImgsPaths.add(path);
            }
        }
    }

    public void upload(View v) {
        if(selectedImgsPaths != null) {
            //Android 6.0中属于protected permission权限，不仅在manifest中声名，还要书写以下代码。
            if (Build.VERSION.SDK_INT >= 23) {
                int REQUEST_CODE_CONTACT = 101;
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //验证是否许可权限
                for (String str : permissions) {
                    if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限
                        this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                        return;
                    }
                }
            }
            //用okhttp做模拟form表单多部件类型上传
            MultipartBody.Builder multibuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (String path : selectedImgsPaths) {
                File file = new File(path);
                multibuilder
                        .addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
            final String message = editText.getText().toString();

            multibuilder.addFormDataPart("message", message);
            RequestBody requestBody = multibuilder.build();
            Request request = new Request.Builder()
                    .url("http://192.168.1.4:8080/SimpleAlbum/Inuyasha/PhoneUpload.action")
                    .post(requestBody)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String msg = response.body().string();
                    Message message1 = Message.obtain();
                    message1.what = 0;
                    message1.obj = msg;
                    handler.sendMessage(message1);

                    Intent back = new Intent();
                    back.setClassName(EntryPreviewer.this, "com.dennis.simplealbum.activity.AlbumActivity");
                    startActivity(back);
                }
            });

        } else {
            Toast.makeText(this, "没有内容", Toast.LENGTH_SHORT).show();
        }

    }

    private class SelectedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return selectedImgs.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedImgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View linearLayout = inflater.inflate(R.layout.select_demo, parent, false);
            ImageView imageView = linearLayout.findViewById(R.id.imageView);
            ImageLoader.getInstance().displayImage(selectedImgs.get(position), imageView, ImageLoaderUtil.options);

            return imageView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }
}
