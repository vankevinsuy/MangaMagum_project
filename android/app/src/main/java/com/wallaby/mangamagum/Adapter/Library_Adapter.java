package com.wallaby.mangamagum.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallaby.mangamagum.Model.Book;
import com.wallaby.mangamagum.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Library_Adapter extends RecyclerView.Adapter<Library_Adapter.MyViewHolder> {

    private ArrayList<Book> bookArrayList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public Library_Adapter(ArrayList<Book> bookArrayList, Context mContext) {
        this.bookArrayList = bookArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Library_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view, viewGroup, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Library_Adapter.MyViewHolder myViewHolder, int i) {
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
