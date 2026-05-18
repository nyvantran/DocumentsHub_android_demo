package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;

import java.util.List;

import android.widget.ImageButton;
import android.widget.Toast;

public class ProfileCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<Collection> collections;

    private OnCollectionClickListener collectionClickListener;
    private OnItemClickListener itemClickListener;

    public interface OnCollectionClickListener {
        void onCollectionClick(Collection collection);
    }

    public interface OnItemClickListener {
        void onItemClick(Collection collection);
    }

    public ProfileCollectionAdapter(Context context, List<Collection> collections, OnCollectionClickListener collectionClickListener, OnItemClickListener itemClickListener) {
        this.context = context;
        this.collections = collections;
        this.collectionClickListener = collectionClickListener;
        this.itemClickListener = itemClickListener;
    }

    public void updateData(List<Collection> newCollections) {
        collections.clear();
        collections.addAll(newCollections);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_collection, parent, false);
        }

        Collection collection = collections.get(position);

        ImageView ivThumbnail = convertView.findViewById(R.id.iv_collection_thumbnail);
        TextView tvName = convertView.findViewById(R.id.tv_collection_name);
        TextView tvCount = convertView.findViewById(R.id.tv_item_count);
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete_collection);

        tvName.setText(collection.getName());
        tvCount.setText(collection.getTotal_items() + " items");

        btnDelete.setOnClickListener(v -> {
            if (collectionClickListener != null) {
                collectionClickListener.onCollectionClick(collection);
            }
        });

        convertView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(collection);
            }
        });

        // In real app, use Glide to load collection.getThumbnailUrl() into ivThumbnail

        return convertView;
    }
}

