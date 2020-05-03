package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private List<Crime> mCrimes;
    private final static String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private static final String TAG = "CrimePagerActivity";
    private Button mFirstButton;
    private Button mLastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = findViewById(R.id.crime_view_pager);
        mFirstButton = findViewById(R.id.first_button);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mLastButton = findViewById(R.id.last_button);
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);
            }
        });

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStateAdapter(fragmentManager, getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0) {
                    mFirstButton.setEnabled(false);
                }
                else if (position == mCrimes.size() - 1) {
                    mLastButton.setEnabled(false);
                }
                else {
                    mFirstButton.setEnabled(true);
                    mLastButton.setEnabled(true);
                }
                Log.d(TAG, "createFragment: Running");
                return CrimeFragment.newInstance(mCrimes.get(position).getId());
            }

            @Override
            public int getItemCount() {
                return mCrimes.size();
            }
        });

        for(int i = 0; i < mCrimes.size(); i++) {
            Log.d(TAG, "onCreate: for loop");
            if(mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        Log.d(TAG, "onCreate: end of onCreate");
    }

    public static Intent newIntent(Context packageContext, UUID id) {
        Intent data = new Intent(packageContext, CrimePagerActivity.class);
        data.putExtra(EXTRA_CRIME_ID, id);
        return data;
    }
}
