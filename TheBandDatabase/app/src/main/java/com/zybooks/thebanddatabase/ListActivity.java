package com.zybooks.thebanddatabase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ListActivity extends AppCompatActivity implements ListFragment.OnBandSelectedListener {

    private static final String KEY_BAND_ID = "bandId";
    private int mBandId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mBandId = -1;

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.list_fragment_container);

        if (fragment == null) {
            fragment = new ListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.list_fragment_container, fragment)
                    .commit();
        }
        // Replace DetailsFragment if state saved when going from portrait to landscape
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_BAND_ID) != 0
                && getResources().getBoolean(R.bool.twoPanes)) {
            mBandId = savedInstanceState.getInt(KEY_BAND_ID);
            Fragment bandFragment = DetailsFragment.newInstance(mBandId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment_container, bandFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state when something is selected
        if (mBandId != -1) {
            outState.putInt(KEY_BAND_ID, mBandId);
        }
    }

    @Override
    public void onBandSelected(int bandId) {

        mBandId = bandId;

        if (findViewById(R.id.details_fragment_container) == null) {
            // Send the band ID of the clicked button to DetailsActivity
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_BAND_ID, bandId);
            startActivity(intent);
        } else {
            // Running on tablet, so replace previous fragment (if one exists) with a new fragment
            Fragment bandFragment = DetailsFragment.newInstance(bandId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment_container, bandFragment)
                    .commit();
        }
    }
}