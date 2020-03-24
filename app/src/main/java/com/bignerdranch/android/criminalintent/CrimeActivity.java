package com.bignerdranch.android.criminalintent;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private final static String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() {
        return CrimeFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID));
    }

    public static Intent newIntent(Context packageContext, UUID CrimeID) {
        Intent data = new Intent(packageContext, CrimeActivity.class);
        data.putExtra(EXTRA_CRIME_ID, CrimeID);
        return data;
    }
}
