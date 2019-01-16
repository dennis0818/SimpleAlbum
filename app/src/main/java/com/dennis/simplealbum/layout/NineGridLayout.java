package com.dennis.simplealbum.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dennis.simplealbum.R;
import com.dennis.simplealbum.po.image;
import com.dennis.simplealbum.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;

public class NineGridLayout extends ViewGroup {

    private static final float DEFAULT_SPACING = 3f;
    private float mSpacing = DEFAULT_SPACING;

    private List<image> mImg_url_list = new ArrayList<image>();

    private Context context;

    private int mTotalWidth;
    private int mSingleWidth;

    private int mRows;
    private int mColumns;

    private boolean mIsFirst = true;


    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在values下attrs.xml文件中定义的可以在布局文件中用的自定义属性，获得由布局文件传入的自定义属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
        mSpacing = typedArray.getDimension(R.styleable.NineGridLayout_spacing, DEFAULT_SPACING);
        typedArray.recycle();

        init(context);
    }

    //*****01***** 初始化组件时判断一次，如果没数据就隐藏
    private void init(Context context) {
        this.context = context;
        if (getListSize(mImg_url_list) == 0) {
            setVisibility(GONE);
        }
    }

    @Override
    //viewgroup的onLayout只调用一次
    //重复使用时也会调用
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Log.i("baodi", "on invoking....");
        mTotalWidth = r - l;
        mSingleWidth = (int) (mTotalWidth - (3 - 1) * mSpacing) / 3;
        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setUrlList(List<image> images) {
        if (getListSize(images) == 0) {
            setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        mImg_url_list.clear();

        mImg_url_list.addAll(images);

        if (!mIsFirst) {
            notifyDataSetChanged();
        }
    }

    //单启线程更新数据
    private void notifyDataSetChanged() {
        //The Runnable will be run on the UI thread
        post(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    private void refresh() {
        removeAllViews();
        int size = getListSize(mImg_url_list);
        if (size == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }

        if (size == 1) {
            final String url = mImg_url_list.get(0).getImg_path();

            LayoutParams params = getLayoutParams();
            params.height = mTotalWidth / 2;
            setLayoutParams(params);


            ImageView imageView = createImageView();
            addView(imageView);
            displayOneImage(url, imageView);
            return;
        }
        generateChildrenArray(size);
        adjustLayoutParams();

        for (int i = 0; i < size; i++) {
            String url = mImg_url_list.get(i).getImg_path();
            ImageView imageView = createImageView();
            layoutImageView(url, imageView, i);
        }
    }

    private void layoutImageView(String url, ImageView imageView, int i) {
        int[] position = findPosition(i);
        int left = (int) ((mSingleWidth + mSpacing) * position[1]);
        int top = (int) ((mSingleWidth + mSpacing) * position[0]);
        int right = left + mSingleWidth;
        int bottom = top + mSingleWidth;

        imageView.layout(left, top, right, bottom);
        addView(imageView);
        ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderUtil.options);
    }

    private int[] findPosition(int x) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == x) {
                    position[0] = i;//row
                    position[1] = j;//column
                    return position;
                }
            }
        }
        return position;
    }

    private void adjustLayoutParams() {
        LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = (int) (mSingleWidth * mRows + mSpacing * (mRows - 1));
        setLayoutParams(layoutParams);
    }

    private void generateChildrenArray(int size) {
        if (size <= 3) {
            mRows = 1;
            mColumns = size;
        } else if (size <= 6) {
            mRows = 2;
            mColumns = 3;
            if (size == 4) {
                mColumns = 2;
            }
        } else {
            mRows = 3;
            mColumns = 3;
        }
    }

    private void displayOneImage(String url, final ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderUtil.options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int height = loadedImage.getHeight();
                int width = loadedImage.getWidth();

                int newH;
                int newW;
                if (height > width) {
                    newW = mTotalWidth / 2;
                    newH = newW * 4 / 3;
                } else {
                    newW = mTotalWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } /*else {
                    newW = mTotalWidth / 2;
                    newH = height * newW / width;
                }*/
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

    private void setOneImageLayoutParams(ImageView imageView, int newW, int newH) {
        LayoutParams params = getLayoutParams();
        params.height = newH;
        setLayoutParams(params);
        imageView.layout(0, 0, newW, newH);
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    private int getListSize(List<image> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
