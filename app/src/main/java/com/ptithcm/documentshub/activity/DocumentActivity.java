package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.SimilarDocumentAdapter;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.utils.NonScrollListView;
import java.util.ArrayList;
import java.util.List;

public class DocumentActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvToolbarTitle;
    private TextView btnSimilar;
    private TextView tvDocumentTitle;
    private TextView tvPostBy;
    private LinearLayout layoutTags;
    private LinearLayout btnDownload;
    private LinearLayout btnLike;
    private ImageButton btnSave;
    private ImageButton btnHistory;
    private ImageButton btnEditDoc;
    private LinearLayout layoutDescriptionHeader;
    private ImageView ivDescriptionArrow;
    private TextView tvDescription;
    private NonScrollListView lvSimilarDocuments;

    private boolean isDescriptionExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        initViews();
        setupData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        btnSimilar = findViewById(R.id.btn_similar);
        tvDocumentTitle = findViewById(R.id.tv_document_title);
        tvPostBy = findViewById(R.id.tv_post_by);
        layoutTags = findViewById(R.id.layout_tags);
        btnDownload = findViewById(R.id.btn_download);
        btnLike = findViewById(R.id.btn_like);
        btnSave = findViewById(R.id.btn_save);
        btnHistory = findViewById(R.id.btn_history);
        btnEditDoc = findViewById(R.id.btn_edit_doc);
        layoutDescriptionHeader = findViewById(R.id.layout_description_header);
        ivDescriptionArrow = findViewById(R.id.iv_description_arrow);
        tvDescription = findViewById(R.id.tv_description);
        lvSimilarDocuments = findViewById(R.id.lv_similar_documents);
    }

    private void setupData() {
        // Set document info (dummy for now)
        String title = getString(R.string.dummy_document_title);
        tvDocumentTitle.setText(title);
        tvToolbarTitle.setText(title);
        tvPostBy.setText(getString(R.string.label_post_by) + "tule193");

        // Add dummy tags
        String[] tags = {"#oop", "#dotnet", "#java", "#ejb"};
        layoutTags.removeAllViews();
        for (String tag : tags) {
            View tagView = LayoutInflater.from(this).inflate(R.layout.item_tag_pill, layoutTags, false);
            TextView tvTag = tagView.findViewById(R.id.tv_tag_name);
            tvTag.setText(tag);
            layoutTags.addView(tagView);
        }

        // Similar Documents List
        List<Document> similarDocs = new ArrayList<>();
        similarDocs.add(new Document("Design Patterns", "gof_master", "Public", 18, "Software"));
        similarDocs.add(new Document("UML Diagrams", "uml_pro", "Public", 9, "Software"));
        similarDocs.add(new Document("Clean Code", "uncle_bob", "Public", 25, "Software"));

        SimilarDocumentAdapter adapter = new SimilarDocumentAdapter(this, similarDocs);
        lvSimilarDocuments.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        layoutDescriptionHeader.setOnClickListener(v -> {
            isDescriptionExpanded = !isDescriptionExpanded;
            tvDescription.setVisibility(isDescriptionExpanded ? View.VISIBLE : View.GONE);
            ivDescriptionArrow.setRotation(isDescriptionExpanded ? 0 : 180);
        });

        btnDownload.setOnClickListener(v -> {
            // Handle download
        });

        btnLike.setOnClickListener(v -> {
            // Handle like
        });

        btnSave.setOnClickListener(v -> {
            // Handle save
        });

        btnSimilar.setOnClickListener(v -> {
            // Scroll to similar documents
            lvSimilarDocuments.getParent().requestChildFocus(lvSimilarDocuments, lvSimilarDocuments);
        });
    }
}