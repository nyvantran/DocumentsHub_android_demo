package com.ptithcm.documentshub.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.SimilarDocumentAdapter;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.utils.NonScrollListView;
import com.ptithcm.documentshub.viewmodel.DocumentViewModel;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvToolbarTitle;
    private TextView btnSimilar;
    private TextView tvDocumentTitle;
    private TextView tvPostBy;
    private LinearLayout layoutTags;
    private MaterialButton btnDownload;
    private MaterialButton btnLike;
    private ImageButton btnSave;
    private ImageButton btnReport;
    private ImageButton btnEditDoc;
    private WebView wvPdfPreview;
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
        setupWebView();

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
        btnReport = findViewById(R.id.btn_report);
        btnEditDoc = findViewById(R.id.btn_edit_doc);
        wvPdfPreview = findViewById(R.id.wv_pdf_preview);
        layoutDescriptionHeader = findViewById(R.id.layout_description_header);
        ivDescriptionArrow = findViewById(R.id.iv_description_arrow);
        tvDescription = findViewById(R.id.tv_description);
        lvSimilarDocuments = findViewById(R.id.lv_similar_documents);

        similarAdapter = new SimilarDocumentAdapter(this, new ArrayList<>());
        lvSimilarDocuments.setAdapter(similarAdapter);
    }

    private void setupWebView() {
        if (wvPdfPreview != null) {
            wvPdfPreview.getSettings().setJavaScriptEnabled(true);
            wvPdfPreview.getSettings().setAllowFileAccess(true);
            wvPdfPreview.getSettings().setDomStorageEnabled(true);
            wvPdfPreview.getSettings().setSupportZoom(true);
            wvPdfPreview.getSettings().setBuiltInZoomControls(true);
            wvPdfPreview.getSettings().setDisplayZoomControls(false);
            wvPdfPreview.setWebViewClient(new WebViewClient());
        }
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
        tvPostBy.setText(getString(R.string.label_post_by) + (document.getOwner() != null ? document.getOwner() : "Anonymous"));
        tvDescription.setText(document.getDesc());

        // Hiển thị PDF qua Google Drive Viewer trong WebView (Khôi phục giải pháp cũ)
        if (document.getFile_preview_url() != null && !document.getFile_preview_url().isEmpty()) {
            String rawUrl = document.getFile_preview_url().replace("localhost", "10.0.2.2");
            String googleDocsUrl = "https://docs.google.com/viewer?url=" + rawUrl + "&embedded=true";
            wvPdfPreview.loadUrl(googleDocsUrl);
        }

        // Cập nhật Tags
        if (document.getTags() != null) {
            layoutTags.removeAllViews();
            for (String tag : document.getTags()) {
                View tagView = LayoutInflater.from(this).inflate(R.layout.item_tag_pill, layoutTags, false);
                TextView tvTag = tagView.findViewById(R.id.tv_tag_name);
                tvTag.setText("#" + tag);
                layoutTags.addView(tagView);
            }
        }

        // Cập nhật MaterialButtons
        btnDownload.setText(String.valueOf(document.getDownload_count()));
        btnLike.setText(String.valueOf(document.getLike_count()));

        if (document.getLiked()) {
            btnLike.setEnabled(false);
            btnLike.setAlpha(0.5f);
        } else {
            btnLike.setEnabled(true);
            btnLike.setAlpha(1.0f);
        }
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

        btnDownload.setOnClickListener(v -> {
            Document doc = viewModel.getDocument().getValue();
            if (doc != null && doc.getFile_preview_url() != null) {
                String downloadUrl = doc.getFile_preview_url().replace("localhost", "10.0.2.2");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                startActivity(intent);
            }
        });
    }
}
