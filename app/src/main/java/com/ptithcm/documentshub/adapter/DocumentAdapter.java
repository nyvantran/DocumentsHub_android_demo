package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Document;

import java.util.List;

public class DocumentAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;

    public DocumentAdapter(Context context, List<Document> documents) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        }

        Document document = documents.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tv_doc_title);
        TextView tvAuthor = convertView.findViewById(R.id.tv_doc_author);
        TextView tvTag = convertView.findViewById(R.id.tv_doc_tag);
        TextView tvPages = convertView.findViewById(R.id.tv_doc_pages);

        tvTitle.setText(document.getTitle());
        tvAuthor.setText(document.getAuthor() + " • " + document.getVisibility());
        tvTag.setText(document.getCategory());
        tvPages.setText(document.getPages() + " pages");

        return convertView;
    }
}