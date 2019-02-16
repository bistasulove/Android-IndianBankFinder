package com.bistasulove.bankfinder;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of banks by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BankLoader extends AsyncTaskLoader<List<Bank>> {
    /** Tag for log messages */
    private static final String LOG_TAG = BankLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public BankLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Bank> loadInBackground() {
        if (mUrl == null)
            return null;
        // Perform the network request, parse the response, and extract a list of banks.
        List<Bank> banks = QueryUtils.fetchBankData(mUrl);
        return  banks;
    }
}
