package com.example.bookportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookportal.R;
import com.example.bookportal.domain.Items;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    Context applicationContext;
    List<Items> mItemList;
    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemList) {
        this.applicationContext = applicationContext;
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mItemList.get(position).getTitle());
        Glide.with(applicationContext).load(mItemList.get(position).getImg_url()).into(holder.mItemImage);

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView mItemImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage=itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.title);
        }
    }
}
