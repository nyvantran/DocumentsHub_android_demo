package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Document;

import java.util.List;

public class TrashDocumentAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;
    private OnRestoreClickListener restoreClickListener;
//    private OnItemClickListener itemClickListener;

    public interface OnRestoreClickListener {
        void onRestoreClick(Document document);
    }

    public interface OnItemClickListener {
        void onItemClick(Document document);
    }

    public TrashDocumentAdapter(Context context, List<Document> documents, OnRestoreClickListener restoreListener) {
        this.context = context;
        this.documents = documents;
        this.restoreClickListener = restoreListener;
//        this.itemClickListener = itemListener;
    }

    public void updateData(List<Document> newDocuments) {
        this.documents = newDocuments;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trash_document, parent, false);
        }

        Document document = documents.get(position);

        ImageView ivThumbnail = convertView.findViewById(R.id.iv_doc_thumbnail);
        TextView tvTitle = convertView.findViewById(R.id.tv_doc_title);
        TextView tvOwner = convertView.findViewById(R.id.tv_doc_owner);
        TextView tvViews = convertView.findViewById(R.id.tv_doc_views);
        TextView tvLikes = convertView.findViewById(R.id.tv_doc_likes);
        ImageButton btnRestore = convertView.findViewById(R.id.btn_restore);

        tvTitle.setText(document.getTitle());
        tvOwner.setText("by " + document.getOwner());
        tvViews.setText(String.valueOf(document.getView_count()));
        tvLikes.setText(String.valueOf(document.getLike_count()));

        btnRestore.setOnClickListener(v -> {
            if (restoreClickListener != null) {
                restoreClickListener.onRestoreClick(document);
            }
        });
        Glide.with(context)
                .load(document.getFile_thumbnail_url())
//                .placeholder(R.drawable.ic_document_placeholder)
                .centerCrop()
                .into(ivThumbnail);

        return convertView;
    }
}
