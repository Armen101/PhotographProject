package com.example.student.userphotograph.utilityes;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Context c;
    private ArrayList<Uri> uriList;
    private StorageReference ref;
    private int position;

    public MyAdapter(StorageReference ref, ArrayList<Uri> movies) {
        //this.c = c;
        this.uriList = movies;
        this.ref = ref;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_grid_layout, parent, false);
        return new MyHolder(v);
    }
    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        this.position = position;
        //PicassoClient.downloadImage(c,uriList.get(position),holder.img);


    }
    @Override
    public int getItemCount() {
        return uriList == null?0:uriList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;

        MyHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.temp_img_gallery);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ref.child(position + " experiment name").putFile(uriList.get(position)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            img.setImageURI(uriList.get(position));
                            Toast.makeText(c, "successful adding", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }

}