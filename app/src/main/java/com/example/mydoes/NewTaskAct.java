package com.example.mydoes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class NewTaskAct extends AppCompatActivity {
    TextView titlepage,addtitle,adddesc,adddate;
    EditText titledoes;
    EditText descdoes;
    TextView datedoes;
    TextView timedoes;
    Button btnSaveTask,btnCancel;
    DatabaseReference reference;
    SharedPref sharedPref;

    Integer doesNum=new Random().nextInt(100000);

    String keydoes = Integer.toString(doesNum);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc=findViewById(R.id.adddesc);
        adddate= findViewById(R.id.adddate);

        titledoes =findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes =(TextView) findViewById(R.id.datedoes);
        timedoes =(TextView) findViewById(R.id.timedoes);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel =findViewById(R.id.btnCancel);

        // 2 dòng dưới ngăn việc popup 1 cái keyboard khi ấn vào 2 edit text đó
//        datedoes.setShowSoftInputOnFocus(false);
//        timedoes.setShowSoftInputOnFocus(false);

        btnSaveTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //insert data to database
                reference= FirebaseDatabase.getInstance().getReference().child("BoxDoes").
                        child("Does"+doesNum);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // cai child() dựa theo id của phần item_does
                        dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                        dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                        dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                        dataSnapshot.getRef().child("timedoes").setValue(timedoes.getText().toString());
                        dataSnapshot.getRef().child("keydoes").setValue(keydoes);
                        Intent createTaskInMain = new Intent(NewTaskAct.this,MainActivity.class);
                        startActivity(createTaskInMain);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        datedoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay();
            };
        });
        timedoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGio();
            }
        });


        //import font
        Typeface Mlight = Typeface.createFromAsset(getAssets(),"fonts/ML.ttf");
        Typeface MMedium =Typeface.createFromAsset(getAssets(),"fonts/MM.ttf");

        //customize font
        titlepage.setTypeface(MMedium);

        titledoes.setTypeface(MMedium);
        addtitle.setTypeface(Mlight);

        adddesc.setTypeface(Mlight);
        descdoes.setTypeface(MMedium);

        adddate.setTypeface(Mlight);
        datedoes.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancel.setTypeface(Mlight);
        Utils.hideKeyboard( NewTaskAct.this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

    }
    private void ChonNgay(){
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i: năm -i1: tháng -i2: ngày
                // user khi set lại thời gian sẽ trả ra i i1 i2, dùng 3 biến đó để set lại time r hiển thị ra edittext bên ngoài
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                datedoes.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,date);
        datePickerDialog.show();
    }
    private void ChonGio(){
        final Calendar  calendar= Calendar.getInstance();
        int gio=calendar.get(Calendar.HOUR_OF_DAY);
        int phut= calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0,0,0,i,i1);
                timedoes.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },gio,phut,true);
        timePickerDialog.show();
    }

    //create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.change_theme:
                Intent change = new Intent(NewTaskAct.this, ChangeTheme.class);
                startActivity(change);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
