package com.itcast.slideqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.slideqq.R;
import com.itcast.slideqq.view.weidget.SlideDelte;

import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */

public class MainAdaper extends BaseAdapter {
    private List<String> mData;
    private Context mContext;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MainHolder holder = null;
        if (convertView == null) {
            mContext = parent.getContext();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.slide_delte, parent, false);
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
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/23 RecyclerView可以解决
                Toast.makeText(mContext,"被点了"+mData.get(position) , Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class MainHolder {
        public TextView tv;
        public ImageView iv;
        public TextView top;
        public TextView delete;
    }
}
