package com.example.exchangeapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.exchangeapp.Adapter.BaseRateAdapter;
import com.example.exchangeapp.Adapter.SelectionAdapter;
import com.example.exchangeapp.Bean.Rate;
import com.example.exchangeapp.Database.RealmHelper;
import com.example.exchangeapp.Fragment.ExchangesFragment;
import com.example.exchangeapp.Preferences.SharedPreferences;
import com.example.exchangeapp.Service.ScheduledService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private List<Rate> mRates;
    private Spinner base_spinner;
    private SharedPreferences sharedPreferences;
    private RealmHelper realmHelper;
    private SelectionAdapter selectionAdapter;
    private ListView listView;
    private Button filterButton;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        sharedPreferences = new SharedPreferences(getApplicationContext());
        realmHelper = new RealmHelper();
        mRates = new ArrayList<>();
        mRates = realmHelper.readAllAscending();
        base_spinner = (Spinner) findViewById(R.id.spinner_nav);
        listView = (ListView) findViewById(R.id.list);
        filterButton = (Button) findViewById(R.id.btnFilter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                ExchangesFragment exchangesFragment = ExchangesFragment.newInstance();
                replaceFragmentWithAnimation(exchangesFragment, "Fragment");
            }
        });
        populateSpinner();
        selectionAdapter= new SelectionAdapter(mRates,getApplicationContext());
        listView.setAdapter(selectionAdapter);

        Intent i= new Intent(getApplicationContext(), ScheduledService.class);
        this.startService(i);

        fillFragment();

    }

    private void populateSpinner(){
        BaseRateAdapter spinAdapter = new BaseRateAdapter(getApplicationContext(), Arrays.asList(getResources().getStringArray(R.array.exchange_array)));
        base_spinner.setAdapter(spinAdapter);
        base_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                sharedPreferences.setBaseCountry(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void fillFragment(){
        ExchangesFragment exchangesFragment = ExchangesFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,exchangesFragment).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

}
