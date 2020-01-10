package com.dc.myapplication.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dc.myapplication.R;
import com.dc.myapplication.Utils.PictureUtils;

public class PhotoFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_PHOTO_PATH = "com.dc.android.photo";
    private ImageView mImageView;

    static PhotoFragment newInstance(String image_path) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PHOTO_PATH, image_path);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_photo);
        mImageView = dialog.findViewById(R.id.show_photo);
        String path = getArguments().getString(EXTRA_PHOTO_PATH);
        Bitmap bitmap = PictureUtils.getScaledBitmap(path, getActivity());
        mImageView.setImageBitmap(bitmap);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog ;
    }
}
