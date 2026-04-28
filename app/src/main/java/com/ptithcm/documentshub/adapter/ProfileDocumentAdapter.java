package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Document;

import java.util.List;

import android.widget.Toast;

import android.widget.ImageButton;

public class ProfileDocumentAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;

    public ProfileDocumentAdapter(Context context, List<Document> documents) {
        this.context = context;
        this.documents = documents;
    }

    @Override
    public int getCount() {
        return documents.size();
    }

    @Override
    public Object getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_document, parent, false);
        }

        Document document = documents.get(position);

        ImageView ivThumbnail = convertView.findViewById(R.id.iv_doc_thumbnail);
        TextView tvTitle = convertView.findViewById(R.id.tv_doc_title);
        TextView tvOwner = convertView.findViewById(R.id.tv_doc_owner);
        TextView tvViews = convertView.findViewById(R.id.tv_doc_views);
        TextView tvLikes = convertView.findViewById(R.id.tv_doc_likes);
        ImageButton ivDelete = convertView.findViewById(R.id.iv_delete_doc);

        tvTitle.setText(document.getTitle());
        tvOwner.setText("by " + document.getOwner());
        tvViews.setText(String.valueOf(document.getView_count()));
        tvLikes.setText(String.valueOf(document.getLike_count()));

        ivDelete.setOnClickListener(v -> {
            Toast.makeText(context, "đã xóa tài liệu có id=" + document.getId(), Toast.LENGTH_SHORT).show();
        });

        // In real app, use Glide to load document.getFile_thumbnail_url() into ivThumbnail

        return convertView;
    }
}
