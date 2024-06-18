package com.example.hydrationtracker_git;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

/** @noinspection ALL*/
public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout dl;
    BottomNavigationView bnv;
    FragmentManager fm;
    Toolbar t;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //Initializing Main Screen Navigation Items
        fab = findViewById(R.id.floatingActionButton_bottom);
        t = findViewById(R.id.toolbar_top);
        setSupportActionBar(t);
        dl = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        bnv = findViewById(R.id.bottomNavigationView_bottom);
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
        transaction.replace(R.id.frameLayout_all, fragment);
        transaction.commit();
    }
    public void radio_button_clicked(View view) {
    }
}

