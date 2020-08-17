package com.example.bookportal.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookportal.R;
import com.example.bookportal.domain.Scheme;

import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SchemeRecyclerAdapter extends RecyclerView.Adapter<SchemeRecyclerAdapter.ViewHolder> {
    Context schemeActivity;
    List<Scheme> mSchemeList;


    public SchemeRecyclerAdapter(Context schemeActivity, List<Scheme> mSchemeList) {
        this.schemeActivity =schemeActivity;
        this.mSchemeList = mSchemeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(schemeActivity).inflate(R.layout.single_scheme_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.schemeNameView.setText("Scheme : "+mSchemeList.get(position).getName());

        holder.schemeDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileName = mSchemeList.get(position).getName();
                String fileExtension =".pdf";
                String destinationDirectory ="";
                String url =mSchemeList.get(position).getUrl();

                downloadFile(schemeActivity, fileName, fileExtension,DIRECTORY_DOWNLOADS, url);

            }
        });

    }


    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        Toast.makeText(schemeActivity, "Downloading", Toast.LENGTH_SHORT).show();
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
        return mSchemeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView schemeNameView;
        LinearLayout schemeDownload;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            schemeNameView = itemView.findViewById(R.id.pdfNameView);
            schemeDownload = itemView.findViewById(R.id.schemeLay);
        }
    }


}
