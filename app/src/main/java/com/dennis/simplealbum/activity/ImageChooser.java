package com.dennis.simplealbum.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dennis.simplealbum.R;
import com.dennis.simplealbum.adapter.MyPreviewGridAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageChooser extends Activity {
    private GridView mGridView;
    private List<String> mImages;

    private ArrayList<String> mOrderImageList_forImageLoader;



    private RelativeLayout mBottom;
    private TextView mDirName;
    private TextView mDirCount;
    private TextView mSelectedButton;


    private File mCurrentDir;
    private int mMAXCount;

    private Set<String> mSelectedImg;

    private ProgressDialog progressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            data2View();
        }
    };

    private void data2View() {
        if (mOrderImageList_forImageLoader != null) {
            //把已选图片集合new出来，并传给adapter
            mSelectedImg = new HashSet<String>();
            MyPreviewGridAdapter adapter = new MyPreviewGridAdapter(this, mOrderImageList_forImageLoader,  mSelectedImg, mSelectedButton);
            mGridView.setAdapter(adapter);
            mDirCount.setText(mOrderImageList_forImageLoader.size() + "");
            mDirName.setText("所有图片");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagechooser);

        initViews();
        initDatas();
        initEvents();

    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.id_girdview);
        mBottom = (RelativeLayout) findViewById(R.id.id_bottomlayout);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);
        mSelectedButton = (TextView) findViewById(R.id.id_selected);
    }


    private void initDatas() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_LONG).show();
            return;
        }

       progressDialog = ProgressDialog.show(this, null, "Loading...");

        new Thread() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    //验证是否许可权限
                    for (String str : permissions) {
                        if (ImageChooser.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            ImageChooser.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                            return;
                        }
                    }
                }
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = ImageChooser.this.getContentResolver();
                Cursor cursor = resolver.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                mOrderImageList_forImageLoader = new ArrayList<String>();

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    File parentFile = new File(path).getParentFile();

                    if (parentFile == null) {
                        continue;
                    } else {
                        int x = path.lastIndexOf("/");
                        String img_name = path.substring(x);
                        String img_path_forImageLoader = "file:/" + parentFile.getAbsolutePath() + img_name;
                        mOrderImageList_forImageLoader.add(img_path_forImageLoader);
                        Log.i("image", img_path_forImageLoader);
                    }
                }
                cursor.close();

                mHandler.sendEmptyMessage(0x110);
            }
        }.start();
    }

    private void initEvents() {
        //屏幕底部的“完成(x/9)”按钮触发事件
        mSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> list = new ArrayList<String>();
                list.addAll(mSelectedImg);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedImgs", list);

                intent.putExtras(bundle);

                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EntryPreviewer.class);
        startActivity(intent);
    }

}
