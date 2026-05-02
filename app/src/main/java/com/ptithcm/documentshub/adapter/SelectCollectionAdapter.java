package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Collection;

import java.util.List;

public class SelectCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<Collection> collections;

    public SelectCollectionAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
    }

    public void updateData(List<Collection> newCollections) {
        this.collections = newCollections;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_collection, parent, false);
        }

        Collection collection = collections.get(position);

        TextView tvName = convertView.findViewById(R.id.tv_collection_name);
        TextView tvCount = convertView.findViewById(R.id.tv_item_count);

        tvName.setText(collection.getName());
        tvCount.setText(collection.getTotal_items() + " items");

        return convertView;
    }
}
