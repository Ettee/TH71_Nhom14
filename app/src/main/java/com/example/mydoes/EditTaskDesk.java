package com.example.mydoes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class EditTaskDesk extends AppCompatActivity {
    EditText titleDoes,descDoes;
    TextView dateDoes,timeDoes;
    Button btnSaveUpdate,btnDelete;
    DatabaseReference reference;
    SharedPref sharedPref;
    String timePicked;
    boolean isDatePicked=false;
    Integer yearPicked,monthPicked,dateOfMonthPicked;
    String titleForAlert,descForAlert;
    Map<String, Object> updates = new HashMap<String,Object>();
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
        setContentView(R.layout.activity_edit_task_desk);

        titleDoes=findViewById(R.id.titleedit);
        descDoes=findViewById(R.id.descedit);
        dateDoes=findViewById(R.id.dateedit);
        timeDoes=findViewById(R.id.timeedit);

        btnSaveUpdate=findViewById(R.id.btnSaveUpdate);
        btnDelete =findViewById(R.id.btnDelete);

        // 2 dòng dưới ngăn việc popup 1 cái keyboard khi ấn vào 2 edit text đó
        dateDoes.setShowSoftInputOnFocus(false);
        timeDoes.setShowSoftInputOnFocus(false);

        //set hint for edit text
        titleDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));
        timeDoes.setText(getIntent().getStringExtra("timedoes"));
        final String keykeyDoes =getIntent().getStringExtra("keydoes");
        reference= FirebaseDatabase.getInstance().getReference().child("BoxDoes").
                child("Does"+keykeyDoes);
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent a =new Intent(EditTaskDesk.this,MainActivity.class);
                            startActivity(a);
                        }else{
                            Toast.makeText(getApplicationContext(),"Can't delete task.Please try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //make an event for button
        btnSaveUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    //insert data to database
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("titledoes").setValue(titleDoes.getText().toString());
                            dataSnapshot.getRef().child("descdoes").setValue(descDoes.getText().toString());
                            dataSnapshot.getRef().child("datedoes").setValue(dateDoes.getText().toString());
                            dataSnapshot.getRef().child("timedoes").setValue(timeDoes.getText().toString());
                            Intent createTaskInMain = new Intent(EditTaskDesk.this,MainActivity.class);
                            startActivity(createTaskInMain);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Date remindDate=new Date(timePicked.trim());
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
                    calendar.setTime(remindDate);
                    calendar.set(Calendar.SECOND,0);

                    titleForAlert=titleDoes.getText().toString();
                    descForAlert=descDoes.getText().toString();

                    Intent intent= new Intent(EditTaskDesk.this,NotifierAlarm.class);
                    intent.putExtra("title",titleForAlert);
                    intent.putExtra("desc",descForAlert);
                    intent.putExtra("time",remindDate.toString());
                    intent.putExtra("id",keykeyDoes);
                    PendingIntent intent1 = PendingIntent.getBroadcast(EditTaskDesk.this,Integer.parseInt(keykeyDoes),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);
            }
        });

        dateDoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateChange();
            }
        });
        timeDoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTimeChange();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

    }
    private void getDateChange(){
        final Calendar calendar = Calendar.getInstance();
        int nam= calendar.get(Calendar.YEAR);
        int thang=calendar.get(Calendar.MONTH);
        int ngay = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i: năm -i1: tháng -i2: ngày
                // user khi set lại thời gian sẽ trả ra i i1 i2, dùng 3 biến đó để set lại time r hiển thị ra edittext bên ngoài
                final Calendar newDate= Calendar.getInstance();
                newDate.set(i,i1,i2);
                Calendar tem=Calendar.getInstance();
                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>=0){
                    calendar.set(i,i1,i2);
                    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                    dateDoes.setText(simpleDateFormat.format(calendar.getTime()));
                    yearPicked=i;
                    monthPicked=i1;
                    dateOfMonthPicked=i2;
                    isDatePicked=true;
                }else{
                    Toast.makeText(EditTaskDesk.this,"Invalid time",Toast.LENGTH_LONG).show();
                }

            }
        },nam,thang,ngay);

        //set mờ những ngày ở quá khứ
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void getTimeChange(){
        if(isDatePicked){
            final Calendar calendar= Calendar.getInstance();
            int gio=calendar.get(Calendar.HOUR_OF_DAY);
            int phut =calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("HH:mm");
                    calendar.set(yearPicked,monthPicked,dateOfMonthPicked,i,i1);
                    timeDoes.setText(simpleDateFormat.format(calendar.getTime()));
                    timePicked=calendar.getTime().toString();
                }
            },gio,phut,true);
            timePickerDialog.show();
        }else{
            Toast.makeText(EditTaskDesk.this,"Chưa chọn ngày",Toast.LENGTH_LONG).show();
        }

    }

//    private boolean checkTitleInput(){
//        final EditText titleedit=findViewById(R.id.titleedit);
//        final String text = titleedit.getText().toString();
//        if(text.length()== 0){
//            titleedit.setError("Bạn chưa nhập title nè");
//            return false;
//        }else {
//            return true;
//        }
//
//    }
//    private boolean checkDescInput(){
//        final EditText descedit=findViewById(R.id.descedit);
//        final String text = descedit.getText().toString();
//        if(text.length()==0){
//            descedit.setError("Bạn chưa nhập description nè.");
//            return false;
//        }
//        else{
//            return true;
//        }
//    }

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
                Intent change = new Intent(EditTaskDesk.this, ChangeTheme.class);
                startActivity(change);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
