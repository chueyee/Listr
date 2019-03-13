package com.example.listr;

import android.support.v4.app.Fragment;

public class ItemListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }
}
