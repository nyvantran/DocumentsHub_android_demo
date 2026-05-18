package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Document;

import java.util.List;

public class ProfileDocumentAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Document document);
    }

    public interface OnItemClickListener {
        void onItemClick(Document document);
    }

    public ProfileDocumentAdapter(Context context, List<Document> documents, OnDeleteClickListener deleteListener, OnItemClickListener itemListener) {
        this.context = context;
        this.documents = documents;
        this.deleteClickListener = deleteListener;
        this.itemClickListener = itemListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_document, parent, false);
        }

        Document document = documents.get(position);

        ImageView ivThumbnail = convertView.findViewById(R.id.iv_doc_thumbnail);
        TextView tvTitle = convertView.findViewById(R.id.tv_doc_title);
        TextView tvOwner = convertView.findViewById(R.id.tv_doc_owner);
        TextView tvViews = convertView.findViewById(R.id.tv_doc_views);
        TextView tvLikes = convertView.findViewById(R.id.tv_doc_likes);
        ImageButton ivDelete = convertView.findViewById(R.id.btn_delete_doc);

        tvTitle.setText(document.getTitle());
        tvOwner.setText("by " + document.getOwner());
        tvViews.setText(String.valueOf(document.getView_count()));
        tvLikes.setText(String.valueOf(document.getLike_count()));

        ivDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(document);
            }
        });

        convertView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(document);
            }
        });

        Glide.with(context)
                .load(document.getFile_thumbnail_url())
//                .placeholder(R.drawable.ic_placeholder) // Hình ảnh tạm thời khi đang tải
//                .error(R.drawable.ic_error) // Hình ảnh hiển thị khi tải thất bại
                .centerCrop()
                .into(ivThumbnail);

        return convertView;
    }
}
