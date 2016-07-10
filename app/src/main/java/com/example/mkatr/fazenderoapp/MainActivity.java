package com.example.mkatr.fazenderoapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Fragmentların açılacagı bir container ekliyoruz. app_bar_main dosyasına
    /*
        <!--Bir container ekliyoruz-->
        <FrameLayout
            android:id="@+id/fragment_container"
            android:paddingTop="60dp"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        </FrameLayout>
    */

    //Yeni fragmentlar eklerken blankfragment ekliyoruz.
    //Ayrıca yeni fragment eklerken altta bulanan include seçeneklerini işaretlemiyoruz.

    static NavigationView navigationView;
    Toolbar toolbar = null;
    SharedPreferences sha;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("FAZENDERO");

        sha = getSharedPreferences("kul", MODE_PRIVATE);
        edit = sha.edit();

        //Main fragment ekle
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (!sha.getString("kullaniciId", "").equals("")) {
            navigationView.getMenu().findItem(R.id.nav_giris).setTitle("PROFİL");
        }

        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //Sağ üstteki menu görünmesin.
        menu.findItem(R.id.action_settings).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_anasayfa) {
            //Main fragment ekle
            MainFragment fragment = new MainFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_markalar) {
            //Markalar fragment ekle
            MarkalarFragment fragment = new MarkalarFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_yiyecekler) {
            //Yiyecekler fragment ekle
            YiyeceklerFragment fragment = new YiyeceklerFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_icecekler) {
            //İçecekler fragment ekle
            IceceklerFragment fragment = new IceceklerFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_tatli) {
            //Tatlı fragment ekle
            TatliFragment fragment = new TatliFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_kampanyalar) {
            //Kampanyalar fragment ekle
            KampanyalarFragment fragment = new KampanyalarFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_giris) {
            //Giriş Yap fragment ekle
            GirisFragment fragment = new GirisFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_nedir) {
            //Fazendero.com anasayfasına
            try {
                String goUrl = "http://www.fazendero.com/tema/v1/fazendero.mp4";
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(goUrl));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "URL Hatası!" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
