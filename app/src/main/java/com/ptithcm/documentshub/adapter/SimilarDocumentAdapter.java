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

public class SimilarDocumentAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;

    public SimilarDocumentAdapter(Context context, List<Document> documents) {
        this.context = context;
        this.documents = documents;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_similar_document, parent, false);
        }

        Document document = documents.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvSubtitle = convertView.findViewById(R.id.tv_subtitle);

        tvTitle.setText(document.getTitle());
        tvSubtitle.setText(document.getAuthor() + " • " + document.getPages() + " pages");

        return convertView;
    }
}