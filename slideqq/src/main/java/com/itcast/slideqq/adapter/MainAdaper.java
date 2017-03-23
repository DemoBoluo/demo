package com.itcast.slideqq.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcast.slideqq.R;

import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */

public class MainAdaper extends BaseAdapter {
    private List<String> mData;

    public MainAdaper(List<String> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_delte, parent, false);
            holder = new MainHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.main_item_name);
            holder.iv = (ImageView) convertView.findViewById(R.id.main_item_iv);
            holder.top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.delete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        } else {
            holder = (MainHolder) convertView.getTag();
        }
        holder.tv.setText(mData.get(position));
        switch ((position % 3)) {
            case 0:
                holder.iv.setImageResource(R.mipmap.head_1);
                break;
            case 1:
                holder.iv.setImageResource(R.mipmap.head_2);
                break;
            case 2:
                holder.iv.setImageResource(R.mipmap.head_3);
                break;
        }
        return convertView;
    }

    class MainHolder {
        public TextView tv;
        public ImageView iv;
        public TextView top;
        public TextView delete;
    }
}
