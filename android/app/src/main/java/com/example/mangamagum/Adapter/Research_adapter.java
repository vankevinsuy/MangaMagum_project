package com.example.mangamagum.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangamagum.Model.Book;
import com.example.mangamagum.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Research_adapter extends RecyclerView.Adapter<Research_adapter.MyViewHolder> {

    private ArrayList<Book> bookArrayList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public Research_adapter(ArrayList<Book> bookArrayList, Context mContext) {
        this.bookArrayList = bookArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Research_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view, viewGroup, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Research_adapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.book_title.setText(bookArrayList.get(i).getName());
        Picasso.with(this.mContext).load(this.bookArrayList.get(i).getCover_link()).into(myViewHolder.img_book_cover);
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView img_book_cover;
        TextView book_title;

        public MyViewHolder(final View itemView, final OnItemClickListener listener){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            img_book_cover = (ImageView)itemView.findViewById(R.id.book_img);
            book_title = (TextView)itemView.findViewById(R.id.book_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

