package com.wallaby.mangamagum.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wallaby.mangamagum.R;

import java.util.ArrayList;

public class Page_adapter extends RecyclerView.Adapter<Page_adapter.ViewHolder>{

    private ArrayList<String> List_urls;
    private Context context;
    private int width;
    private int height;

    public Page_adapter(ArrayList<String> list_urls, Context context, int width, int height) {
        this.List_urls = list_urls;
        this.context = context;
        this.width = width;
        this.height = height;
    }



    @NonNull
    @Override
    public Page_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.page_layout, viewGroup,false);

        return new Page_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Page_adapter.ViewHolder viewHolder, int i) {
        String image_url = this.List_urls.get(i);

//        Picasso.with(context).load(image_url).into(viewHolder.imageView);
        Glide
                .with(context)
                .load(image_url).override(this.width,this.height)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.getImageView());

    }

    @Override
    public int getItemCount() {
        return List_urls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.page_content);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
