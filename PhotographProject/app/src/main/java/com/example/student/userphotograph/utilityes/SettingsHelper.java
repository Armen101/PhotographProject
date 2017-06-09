package com.example.student.userphotograph.utilityes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class SettingsHelper {

   public static final int REQUEST_AVATAR_ACTION_PICK = 100;
   public static final int REQUEST_GALLERY_ACTION_PICK = 101;


    public void actionPic(Activity activity, Boolean isAvatar) {

        Intent actionPick = new Intent(Intent.ACTION_PICK);
        actionPick.setType("image/*");
        if (isAvatar) activity.startActivityForResult(actionPick, REQUEST_AVATAR_ACTION_PICK);
        else activity.startActivityForResult(actionPick, REQUEST_GALLERY_ACTION_PICK);

    }

    public Bitmap downloadImageAndSetGallery(StorageReference ref) {
        final Bitmap[] bmp = {null};

        ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
               bmp[0] = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

         return bmp[0];
    }

}
