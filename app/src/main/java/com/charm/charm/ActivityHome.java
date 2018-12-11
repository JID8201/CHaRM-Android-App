package com.charm.charm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ActivityHome extends AppCompatActivity implements ZipDialogFragment.DialogListener {

    public interface ActivityInterface {
        void callListener();
    }

    private ActivityInterface listener;

    public void setListener( ActivityInterface listener ) {
        this.listener = listener;
    }

    // Fragment variables
    private FragmentManager fragmentManager;

    // DrawerLayout variables
    private DrawerLayout drawerLayout;

    private Menu toolbarMenu;
    private Boolean zip_modified_from_toolbar;

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

        zip_modified_from_toolbar = false;
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

                                setEditZipInvisible( true );
                                break;
                            case R.id.nav_recycle_option:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentRecycle fragmentRecycle = new FragmentRecycle();
                                fragmentTransaction.replace( R.id.frame_fragment_container, fragmentRecycle );
                                fragmentTransaction.commit();
                                setListener( fragmentRecycle );

                                setEditZipInvisible( false );
                                break;
                            case R.id.nav_info_option:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentInfo fragmentInfo = new FragmentInfo();
                                fragmentTransaction.replace( R.id.frame_fragment_container, fragmentInfo );
                                fragmentTransaction.commit();

                                setEditZipInvisible( true );
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
            case R.id.toolbar_edit_zip:
                zip_modified_from_toolbar = true;
                ZipDialogFragment zipDialogFragment = new ZipDialogFragment();
                zipDialogFragment.show( getSupportFragmentManager(), "zipcode" );
        }

        return super.onOptionsItemSelected( item );
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbarMenu = menu;
        return true;
    }

    public void onDialogPositiveClick( DialogFragment dialog ) {
        if( !zip_modified_from_toolbar ) {
            listener.callListener();
        }
        // Reset this after opening the toolbar
        zip_modified_from_toolbar = false;
    }

    public void onDialogCancel( DialogFragment dialog ) {
        // Reset this since the zip was exited.
        zip_modified_from_toolbar = false;
    }

    private void setEditZipInvisible( Boolean invisible ) {
        MenuItem toolbar_edit_zip = toolbarMenu.findItem( R.id.toolbar_edit_zip );
        if( invisible ) {
            toolbar_edit_zip.setVisible( false );
        } else {
            toolbar_edit_zip.setVisible( true );
        }

    }
}
