package com.dennis.simplealbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.dennis.simplealbum.R;
import com.dennis.simplealbum.layout.NineGridLayout;
import com.dennis.simplealbum.po.entryVo;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private List<entryVo> voList;
    private Context context;
    private LayoutInflater inflater;

    public MyListAdapter(Context context, List<entryVo> voList) {
        this.context = context;
        this.voList = voList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.album_list_item, parent, false);
            holder.membername = convertView.findViewById(R.id.id_entry_item_membername);
            holder.message = convertView.findViewById(R.id.id_entry_item_message);
            holder.nineGrid = convertView.findViewById(R.id.id_entry_item_ninegrid);
            holder.date = convertView.findViewById(R.id.id_entry_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.membername.setText(voList.get(position).getUsername());
        holder.message.setText(voList.get(position).getMessage());
        holder.nineGrid.setUrlList(voList.get(position).getImages());
        holder.date.setText(voList.get(position).getTime().toString());

        return convertView;
    }
    private class ViewHolder {
        private TextView membername;
        private TextView message;
        private NineGridLayout nineGrid;
        private TextView date;
    }
}
