package edu.wallawalla.cs.sukaki.drawit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SaveActivity extends AppCompatActivity implements SaveFragment.OnButtonSelectedListener {

    String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.save_fragment_container);

        if (fragment == null) {
            fragment = new SaveFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.save_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onButtonSelected(String buttonId) {
        // TODO: implement saving
            mState = buttonId;
            finish();
    }
}