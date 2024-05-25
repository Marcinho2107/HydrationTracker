package com.example.hydrationtracker_git;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements LocationListener{

    Button button_location;
    TextView txt_view_location;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        txt_view_location = findViewById(R.id.location_txt_view);
        button_location = findViewById(R.id.location_button);


        //Runtime Permission
        if (ContextCompat.checkSelfPermission(MainScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainScreen.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        button_location.setOnClickListener(v -> getLocation());

        //WeatherAPIRequest Instance
        WeatherAPIRequest weatherAPIRequest = new WeatherAPIRequest();

        //WeatherAPIRequest Instance URL(021ce27779648254e2bc7282e66f7923)
        weatherAPIRequest.execute("https://api.openweathermap.org/data/2.5/weather?q=YOUR_LOCATION&appid=021ce27779648254e2bc7282e66f7923");
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainScreen.this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try{
            Geocoder geocoder = new Geocoder(MainScreen.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            assert addresses != null;
            String address = addresses.get(0).getAddressLine(0);
            txt_view_location.setText(address);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}

