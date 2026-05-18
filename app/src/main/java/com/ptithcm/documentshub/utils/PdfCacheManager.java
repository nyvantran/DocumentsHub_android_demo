package com.ptithcm.documentshub.utils;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class PdfCacheManager {
    private static final String TAG = "PdfCacheManager";
    private static final int MAX_CACHE_SIZE = 5;
    private final Context context;

    public PdfCacheManager(Context context) {
        this.context = context;
    }

    public interface PdfDownloadListener {
        void onDownloadSuccess(File file);
        void onDownloadFailure(Exception e);
    }

    public void getPdfFile(String documentId, String pdfUrl, PdfDownloadListener listener) {
        new Thread(() -> {
            try {
                File cacheDir = new File(context.getCacheDir(), "pdf_cache");
                if (!cacheDir.exists()) cacheDir.mkdirs();

                File pdfFile = new File(cacheDir, documentId + ".pdf");

                if (pdfFile.exists()) {
                    // Update last modified time to keep it in cache (LRU)
                    pdfFile.setLastModified(System.currentTimeMillis());
                    listener.onDownloadSuccess(pdfFile);
                    return;
                }

                // Check cache size before downloading
                cleanCacheIfNecessary(cacheDir);

                // Download file
                Log.d(TAG, "Downloading PDF from: " + pdfUrl);
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    listener.onDownloadFailure(new Exception("Server returned HTTP " + connection.getResponseCode()));
                    return;
                }

                try (InputStream input = connection.getInputStream();
                     FileOutputStream output = new FileOutputStream(pdfFile)) {
                    byte[] data = new byte[4096];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                }

                listener.onDownloadSuccess(pdfFile);

            } catch (Exception e) {
                Log.e(TAG, "Error downloading PDF", e);
                listener.onDownloadFailure(e);
            }
        }).start();
    }

    private void cleanCacheIfNecessary(File cacheDir) {
        File[] files = cacheDir.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files != null && files.length >= MAX_CACHE_SIZE) {
            // Sort by last modified time (oldest first)
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            
            // Delete the oldest file
            if (files[0].delete()) {
                Log.d(TAG, "Deleted oldest cache file: " + files[0].getName());
            }
        }
    }

    public static PdfRenderer getRenderer(File file) throws Exception {
        ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        return new PdfRenderer(fd);
    }
}
