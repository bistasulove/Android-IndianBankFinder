package com.bistasulove.bankfinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper methods related to requesting and receiving Bank Details data from API endpoint.
 */
public final class QueryUtils {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the API and return a list of {@link Bank} objects.
     */
    public static List<Bank> fetchBankData(String requestUrl) {
        //Create URL Object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i("response", jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Bank> banks;
        if (requestUrl.contains("branches")){
            //Extract relevant fields from the JSON Response and create a list of {@link Bank}s
            banks = extractCityFeatureFromJson(jsonResponse);
        }
        else{
            banks = extractIfscFeatureFromJson(jsonResponse);
        }
        // Return the list of {@link Bank}s
        return banks;
    }

    /**
     * Return a list of {@link Bank} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Bank> extractCityFeatureFromJson(String bankJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bankJSON))
            return null;

        // Create an empty ArrayList that we can start adding Banks to
        List<Bank> banks = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONArray from the SAMPLE_JSON_RESPONSE string
            Log.i("HI", "working");
            Log.i("JSON", bankJSON);
            JSONArray bankArray = new JSONArray(bankJSON);
            Log.i("first", bankArray.toString());

            for (int i = 0; i < bankArray.length(); i++) {
                // Extract the JSONObject
                JSONObject baseJsonResponse = bankArray.getJSONObject(i);
                Log.i("second", baseJsonResponse.toString());
                JSONObject currentBank = baseJsonResponse;

                String ifsc = currentBank.getString("ifsc");
                String branch = currentBank.getString("branch");
                String city = currentBank.getString("city");
                String district = currentBank.getString("district");
                String state = currentBank.getString("state");
                String address = currentBank.getString("address");
                JSONObject name_obj = currentBank.getJSONObject("bank");
                String name = name_obj.getString("name");
                Log.i("Branch : ", branch);

                String bank_detail = "Name : " + name + "\nBranch : " + branch + "\nCity : " + city +
                        "\nDistrict : " + district + "\nState" + state + "\nIFSC : " + ifsc + "\nAddress : " + address;

                // Create a new {@link BAnk} object with the details from JSON response
                Bank bank = new Bank(bank_detail);

                // Add the new {@link BAnk} to the list of BAnks.
                banks.add(bank);

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Bank JSON results", e);
        }

        // Return the list of banks
        return banks;
    }



    public static List<Bank> extractIfscFeatureFromJson(String bankJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bankJSON))
            return null;

        // Create an empty ArrayList that we can start adding Banks to
        List<Bank> banks = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONArray from the SAMPLE_JSON_RESPONSE string
            Log.i("HI", "working");
            Log.i("JSON", bankJSON);
            JSONObject bankJsonObject = new JSONObject(bankJSON);
            Log.i("first", bankJsonObject.toString());

                // Extract the JSONObject
                JSONObject currentBank = bankJsonObject;

                String ifsc = currentBank.getString("ifsc");
                String branch = currentBank.getString("branch");
                String city = currentBank.getString("city");
                String district = currentBank.getString("district");
                String state = currentBank.getString("state");
                String address = currentBank.getString("address");
                JSONObject name_obj = currentBank.getJSONObject("bank");
                String name = name_obj.getString("name");
                Log.i("Branch : ", branch);

                String bank_detail = "Name : " + name + "\nBranch : " + branch + "\nCity : " + city +
                        "\nDistrict : " + district + "\nState" + state + "\nIFSC : " + ifsc + "\nAddress : " + address;

                // Create a new {@link BAnk} object with the details from JSON response
                Bank bank = new Bank(bank_detail);

                // Add the new {@link BAnk} to the list of BAnks.
                banks.add(bank);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Bank JSON results", e);
        }

        // Return the list of banks
        return banks;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        Log.i("urlhttp", url.toString());
        String jsonResponse = "";
        //If the URL is null then return early
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                Log.i("inputsream", inputStream.toString());
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Banks JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            Log.i("line:", line);
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
