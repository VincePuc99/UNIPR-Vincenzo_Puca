package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import java.net.URI;

public class ZoomInPhotoFragment extends DialogFragment{
    public static final String DIALOG_TAG = "ShowFullscreen";
    private ImageView mPhotoFrameFull;
    private static Bitmap mBitmap;
    public static void SendScaledBitmap(Bitmap btm){
        mBitmap=btm;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
            View v= LayoutInflater.from(getActivity()).inflate(R.layout.fullphotoframe,null);
            mPhotoFrameFull = v.findViewById(R.id.PhotoFrameFull);

            mPhotoFrameFull.setImageBitmap(PictureUtils.RotateBitmap(mBitmap,90));

            return new AlertDialog.Builder(getActivity())
                    .setView(v)
                    .setTitle(R.string.zoom_in_title)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }

