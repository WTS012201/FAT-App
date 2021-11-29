package com.example.fatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MealPlan extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener { //, CreateMeals.FragmentAListener, TotalCalories.FragmentBListener {
    private DrawerLayout drawer;
//hello

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CreateMeals()).commit();
            navigationView.setCheckedItem(R.id.nav_mealplan);


        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_mealplan:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateMeals()).commit();
                    break;
                case R.id.nav_calories:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new TotalCalories()).commit();
                    break;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Home()).commit();
                    break;
                case R.id.nav_share:
                  //  Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_about:
                //    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                    break;
            }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
