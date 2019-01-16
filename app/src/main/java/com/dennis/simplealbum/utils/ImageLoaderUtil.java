package com.dennis.simplealbum.utils;

import android.graphics.Bitmap;

import com.dennis.simplealbum.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ImageLoaderUtil {
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.no_pic)
            .cacheInMemory(true)
            //.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(20))
            .bitmapConfig(Bitmap.Config.RGB_565).build();
}
