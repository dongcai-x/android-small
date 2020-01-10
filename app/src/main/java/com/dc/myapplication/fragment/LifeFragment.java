package com.dc.myapplication.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dc.myapplication.R;
import com.dc.myapplication.Utils.PictureUtils;
import com.dc.myapplication.model.Life;
import com.dc.myapplication.model.LifeLab;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LifeFragment extends Fragment {

    private Life mLife;
    private Life temp;
    private Button mDateButton;
    private Button mTimeButton;
    private LifeLab lifeLab;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private EditText mDescription;
    private EditText mTitle;
    private static final String ARG_CRIME_ID = "life_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO= 2;

    public static LifeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        LifeFragment fragment = new LifeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mLife = LifeLab.getsLifeLab(getActivity()).getLife(crimeId);
        lifeLab = LifeLab.getsLifeLab(getActivity());
        temp = new Life();
        temp.setDate(mLife.getDate());
        setHasOptionsMenu(true);
        mPhotoFile = LifeLab.getsLifeLab(getActivity()).getPhotoFile(mLife);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_life,
                container, false);
        //Description
        mDescription = v.findViewById(R.id.description);
        mDescription.setText(mLife.getDescription());
//        mDescription.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    mLife.setDescription(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        //title
        //title
        mTitle = v.findViewById(R.id.crime_title);
        mTitle.setText(mLife.getTitle());
//        mTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                mLife.setTitle(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        //star
        //star
        CheckBox mStarCheckBox = v.findViewById(R.id.life_star);
        mStarCheckBox.setChecked(mLife.isStar());
        mStarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temp.setStar(isChecked);
            }
        });




        //time
        mDateButton = v.findViewById(R.id.crime_date);
        mTimeButton = v.findViewById(R.id.crime_h);
        updateDate();
        updateTime();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment
                        .newInstance(temp.getDate());
                datePickerFragment.setTargetFragment(LifeFragment.this, REQUEST_DATE);
                datePickerFragment.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment
                        .newInstance(temp.getDate());
                timePickerFragment.setTargetFragment(LifeFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager, DIALOG_TIME);
            }
        });

        ImageButton mPhotoButton = v.findViewById(R.id.life_camera);
        PackageManager packageManager = getActivity().getPackageManager();

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.dc.myapplication", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = v.findViewById(R.id.life_photo);
        updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                PhotoFragment pictureDialogFragment = PhotoFragment.newInstance(mPhotoFile.getPath());
                pictureDialogFragment.setTargetFragment(LifeFragment.this,0);
                pictureDialogFragment.show(manager, DIALOG_PHOTO);
            }

        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.del_crime : {

                mLife.setDescription(mDescription.getText().toString());
                mLife.setTitle(mTitle.getText().toString());
                mLife.setDate(temp.getDate());
                mLife.setStar(temp.isStar());
                lifeLab.updateLife(mLife);
                Toast.makeText(getActivity(), "保存成功~", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).finish();
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {

            assert data != null;
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //mLife.setDate(date);
            temp.setDate(date);
            updateDate();

        } else if (requestCode == REQUEST_TIME) {

            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_H);
            //mLife.setDate(date);
            temp.setDate(date);
            updateTime();
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.dc.myapplication",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateTime() {
        mTimeButton.setText(DateFormat.format("HH:mm", temp.getDate()));
    }

    private void updateDate() {
        mDateButton.setText(temp.getDate().toString());
        mDateButton.setText(DateFormat.format("yyyy MM dd EEE", temp.getDate()));
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LifeLab.getsLifeLab(getActivity())
                .updateLife(mLife);
    }
}
