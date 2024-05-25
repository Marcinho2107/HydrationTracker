package com.example.hydrationtracker_git;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPIRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Add your API key to the request
            connection.addRequestProperty("x-api-key", "YOUR_API_KEY");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
        } catch (Exception e) {
            Log.e("WeatherAPIRequest", "Error fetching weather data", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Parse the JSON response and update your UI with the weather data
        // Example: display the raw response in logcat
        Log.d("WeatherAPIResponse", result);
    }
}