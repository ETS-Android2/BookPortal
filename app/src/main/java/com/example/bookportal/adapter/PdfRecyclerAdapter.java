package com.example.bookportal.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookportal.DeletePDFActivity;
import com.example.bookportal.GlobalData;
import com.example.bookportal.PdfActivity;
import com.example.bookportal.R;
import com.example.bookportal.domain.PdfItems;

import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.pdfNameView.setText("PDF name : " +mPDFList.get(position).getPdf_name());
        String userPdfId = mPDFList.get(position).getUserID();

        holder.deletePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeletePDFActivity.class);
                intent.putExtra("detail", mPDFList.get(position));
                context.startActivity(intent);

            }
        });


        
        
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = mPDFList.get(position).getPdf_name();
                String fileExtension =".pdf";
                String destinationDirectory ="";
                String url =mPDFList.get(position).getPdf_url();

                downloadFile(context, fileName, fileExtension,DIRECTORY_DOWNLOADS, url);

            }
        });

    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);

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
