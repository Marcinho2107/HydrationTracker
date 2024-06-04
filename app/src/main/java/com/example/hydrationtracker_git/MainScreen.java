package com.example.hydrationtracker_git;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import java.util.List;
import java.util.Locale;

/** @noinspection ALL*/
public class MainScreen extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {
    TextView txt_view_location;
    Button button_location;
    LocationManager locationManager_location;
    DrawerLayout dl;
    BottomNavigationView bnv;
    FragmentManager fm;
    Toolbar t;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //Initializing Location Items
        //txt_view_location = findViewById(R.id.location_txt_view);
        //button_location = findViewById(R.id.location_button);
        //Initializing Main Screen Navigation Items
        fab = findViewById(R.id.floatingActionButton);
        t = findViewById(R.id.toolbar);
        setSupportActionBar(t);
        dl = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setBackground(null);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID=item.getItemId();
                if (itemID==R.id.user){
                    openFragment(new UserFragment());
                    return true;
                } else if (itemID==R.id.progress){
                    openFragment(new ProgressFragment());
                    return true;
                }
                return false;
            }
        });
        fm = getSupportFragmentManager();
        openFragment(new HomeFragment());
        fab.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainScreen.this,"Upload", Toast.LENGTH_SHORT).show();
            }
        }));
        //Runtime Permission
        if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainScreen.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        button_location.setOnClickListener(v -> getLocation());
        //WeatherAPIRequest Instance
        WeatherAPIRequest weatherAPIRequest = new WeatherAPIRequest();
        //WeatherAPIRequest Instance URL()
        weatherAPIRequest.execute("https://api.openweathermap.org/data/2.5/weather?q=YOUR_LOCATION&appid=");
    }


    //Navigation Menu Methods
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.info){
            openFragment(new ProgressFragment());
        } else if (itemId==R.id.day_tracking) {
            Toast.makeText(this,"Day Tracking", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.week_tracking) {
            Toast.makeText(this,"Week Tracking", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.month_tracking) {
            Toast.makeText(this,"Month Tracking", Toast.LENGTH_SHORT).show();
        }
        dl.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.fragment_layout_fragmentContainer, fragment);
        transaction.commit();
    }
    //Location Methods
    @SuppressLint("MissingPermission")
    private void getLocation(){
        try{
            locationManager_location = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager_location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainScreen.this);
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

    public void radio_button_clicked(View view) {
    }
}

