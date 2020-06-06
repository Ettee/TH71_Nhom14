package com.example.mydoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView titlepage, subtitlepage,endpage;
    Button btnAddNew;
    SearchView searchView;
    SwipeRefreshLayout swipeRefresh;
    SharedPref sharedPref;


    RecyclerView ourdoes;
    ArrayList<MyDoesApp> list;
    DatabaseReference reference;
    DoesAdapter doesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titlepage =findViewById(R.id.titlepage);
        subtitlepage=findViewById(R.id.subtitlepage);
        endpage=findViewById(R.id.endpage);
        btnAddNew =findViewById(R.id.btnAddNew);
        searchView = findViewById(R.id.search_view);
        swipeRefresh = findViewById(R.id.refreshlayout);



        //import font
        Typeface MLight=Typeface.createFromAsset(getAssets(),"fonts/ML.ttf");
        Typeface MMedium=Typeface.createFromAsset(getAssets(),"fonts/MM.ttf");

        //customize font
        titlepage.setTypeface(MMedium);
        subtitlepage.setTypeface(MLight);
        endpage.setTypeface(MLight);

        //refresh layout
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.Crimson), getResources().getColor(R.color.Himawari), getResources().getColor(R.color.Aqua));
                        Toast.makeText(swipeRefresh.getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });

        // when btn is clicked, a new activity will show up on screen
        btnAddNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent createNewTask = new Intent(MainActivity.this,NewTaskAct.class);
                startActivity(createNewTask);
            }
        });

        //working with data
        ourdoes=findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyDoesApp>();

        //get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("BoxDoes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data from firebase and replace layout
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MyDoesApp p = dataSnapshot1.getValue(MyDoesApp.class);
                    list.add(p);
                }
                doesAdapter = new DoesAdapter(MainActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //set code to show an error
                Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private void search(String str) {
        ArrayList<MyDoesApp> mylist = new ArrayList<>();
        for(MyDoesApp object : list) {
            if(object.getTitledoes().toLowerCase().contains(str.toLowerCase()) || object.getDescdoes().toLowerCase().contains(str.toLowerCase())) {
                mylist.add(object);
            }
        }
        DoesAdapter adapter = new DoesAdapter(MainActivity.this, mylist);
        ourdoes.setAdapter(adapter);
    }

    //create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

//    public void ganNgonNgu(String lang) {
//        Locale locale = new Locale(lang);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        Intent inten = new Intent(MainActivity.this, MainActivity.class);
//        startActivity(inten);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.lang_vn:
//                ganNgonNgu("vi");
//                return true;
//            case R.id.lang_en:
//                ganNgonNgu("en");
//                return true;
            case R.id.change_theme:
                Intent intent = new Intent(MainActivity.this, ChangeTheme.class);
                startActivity(intent);
                return true;
            case R.id.action_language:
                showChangeLanguageDialog();
                return true;
            case R.id.action_contact:
                Intent intent1 = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"Tiếng Việt", "English"};
        AlertDialog.Builder alertlog = new AlertDialog.Builder(MainActivity.this);
        alertlog.setTitle("Choose Language");
        alertlog.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0) {
                    setLocale("vi");
                    recreate();
                }
                else if(i == 1) {
                    setLocale("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = alertlog.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editLANG = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editLANG.putString("My_Lang", lang);
        editLANG.apply();
    }

    //Load languague saved in SharedPreferences
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}
