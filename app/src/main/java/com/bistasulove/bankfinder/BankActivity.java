package com.bistasulove.bankfinder;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class BankActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Bank>> {

    /**
     * URL for bankfinder api hosted on heroku
     */
    private static final String BANKFINDER_REQUEST_URL =
            "https://tranquil-spire-92438.herokuapp.com/";

    /**
     * Constant value for the Bank's branch loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BANK_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Adapter for the list of Banks
     */
    private BankAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        //Find a reference to the {@link ListView} in the layout
        ListView bankListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        bankListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of banks as input
        mAdapter = new BankAdapter(this, new ArrayList<Bank>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bankListView.setAdapter(mAdapter);
        
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BANK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<Bank>> onCreateLoader(int i, Bundle bundle) {
        String ifscCodeValue;
        String  cityName;
        String  bankName;
        String isFindByCity = getIntent().getStringExtra("isFindByCity");
        if (isFindByCity.equals("true")){
            cityName = getIntent().getStringExtra("cityName");
            bankName = getIntent().getStringExtra("bankName");

            Uri baseUri = Uri.parse(BANKFINDER_REQUEST_URL+"branches/");
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("format", "json");
            uriBuilder.appendQueryParameter("bank_name" , bankName.trim());
            uriBuilder.appendQueryParameter("city",cityName.trim());
            // Create a new loader for the given URL
            return new BankLoader(this, uriBuilder.toString());
        }
        else {
            ifscCodeValue = getIntent().getStringExtra("ifscCode");

            Uri baseUri = Uri.parse(BANKFINDER_REQUEST_URL+"branch/"+ifscCodeValue.trim());
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("format","json");
            // Create a new loader for the given URL
            return new BankLoader(this, uriBuilder.toString());

        }
    }

    @Override
    public void onLoadFinished(Loader<List<Bank>> loader, List<Bank> banks) {

        // Set empty state text to display "No banks found."
        mEmptyStateTextView.setText(R.string.no_banks);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link BAnk}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (banks != null && !banks.isEmpty()) {
            mAdapter.addAll(banks);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Bank>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
