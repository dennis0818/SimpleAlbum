package com.dennis.simplealbum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dennis.simplealbum.R;
import com.dennis.simplealbum.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Set;

public class MyPreviewGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> mImagePaths;
    private String dirPath;

    private Set<String> mSelectedImg;
    private Context context;
    private TextView mSelectedButton;

    public MyPreviewGridAdapter(Context context, List<String> mOrderImgPaths, Set<String> mSelectedImg, TextView mSelectedButton) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mImagePaths = mOrderImgPaths;
        //this.dirPath = dirPath;
        this.mSelectedImg = mSelectedImg;
        this.mSelectedButton = mSelectedButton;
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.preview_grid_item, parent, false);
            holder.mImg = convertView.findViewById(R.id.id_item_image);//怎么不用强转？？ 因为findViewById方法采用了<T> T method(String class)的杠杆模式
            holder.mSelect = convertView.findViewById(R.id.id_item_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mImg.setColorFilter(null);
        holder.mSelect.setImageResource(R.drawable.select_no);

        //use universal-imageloader 拼装成imageloader显示图片的路径格式
        //final String absolutePath = "file:/" + dirPath + "/" + mImagePaths.get(position);
        final String absolutePath = mImagePaths.get(position);

        ImageLoader.getInstance().displayImage(absolutePath, holder.mImg, ImageLoaderUtil.options);
        holder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedImg.size() < 9) {
                    if(! mSelectedImg.contains(absolutePath)) {
                        mSelectedImg.add(absolutePath);
                        holder.mImg.setColorFilter(Color.parseColor("#77000000"));
                        holder.mSelect.setImageResource(R.drawable.select_yes);
                    } else {
                        mSelectedImg.remove(absolutePath);
                        holder.mImg.setColorFilter(null);
                        holder.mSelect.setImageResource(R.drawable.select_no);
                    }
                    mSelectedButton.setText("完成(" + mSelectedImg.size() + "/9)");
                } else {
                    if(mSelectedImg.size() == 9) {
                        mSelectedImg.remove(absolutePath);
                        holder.mImg.setColorFilter(null);
                        holder.mSelect.setImageResource(R.drawable.select_no);
                    }
                    Toast.makeText(context, "DO NOT EXCEED 9", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(mSelectedImg.contains(absolutePath)) {
            holder.mImg.setColorFilter(Color.parseColor("#77000000"));
            holder.mSelect.setImageResource(R.drawable.select_yes);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
        ImageButton mSelect;
    }
}
