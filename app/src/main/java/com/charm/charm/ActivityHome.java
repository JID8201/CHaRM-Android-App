package com.charm.charm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ActivityHome extends AppCompatActivity {

    // Fragment variables
    private FragmentManager fragmentManager;

    // DrawerLayout variables
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // Manage fragment setup.
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Set the fragment that the activity initializes to.
        fragmentTransaction.add( R.id.frame_fragment_container, new FragmentHome() );
        fragmentTransaction.commit();

        // Manage DrawerLayout
        setupDrawer();

        // Setup toolbar
        Toolbar toolbar = findViewById( R.id.loggedIn_toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeAsUpIndicator( R.drawable.ic_menu_icon );
    }

    /**
     * Add DrawerLayout listener and add navigation view listener.
     */
    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);


        // Set the home fragment as the selected item in our nav menu.
        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setCheckedItem( R.id.nav_home_option );


        // A listener that handles onClicks for menu items in the navigation drawer.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    FragmentTransaction fragmentTransaction;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch( item.getItemId() ) {
                            case R.id.nav_home_option:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentHome fragmentHome = new FragmentHome();
                                fragmentTransaction.replace( R.id.frame_fragment_container, fragmentHome );
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_recycle_option:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentRecycle fragmentRecycle = new FragmentRecycle();
                                fragmentTransaction.replace( R.id.frame_fragment_container, fragmentRecycle );
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_info_option:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentInfo fragmentInfo = new FragmentInfo();
                                fragmentTransaction.replace( R.id.frame_fragment_container, fragmentInfo );
                                fragmentTransaction.commit();
                                break;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    /**
     * Handles all toolbar interactions.
     */
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch( item.getItemId() ) {
            case android.R.id.home:
                drawerLayout.openDrawer( GravityCompat.START );
                return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
