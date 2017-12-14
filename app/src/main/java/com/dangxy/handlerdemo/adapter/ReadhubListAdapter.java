package com.dangxy.handlerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dangxy.handlerdemo.R;
import com.dangxy.handlerdemo.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dangxueyi
 * @description
 * @date 2017/12/14
 */

public class ReadhubListAdapter extends RecyclerView.Adapter<ReadhubListAdapter.ViewHolder> {

    private List<NewsEntity>listEntities = new ArrayList<>();

    public ReadhubListAdapter(List<NewsEntity> listEntities) {
        this.listEntities = listEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.readhub_item_news, null);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.more.setText(listEntities.get(position).getTitle());
        holder.summary.setText(listEntities.get(position).getSummary());
        holder.more.setText(listEntities.get(position).getSiteName());
    }



    @Override
    public int getItemCount() {
        return listEntities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,summary,more;


        public ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            summary = (TextView) convertView.findViewById(R.id.summary);
            more = (TextView) convertView.findViewById(R.id.more);
        }
    }
}
