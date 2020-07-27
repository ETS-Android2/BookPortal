package com.example.bookportal.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookportal.DetailActivity;
import com.example.bookportal.GlobalData;
import com.example.bookportal.R;
import com.example.bookportal.domain.Items;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    //private OnItemClickListener mListener;

    Context applicationContext;
    List<Items> mItemList;
    Boolean search;



    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemList, Boolean search) {
        this.applicationContext = applicationContext;
        this.mItemList = mItemList;
        this.search = search;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText("Title : "+mItemList.get(position).getBook());
        holder.author.setText("Author : "+mItemList.get(position).getAuthor());
        Glide.with(applicationContext).load(mItemList.get(position).getImg_url()).into(holder.mItemImage);
        if(!search){

        Glide.with(applicationContext).load(mItemList.get(position).getImg_url()).into(holder.mItemImage);


        }
        else
        {
            holder.mItemImage.setVisibility(View.GONE);
        }




//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(applicationContext,DetailActivity.class);
//                applicationContext.startActivity(intent);
//            }
//        });
//
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(applicationContext, ""+mItemList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail", mItemList.get(position));
                applicationContext.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    //public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;

        private ImageView mItemImage;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage = itemView.findViewById(R.id.item_image);
            author = itemView.findViewById(R.id.author);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);


//            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            if(mListener !=null){
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION){
//                    mListener.onItemClick(position);
//
//                }
//
//            }
//
//        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Your Action");
//            MenuItem doWhatever = menu.add(Menu.NONE,1,1,"Do Whatever");
//            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
//
//            doWhatever.setOnMenuItemClickListener(this);
//            delete.setOnMenuItemClickListener( this);
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//
//            if(mListener !=null){
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION){
//                    switch (item.getItemId()){
//                        case 1:
//                            mListener.onWhatEverClick(position);
//                            return true;
//                        case 2:
//                            mListener.onDeleteClick(position);
//                            return true;
//                    }
//
//                }
//
//            }
//            return false;
//        }
//    }
//    public interface OnItemClickListener{
//        void onItemClick(int position);
//        void  onWhatEverClick(int position);
//        void onDeleteClick(int position);
//
//    }
//
//    public void SetOnItemClickListener(OnItemClickListener listener){
//        mListener = listener;
//
//    }
    }
}