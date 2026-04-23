package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.SimilarDocumentAdapter;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.utils.NonScrollListView;
import com.ptithcm.documentshub.viewmodel.DocumentViewModel;
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
    private DocumentViewModel viewModel;
    private SimilarDocumentAdapter similarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        initViews();
        setupViewModel();
        setupListeners();
        
        // Giả sử nhận ID từ Intent (tạm thời để cứng "1" để test)
        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        if (documentId == null) documentId = "1";
        viewModel.fetchDocumentDetail(documentId);
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
        
        // Khởi tạo adapter trống
        similarAdapter = new SimilarDocumentAdapter(this, new ArrayList<>());
        lvSimilarDocuments.setAdapter(similarAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DocumentViewModel.class);
        
        viewModel.getDocument().observe(this, this::updateUI);
        
        viewModel.getSimilarDocuments().observe(this, documents -> {
            if (documents != null) {
                similarAdapter.updateData(documents);
            }
        });
    }

    private void updateUI(Document document) {
        if (document == null) return;

        tvDocumentTitle.setText(document.getTitle());
        tvToolbarTitle.setText(document.getTitle());
        tvPostBy.setText(getString(R.string.label_post_by) + (document.getAuthor() != null ? document.getAuthor() : "Anonymous"));
        tvDescription.setText(document.getDescription());

        // Cập nhật Tags (Dummy tags nếu model chưa có field tags)
        String[] tags = {"#oop", "#dotnet", "#java", "#ejb"};
        layoutTags.removeAllViews();
        for (String tag : tags) {
            View tagView = LayoutInflater.from(this).inflate(R.layout.item_tag_pill, layoutTags, false);
            TextView tvTag = tagView.findViewById(R.id.tv_tag_name);
            tvTag.setText(tag);
            layoutTags.addView(tagView);
        }
        
        // Cập nhật số lượng download, like nếu cần
        TextView tvDownloadCount = btnDownload.findViewById(R.id.tv_download_count);
        if (tvDownloadCount != null) tvDownloadCount.setText(String.valueOf(document.getDownloads()));
        
        TextView tvLikeCount = btnLike.findViewById(R.id.tv_like_count);
        if (tvLikeCount != null) tvLikeCount.setText(String.valueOf(document.getLikes()));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        layoutDescriptionHeader.setOnClickListener(v -> {
            isDescriptionExpanded = !isDescriptionExpanded;
            tvDescription.setVisibility(isDescriptionExpanded ? View.VISIBLE : View.GONE);
            ivDescriptionArrow.setRotation(isDescriptionExpanded ? 0 : 180);
        });

        btnSimilar.setOnClickListener(v -> {
            lvSimilarDocuments.getParent().requestChildFocus(lvSimilarDocuments, lvSimilarDocuments);
        });
    }
}