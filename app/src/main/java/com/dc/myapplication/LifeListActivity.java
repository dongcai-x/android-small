package com.dc.myapplication;

import androidx.fragment.app.Fragment;

import com.dc.myapplication.fragment.LifeListFragment;

public class LifeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LifeListFragment();
    }
}
