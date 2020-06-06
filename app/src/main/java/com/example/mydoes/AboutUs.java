package com.example.mydoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutUs extends AppCompatActivity {
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("For more details:");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Please note that this is Beta version. We will continually updated. If you have problems, please support or contact with us via these here")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with our team: ")
                .addEmail("khoakhung2210@gmail.com")
                .addWebsite("https://www.google.com/")
                .addFacebook("phananh.khoa.714")
                .addYoutube("UCagZNfONkgQncCB71z8lkBg")
                .addPlayStore("My PlayStore")
                .create();

        setContentView(aboutPage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
