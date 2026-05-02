package com.ptithcm.documentshub.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.PdfPageAdapter;
import com.ptithcm.documentshub.adapter.SelectCollectionAdapter;
import com.ptithcm.documentshub.adapter.SimilarDocumentAdapter;
import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.utils.NonScrollListView;
import com.ptithcm.documentshub.utils.PdfCacheManager;
import com.ptithcm.documentshub.viewmodel.DocumentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView rvPdfPreview;
    private LinearLayout layoutDescriptionHeader;
    private ImageView ivDescriptionArrow;
    private TextView tvDescription;
    private NonScrollListView lvSimilarDocuments;

    private boolean isDescriptionExpanded = true;
    private DocumentViewModel viewModel;
    private SimilarDocumentAdapter similarAdapter;
    private PdfPageAdapter pdfAdapter;
    private PdfCacheManager pdfCacheManager;
    private SelectCollectionAdapter selectCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        pdfCacheManager = new PdfCacheManager(this);
        initViews();
        setupViewModel();
        setupListeners();

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
        rvPdfPreview = findViewById(R.id.rv_pdf_preview);
        layoutDescriptionHeader = findViewById(R.id.layout_description_header);
        ivDescriptionArrow = findViewById(R.id.iv_description_arrow);
        tvDescription = findViewById(R.id.tv_description);
        lvSimilarDocuments = findViewById(R.id.lv_similar_documents);

        rvPdfPreview.setLayoutManager(new LinearLayoutManager(this));

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

        // Observe official download URL
        viewModel.getDownloadUrl().observe(this, url -> {
            android.util.Log.d("DocumentActivity", "Download URL observed: " + url);
            if (url != null && !url.isEmpty()) {
                startDownload(url);
                viewModel.clearDownloadUrl(); // Prevent re-triggering on config change
            }
        });

        viewModel.getMyCollections().observe(this, collections -> {
            if (collections != null && selectCollectionAdapter != null) {
                selectCollectionAdapter.updateData(collections);
            }
        });

        viewModel.getStatusMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownload(String url) {
        android.util.Log.d("DocumentActivity", "Starting download with URL: " + url);
        String downloadUrl = url.replace("localhost", "10.0.2.2");
        Document doc = viewModel.getDocument().getValue();
        String fileName = (doc != null ? doc.getTitle() : "document") + ".pdf";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("Downloading " + fileName);
        request.setDescription("DocumentHub");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(Document document) {
        if (document == null) return;

        tvDocumentTitle.setText(document.getTitle());
        tvToolbarTitle.setText(document.getTitle());
        tvPostBy.setText(getString(R.string.label_post_by) + " " + (document.getOwner() != null ? document.getOwner() : "Anonymous"));
        tvDescription.setText(document.getDesc());

        // Hiển thị PDF bằng PdfRenderer
        if (document.getFile_preview_url() != null && !document.getFile_preview_url().isEmpty()) {
            String rawUrl = document.getFile_preview_url().replace("localhost", "10.0.2.2");
            String docId = String.valueOf(document.getId());

            pdfCacheManager.getPdfFile(docId, rawUrl, new PdfCacheManager.PdfDownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    runOnUiThread(() -> {
                        try {
                            if (pdfAdapter != null) {
                                pdfAdapter.closeRenderer();
                            }
                            PdfRenderer renderer = PdfCacheManager.getRenderer(file);
                            pdfAdapter = new PdfPageAdapter(renderer);
                            rvPdfPreview.setAdapter(pdfAdapter);
                        } catch (Exception e) {
                            Toast.makeText(DocumentActivity.this, "Error rendering PDF", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onDownloadFailure(Exception e) {
                    runOnUiThread(() -> Toast.makeText(DocumentActivity.this, "Failed to load PDF", Toast.LENGTH_SHORT).show());
                }
            });
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
            btnLike.setAlpha(0.5f);
        } else {
            btnLike.setAlpha(1.0f);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnLike.setOnClickListener(v -> viewModel.toggleLike());

        btnSave.setOnClickListener(v -> showSelectCollectionDialog());

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
            if (doc != null) {
//                Toast.makeText(this, String.valueOf(doc.getId()), Toast.LENGTH_SHORT).show();
                viewModel.fetchDownloadUrl(String.valueOf(doc.getId()));
            }
        });
    }

    private void showSelectCollectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_collection, null);
        builder.setView(dialogView);

        android.widget.ListView lvCollections = dialogView.findViewById(R.id.lv_collections);
        List<Collection> initialData = viewModel.getMyCollections().getValue();
        if (initialData == null) initialData = new ArrayList<>();

        selectCollectionAdapter = new SelectCollectionAdapter(this, initialData);
        lvCollections.setAdapter(selectCollectionAdapter);

        AlertDialog dialog = builder.create();

        lvCollections.setOnItemClickListener((parent, view, position, id) -> {
            Collection selectedCollection = (Collection) selectCollectionAdapter.getItem(position);
            viewModel.addItemToCollection(selectedCollection.getId());
            dialog.dismiss();
        });

        dialog.show();
        viewModel.fetchMyCollections();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfAdapter != null) {
            pdfAdapter.closeRenderer();
        }
    }
}
