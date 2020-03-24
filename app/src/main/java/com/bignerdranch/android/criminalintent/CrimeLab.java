package com.bignerdranch.android.criminalintent;
import android.content.Context;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    // s prefix, convention used to indicate the variable is static
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    private Hashtable<UUID, Crime> mLookupTable;

    // returns an instance if it already doesn't exist
    public static CrimeLab get(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    // private constructor
    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        mLookupTable = new Hashtable<>(100);
        // populate the list with 100 boring crimes
        for(int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            crime.setPosition(i);
            mCrimes.add(crime);
            mLookupTable.put(crime.getId(), crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        if(mLookupTable.containsKey(id)) {
            return mLookupTable.get(id);
        }
        return null;
    }
}
