package com.example.student.userphotograph.utilityes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Picture;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
 
    private Context context;
    private List<Picture> pictures;
 
    public MyAdapter(List<Picture> pictures) {
        this.pictures = pictures;
        this.context = context;
    }
 
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images, parent, false);
        return new ViewHolder(rootView);
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = pictures.get(position);
        holder.textViewName.setText(picture.getTitle());
 
        Glide.with(context)
                .load(picture.getImageUri())
                .into(holder.imageView);
    }
 
    @Override
    public int getItemCount() {
        return pictures.size();
    }
 
    class ViewHolder extends RecyclerView.ViewHolder {
 
        public TextView textViewName;
        public ImageView imageView;
 
        public ViewHolder(View itemView) {
            super(itemView);
 
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}