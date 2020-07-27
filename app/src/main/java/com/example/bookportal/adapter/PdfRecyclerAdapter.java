package com.example.bookportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookportal.PdfActivity;
import com.example.bookportal.R;
import com.example.bookportal.domain.PdfItems;

import java.util.List;

public class PdfRecyclerAdapter extends RecyclerView.Adapter<PdfRecyclerAdapter.ViewHolder> {

    Context context;
    List<PdfItems> mPDFList;



    public PdfRecyclerAdapter(Context pdfActivity, List<PdfItems> mPDFList) {
        this.context=pdfActivity;
        this.mPDFList = mPDFList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_pdf_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pdfNameView.setText("PDF name : " +mPDFList.get(position).getPdf_name());

    }

    @Override
    public int getItemCount() {
        return mPDFList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pdfNameView;
        Button downloadButton;
        Button deletePdfButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            pdfNameView = itemView.findViewById(R.id.pdfNameView);
            downloadButton= itemView.findViewById(R.id.downloadBtn);
            deletePdfButton= itemView.findViewById(R.id.deletePdfBtn);

        }
    }
}
