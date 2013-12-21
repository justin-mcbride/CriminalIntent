package com.justin.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Justin on 11/23/13.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
