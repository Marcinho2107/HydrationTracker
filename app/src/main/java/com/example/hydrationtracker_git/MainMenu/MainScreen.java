/**
 * Diese Klasse stellt den Hauptbildschirm der Hydration Tracker-Anwendung dar.
 * Sie enthält die Navigation, Wetter- und Standortdaten, den Hydrationsbedarf des Benutzers und UI-Elemente für die Benutzerinteraktion.
 */
package com.example.hydrationtracker_git.MainMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.example.hydrationtracker_git.R;
import com.example.hydrationtracker_git.User_Progress.UserActivity;
import com.example.hydrationtracker_git.User_Progress.ProgressActivity;
import com.example.hydrationtracker_git.User_Progress.UserPreferences;
import com.example.hydrationtracker_git.weather_and_location.weatherData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONObject;
import android.Manifest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.util.Locale;
import java.util.Calendar;
import cz.msebera.android.httpclient.Header;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout dl;
    BottomNavigationView bnv;
    Toolbar t;
    static final String API_KEY = "021ce27779648254e2bc7282e66f7923";
    static final String Open_Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    static final long MIN_TIME = 5000;
    static final float MIN_DISTANCE = 1000;
    static final int REQUEST_CODE = 101;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    TextView mNameOfCity, mWeatherState, mTemperature;
    ImageView mWeatherIcon;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private TextView dailyRequirementTextView;
    String currentUsername;
    private TextView suggestedAmountNumber1TextView;
    private TextView suggestedAmountNumber2TextView;
    private TextView certainAmountPercentageTextView;
    private ImageView imageViewBottleRight;
    private int selectedAmount = 0;
    private int totalSelectedAmount = 0;



    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        t=findViewById(R.id.toolbar_top);
        setSupportActionBar(t);
        dl=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, dl, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        bnv=findViewById(R.id.bottomNavigationView_bottom);
        bnv.setBackground(null);
        bnv.setOnItemSelectedListener(item -> {
            int itemID=item.getItemId();
            if (itemID==R.id.main) {
                openActivity(MainScreen.class);
                return true;
            } else if (itemID==R.id.progress) {
                Intent intent1=new Intent(MainScreen.this, ProgressActivity.class);
                intent1.putExtra("username", currentUsername);
                intent1.putExtra("suggestedAmountNumber2", suggestedAmountNumber2TextView.getText().toString());
                intent1.putExtra("certainAmountPercentage", certainAmountPercentageTextView.getText().toString());
                intent1.putExtra("selectedAmount", selectedAmount);
                intent1.putExtra("totalSelectedAmount", totalSelectedAmount);
                startActivity(intent1);
                return true;
            } else if (itemID==R.id.user) {
                Intent intent2=new Intent(MainScreen.this, UserActivity.class);
                intent2.putExtra("username", currentUsername);
                intent2.putExtra("suggestedAmountNumber2", suggestedAmountNumber2TextView.getText().toString());
                intent2.putExtra("certainAmountPercentage", certainAmountPercentageTextView.getText().toString());
                intent2.putExtra("selectedAmount", selectedAmount);
                intent2.putExtra("totalSelectedAmount", totalSelectedAmount);
                startActivity(intent2);
                return true;
            }
            return false;
        });
        mWeatherState = findViewById(R.id.weatherCondition);
        mTemperature = findViewById(R.id.temperature);
        mWeatherIcon = findViewById(R.id.weatherSymbol);
        mNameOfCity = findViewById(R.id.cityName);
        dailyRequirementTextView = findViewById(R.id.dailyRequirementTextView);
        UserPreferences userPreferences = new UserPreferences(this);
        currentUsername = getIntent().getStringExtra("username");
        int wasserbedarf = userPreferences.getWasserbedarf(currentUsername);
        TextView suggestedAmountTextView = findViewById(R.id.textView_suggested_amount_number1);
        suggestedAmountTextView.setText(wasserbedarf + " ml");
        suggestedAmountNumber2TextView = findViewById(R.id.textView_suggested_amount_number2);
        suggestedAmountNumber2TextView.setText("0 ml");
        if (currentUsername == null) {
            Log.e("MainScreen", "Benutzername ist null");
            currentUsername = "default_user"; // Beispielweise ein default Wert
        } else {
            Log.d("MainScreen", "Benutzername ist " + currentUsername);
        }
        suggestedAmountNumber1TextView = findViewById(R.id.textView_suggested_amount_number1);
        suggestedAmountNumber2TextView = findViewById(R.id.textView_suggested_amount_number2);
        certainAmountPercentageTextView = findViewById(R.id.textView_certain_amount_percentage);
        suggestedAmountNumber2TextView = findViewById(R.id.textView_suggested_amount_number2);
        certainAmountPercentageTextView = findViewById(R.id.textView_certain_amount_percentage);
        Button updateButton = findViewById(R.id.button);
        imageViewBottleRight = findViewById(R.id.imageView_bottle_right);
        updateButton.setOnClickListener(v -> {
            suggestedAmountNumber2TextView.setText(totalSelectedAmount + " ml");
            if (totalSelectedAmount >= getSuggestedAmountNumber1()) {
                showNotification();
            }
            calculateAndDisplayPercentage();
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            userPreferences.saveDailyIntake(currentUsername, dayOfWeek, totalSelectedAmount);
        });
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);
        RadioButton rb4 = findViewById(R.id.rb4);
        rb1.setOnClickListener(v -> {
            selectedAmount = 250;
            totalSelectedAmount += selectedAmount;
        });
        rb2.setOnClickListener(v -> {
            selectedAmount = 500;
            totalSelectedAmount += selectedAmount;
        });
        rb3.setOnClickListener(v -> {
            selectedAmount = 750;
            totalSelectedAmount += selectedAmount;
        });
        rb4.setOnClickListener(v -> {
            selectedAmount = 1000;
            totalSelectedAmount += selectedAmount;
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main) {
            openActivity(MainScreen.class);
        } else if (id == R.id.progress) {
            openActivity(ProgressActivity.class);
        } else if (id == R.id.user) {
            openActivity(UserActivity.class);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Eröffnet eine neue Aktivität basierend auf der angegebenen Aktivitätsklasse.
     * Übergibt den aktuellen Benutzernamen als Extra an die neue Aktivität.
     *
     * @param activityClass Die Klasse der zu öffnenden Aktivität.
     */
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("username", currentUsername);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city=mIntent.getStringExtra("City");
        if(city!=null){
            getWeatherForNewCity(city);
        }else{
            getWeatherForCurrentLocation();
        }
        String suggestedAmountNumber2 = mIntent.getStringExtra("suggestedAmountNumber2");
        String certainAmountPercentage = mIntent.getStringExtra("certainAmountPercentage");
        int selectedAmount = mIntent.getIntExtra("selectedAmount", 0);
        int totalSelectedAmount = mIntent.getIntExtra("totalSelectedAmount", 0);
        if (suggestedAmountNumber2 != null) {
            suggestedAmountNumber2TextView.setText(suggestedAmountNumber2);
        }
        if (certainAmountPercentage != null) {
            certainAmountPercentageTextView.setText(certainAmountPercentage);
        }
        updateBottleImage(calculatePercentage());
        this.selectedAmount = selectedAmount;
        this.totalSelectedAmount = totalSelectedAmount;
    }

    /**
     * Platzhalter-Methode zur Berechnung und Rückgabe eines Prozentsatzes.
     *
     * @return Gibt immer 0 zurück (Platzhalter-Implementierung).
     */
    private double calculatePercentage() {
        return 0;
    }

    /**
     * Holt Wetterdaten für eine neue Stadt über die OpenWeatherMap API.
     *
     * @param city Der Name der Stadt, für die Wetterdaten angefordert werden.
     */
    private void getWeatherForNewCity(String city){
        RequestParams params=new RequestParams();
        params.put("q", city);
        params.put("appid", API_KEY);
        calculateData(params);
    }

    /**
     * Holt Wetterdaten für den aktuellen Gerätestandort anhand von GPS-Koordinaten ab.
     * Wenn die Standortgenehmigung nicht erteilt wird, fordert sie der Benutzer an.
     */
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                RequestParams params=new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", API_KEY);
                calculateData(params);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                showToast();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainScreen.this, "Location permission allowed by User", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
        }else{
            Toast.makeText(MainScreen.this, "Location permission denied by User", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Führt einen API-Aufruf durch, um Wetterdaten mit den angegebenen Parametern abzurufen.
     *
     * @param params Abfrageparameter für den API-Aufruf.
     */
    private void calculateData(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Open_Weather_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainScreen.this,"Data received!",Toast.LENGTH_SHORT).show();
                weatherData weatherD=weatherData.fromJson(response);
                assert weatherD != null;
                updateUI(weatherD);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainScreen.this, "Data not received!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Zeigt eine kurze Toast-Meldung an, die darauf hinweist, dass der Standort nicht abgerufen werden kann.
     */
    private void showToast() {
        Toast.makeText(this, "Can't get location!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Analysiert die Temperatur aus dem angegebenen Temperaturtext.
     * Entfernt nicht-numerische Zeichen aus dem Text und versucht, den numerischen Teil in eine Ganzzahl zu zerlegen.
     * Schlägt das Parsen fehl, wird 0 als Standardwert zurückgegeben.
     */
    @SuppressLint("SetTextI18n")
    private void updateUI(weatherData weather){
        mTemperature.setText(weather.getWdTemperature());
        mNameOfCity.setText(weather.getWdCity());
        mWeatherState.setText(weather.getWdType());
        @SuppressLint("DiscouragedApi") int resourceID=getResources().getIdentifier(weather.getWdIcon(),"drawable",getPackageName());
        mWeatherIcon.setImageResource(resourceID);
        updateDailyRequirementText(mTemperature.getText().toString());
    }
    private int parseTemperature(String temperatureText) {
        int temperature = 0;

        if (temperatureText != null && !temperatureText.isEmpty()) {
            String numericPart = temperatureText.replaceAll("\\D", "");

            try {
                temperature = Integer.parseInt(numericPart);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return temperature;
    }

    /**
     * Aktualisiert den Text von daily requirement TextView auf der Grundlage der Temperaturinformationen.
     * Die Methode analysiert die Temperatur aus dem angegebenen Text und setzt dann den Text
     * von {@code dailyRequirementTextView}, um die entsprechenden Trinkmengen
     * basierend auf der geparsten Temperatur.
     *
     * @param temperatureText Der Text mit der Temperaturinformation.
     * Sollte in einem Format vorliegen, das in eine Ganzzahl geparst werden kann.
     * Wenn das Parsen fehlschlägt oder die Temperatur 0 ist, werden entsprechende Meldungen
     * werden entsprechende Meldungen angezeigt, die auf fehlende Wetterinformationen hinweisen.
     */
    @SuppressLint("SetTextI18n")
    private void updateDailyRequirementText(String temperatureText) {
        int temperature = parseTemperature(temperatureText);

        if (temperature == 0) {
            dailyRequirementTextView.setText("No Weather Information! (No Value)");
            return;
        }
        try {
            if (temperature <= 25) {
                dailyRequirementTextView.setText("Normal Weather, drinking amount stays normal!");
            } else if (temperature <= 35) {
                dailyRequirementTextView.setText("Warm Weather, drinking amount should slightly increase!");
            } else {
                dailyRequirementTextView.setText("Hot Weather, drinking amount should significantly increase!");
            }
        } catch (NumberFormatException e) {
            dailyRequirementTextView.setText("No Weather Information! (No Parsing)");
        }
    }

    /**
     * Berechnet und zeigt den Prozentsatz der verbrauchten Menge im Verhältnis zur empfohlenen Menge an.
     * Aktualisiert die UI-Elemente, die den berechneten Prozentsatz anzeigen, und aktualisiert das Flaschenbild entsprechend.
     */
    private void calculateAndDisplayPercentage() {
        String suggestedAmountStr = suggestedAmountNumber1TextView.getText().toString().trim();
        String consumedAmountStr = suggestedAmountNumber2TextView.getText().toString().trim();
        if (!suggestedAmountStr.isEmpty() && !consumedAmountStr.isEmpty()) {
            int suggestedAmount = Integer.parseInt(suggestedAmountStr.replace(" ml", ""));
            int consumedAmount = Integer.parseInt(consumedAmountStr.replace(" ml", ""));
            double percentage = ((double) consumedAmount / suggestedAmount) * 100;
            certainAmountPercentageTextView.setText(String.format(Locale.getDefault(), "%.2f%%", percentage));
            updateBottleImage(percentage);
        }
    }

    /**
     * Ruft den vorgeschlagenen Betrag Nummer 1 von der Benutzeroberfläche ab.
     *
     * @return Der vorgeschlagene Betrag Nummer 1, geparst als Ganzzahl.
     */
    private int getSuggestedAmountNumber1() {
        String suggestedAmountStr = suggestedAmountNumber1TextView.getText().toString().trim();
        if (!suggestedAmountStr.isEmpty()) {
            return Integer.parseInt(suggestedAmountStr.replace(" ml", ""));
        }
        return 0;
    }

    /**
     * Zeigt dem Benutzer einen Benachrichtigungsdialog an.
     * Der Dialog zeigt eine Warnmeldung an und wird beim Anklicken der Schaltfläche geschlossen.
     */
    @SuppressLint("SetTextI18n")
    private void showNotification() {
        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        Button button = dialogView.findViewById(R.id.alert_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        button.setOnClickListener(v -> alertDialog.dismiss());
    }

    /**
     * Aktualisiert das Flaschenbild auf der Grundlage des angegebenen Prozentsatzes.
     *
     * @param percentage Der Prozentwert, der bestimmt, welches Bild angezeigt werden soll.
     */
    private void updateBottleImage(double percentage) {
        if (percentage >= 100) {
            imageViewBottleRight.setImageResource(R.drawable.bottle_100);
        } else if (percentage >= 75) {
            imageViewBottleRight.setImageResource(R.drawable.bottle_75);
        } else if (percentage >= 50) {
            imageViewBottleRight.setImageResource(R.drawable.bottle_50);
        } else if (percentage >= 25) {
            imageViewBottleRight.setImageResource(R.drawable.bottle_25);
        }
    }

    /**
     * Callback-Methode, die aufgerufen wird, wenn ein Optionsfeld angeklickt wird.
     * Diese Methode ist dafür gedacht, das Klick-Ereignis von Optionsschaltflächen in der Benutzeroberfläche zu behandeln.
     *
     * @param view Die Ansicht, die angeklickt wurde (in diesem Fall ein Optionsfeld).
     */
    public void radio_button_clicked(View view) {
    }
}