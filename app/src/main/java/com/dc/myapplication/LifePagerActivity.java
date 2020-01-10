package com.dc.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dc.myapplication.fragment.LifeFragment;
import com.dc.myapplication.model.Life;
import com.dc.myapplication.model.LifeLab;

import java.util.List;
import java.util.UUID;

public class LifePagerActivity extends AppCompatActivity {

    private List<Life> mLives;
    private ViewPager mViewPager;
    private Button mGoFirst;
    private Button mGoEnd;
    private static final String EXTRA_CRIME_ID =
            "com.dc.android.life_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, LifePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = findViewById(R.id.crime_view_pager);
        mLives = LifeLab.getsLifeLab(this).getLives();
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mGoFirst = findViewById(R.id.frist_button);
        mGoFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mGoEnd = findViewById(R.id.end_button);
        mGoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mLives.size()-1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0 )
                    mGoFirst.setEnabled(false);
                else
                    mGoFirst.setEnabled(true);

                if (position == mLives.size()-1)
                    mGoEnd.setEnabled(false);
                else
                    mGoEnd.setEnabled(true);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {

                Life life = mLives.get(position);
                return LifeFragment.newInstance(life.getId());

            }
            @Override
            public int getCount() {
                return mLives.size();
            }
        });

        for (int i = 0; i < mLives.size(); i++) {
            if (mLives.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
