package com.ptithcm.documentshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.activity.DocumentActivity;
import com.ptithcm.documentshub.model.Category;

import java.util.List;

public class CategorySectionAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;

    public CategorySectionAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void updateData(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_section, parent, false);
        }

        Category category = categories.get(position);

        TextView tvCategoryName = convertView.findViewById(R.id.tv_category_name);
        ListView lvTrending = convertView.findViewById(R.id.lv_trending_documents);

        tvCategoryName.setText(category.getName());

        DocumentAdapter documentAdapter = new DocumentAdapter(context, category.getTrendingDocuments());
        lvTrending.setAdapter(documentAdapter);

        // Chuyển sang màn hình chi tiết tài liệu khi nhấn vào một item
        lvTrending.setOnItemClickListener((parent1, view1, position1, id) -> {
            Intent intent = new Intent(context, DocumentActivity.class);
            context.startActivity(intent);
        });

        return convertView;
    }
}