package com.ptithcm.documentshub.adapter;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.documentshub.R;

public class PdfPageAdapter extends RecyclerView.Adapter<PdfPageAdapter.PdfViewHolder> {

    private PdfRenderer renderer;

    public PdfPageAdapter(PdfRenderer renderer) {
        this.renderer = renderer;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return renderer != null ? renderer.getPageCount() : 0;
    }

    class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPage;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPage = itemView.findViewById(R.id.iv_pdf_page);
        }

        public void bind(int position) {
            if (renderer == null) return;

            // Render the page
            synchronized (renderer) {
                PdfRenderer.Page page = renderer.openPage(position);
                
                // Adjust bitmap size to match screen width for better quality
                int width = ivPage.getContext().getResources().getDisplayMetrics().widthPixels;
                int height = (int) (width * (float) page.getHeight() / page.getWidth());
                
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                ivPage.setImageBitmap(bitmap);
                
                page.close();
            }
        }
    }
    
    public void closeRenderer() {
        if (renderer != null) {
            renderer.close();
            renderer = null;
        }
    }
}
