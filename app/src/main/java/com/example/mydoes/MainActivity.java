package com.example.mydoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.OptionalDouble;

public class MainActivity extends AppCompatActivity   {
    TextView titlepage, subtitlepage, endpage;
    Button btnAddNew;
    SearchView searchView;
    SwipeRefreshLayout swipeRefresh;
    SharedPref sharedPref;
    SharedPreferences sharedPreferences;
    //Thinh
    EditTaskDesk editTaskDesk;
    final Context context = this;
    //KetThucThinh

    RecyclerView ourdoes;
    ArrayList<MyDoesApp> list;
    DatabaseReference reference;
    DoesAdapter doesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);
        endpage = findViewById(R.id.endpage);
        btnAddNew = findViewById(R.id.btnAddNew);
        searchView = findViewById(R.id.search_view);
        swipeRefresh = findViewById(R.id.refreshlayout);


        //import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

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
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewTask = new Intent(MainActivity.this, NewTaskAct.class);
                startActivity(createNewTask);
            }
        });

        //working with data
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyDoesApp>();

        //get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("BoxDoes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data from firebase and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyDoesApp p = dataSnapshot1.getValue(MyDoesApp.class);
                    list.add(p);

                }
                //Toast.makeText(MainActivity.this,list.get(1).getKeydoes().toString(),Toast.LENGTH_SHORT).show();
                doesAdapter = new DoesAdapter(MainActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

//        //Thinh Delete swipe
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int direction) {
                //xoa note tren interface
                int position = target.getAdapterPosition();
                //Toast.makeText(MainActivity.this,"pos"+String.valueOf(position),Toast.LENGTH_SHORT).show();
               //Bat dau xoa
                //truyen gia tri key does cho intentkeykeyDoes
                //inten.putExtra("keydoes",list.get(position).getKeydoes());
                //Toast.makeText(MainActivity.this,"keykey"+list.get(position).getKeydoes(),Toast.LENGTH_SHORT).show();
                //truyen gia tri cho keykeyDoes
                //log thu key does ra de xem dung ko

                final String keykeyDoes =list.get(position).getKeydoes();
               Toast.makeText(MainActivity.this,"Delete Task is deleted:",Toast.LENGTH_SHORT).show();
                reference=FirebaseDatabase.getInstance().getReference("BoxDoes").child("Does"+keykeyDoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                      Intent a =new Intent(MainActivity.this,MainActivity.class);
                                     startActivity(a);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Can't delete task.Please try again later.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                list.remove(position);
                //doesAdapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(ourdoes);
        //Ket thuc thinh Delete swipe

//        Bat dau Search : thinh
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
//        Ket thuc search:thinh
   }
    //Thinh Exit Dialog
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Confirm to Exit");
        alertDialogBuilder.setMessage("Do you want to Exit from this app ?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                // current activity
                finishAffinity();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_LONG).show();
                        // if this button is clicked, Cancel
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    Search function
    private void search(String str) {
        ArrayList<MyDoesApp> mylist = new ArrayList<>();
        for (MyDoesApp object : list) {
            if (object.getTitledoes().toLowerCase().contains(str.toLowerCase()) || object.getDescdoes().toLowerCase().contains(str.toLowerCase())) {
                mylist.add(object);
            }
        }
        DoesAdapter adapter = new DoesAdapter(MainActivity.this, mylist);
        ourdoes.setAdapter(adapter);

    }
    //End Search funtion

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
            case R.id.action_delete:
                //DeleteThinh
//                Intent delete = new Intent(MainActivity.this, DeleteButton.class);
//                startActivity(delete);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Delete Confirm");
                alertDialogBuilder.setMessage("Do you want to delete all Tasks ?");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent a = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(a);
                                    Toast.makeText(getApplicationContext(), "All note are deleted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Can't delete Task.Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        finish();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "You clicked on Cancel", Toast.LENGTH_LONG).show();
                                // if this button is clicked, Cancel
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            case R.id.action_refresh:
                recreate();
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
                if (i == 0) {
                    setLocale("vi");
                    recreate();
                } else if (i == 1) {
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
/* Query
       databaseReferencedDelete=FirebaseDatabase.getInstance().getReference("BoxDoes");
                //delete Data bang query
                //end deleteData byQuery
                 Query query=FirebaseDatabase.getInstance().getReference("BoxDoes")
                                    .orderByChild("titledoes")
                                        .equalTo("h3hbr");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                        {
                                            MyDoesApp item=snapshot.getValue(MyDoesApp.class);
                                            list.remove(item);
                                        }
                                        doesAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        //set code to show an error
                                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                                    }
                                });
*/
}

