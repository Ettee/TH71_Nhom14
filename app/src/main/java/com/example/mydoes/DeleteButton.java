package com.example.mydoes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Locale;

public class DeleteButton extends AppCompatActivity {
    RecyclerView ourdoes;
    //list Data
    ArrayList<MyDoesApp> list;
    ArrayList<Integer> tackbundel = new ArrayList<>();
    ArrayList<Integer> reciveBunde = new ArrayList<>();
    //Cac bien trong phuong thuc chua chay:1
    ArrayList<DoesAdapter_Delete> loadlist= new ArrayList<>();
    ArrayList<Integer> selectedList = new ArrayList<>();
    //KetThuc:1

    //List position wait to delete data:2
    public boolean isActionMode =false;
    ArrayList<MyDoesApp> waitList = new ArrayList<>();
    private int counter = 0;
    public int positionCheck = -1;
    private int countitem;
    //KetThuc2

    SharedPref sharedPref;

    ArrayList<Integer> positionList;
    DatabaseReference reference;
    DoesAdapter_Delete doesAdapter;
    SearchView searchViewdelete;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose__delete);

        searchViewdelete = findViewById(R.id.search_viewdelete);
//        setContentView(R.layout.activity_chose__delete);
        getSupportActionBar().setTitle("Chọn Notes cần xoá:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<MyDoesApp>();
        positionList = new ArrayList<>();


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
                doesAdapter = new DoesAdapter_Delete(DeleteButton.this, list);
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


//        ourdoes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position= ourdoes.getChildAdapterPosition(v);
//                Toast.makeText(context, "Checked Item"+ position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        DeleteButton deleteButton=

        //Cach Chua Chay
        //Chonn cac Item bang cach selected
//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                Toast.makeText(DeleteButton.this,"selected"+list.get(position).titledoes,Toast.LENGTH_SHORT).show();
//                Intent inten=new Intent(DeleteButton.this,DeleteButton.class);
//                if(selectedList.size()<0)
//                {
//                    selectedList.add(position);
//                }
//                else
//                    {
//                        for(int i=0;i<=selectedList.size();i++)
//                            {
//                                if(selectedList.get(i)==position)
//                                {
//                                    Toast.makeText(DeleteButton.this,"this item alredy selected"+list.get(position).titledoes,Toast.LENGTH_SHORT).show();
//                                }
//                                    else
//                                {
//                                    selectedList.add(position);
//                                }
//                            }
//                    }
//            }
//        });
    }

    //Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }
    //Ket thuc search:thinh

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Toast.makeText(getApplicationContext(), "yo", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case R.id.action_delete_notes:
                loadPush();
                return true;
                //Khong co Check
//                if (tackbundel.size()<=0)
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("No Item Selected" );
//                    builder.setTitle("Confirm");
//                    builder.setCancelable(true);
//                    builder.setPositiveButton("Keep Chosing", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                            }
//                    })
//                            .setNegativeButton("Back to main screen", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(getApplicationContext(), "You clicked on Cancel", Toast.LENGTH_SHORT).show();
//                                    // if this button is clicked, Cancel
//                                    Intent a = new Intent(DeleteButton.this, MainActivity.class);
//                                    startActivity(a);
////
//                                }
//                            });
//                    builder.show();
//                }
//
//                    else{
////                     checkBundel();
//                    LoadBundel();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("Delete " + tackbundel.size() + " item");
//                    builder.setTitle("Confirm");
//                    builder.setCancelable(true);
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            for (int i = 0; i <= tackbundel.size(); i++) {
//                                final String keykeyDoes = list.get(tackbundel.get(i)).getKeydoes();
//                                Toast.makeText(DeleteButton.this, "Delete Task is deleted:", Toast.LENGTH_SHORT).show();
//                                reference = FirebaseDatabase.getInstance().getReference("BoxDoes").child("Does" + keykeyDoes);
//                                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Intent a = new Intent(DeleteButton.this, DeleteButton.class);
//                                            startActivity(a);
//                                        } else {
//                                            Toast.makeText(getApplicationContext(), "Can't delete task.Please try again later.", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                                list.remove(i);
//                            }
//                            //finishAffinity();
//                            Intent a = new Intent(DeleteButton.this, DeleteButton.class);
//                            startActivity(a);
//                        }
//                    })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(getApplicationContext(), "You clicked on Cancel", Toast.LENGTH_LONG).show();
//                                    // if this button is clicked, Cancel
//                                    dialog.cancel();
//                                }
//                            });
//                    builder.show();
//                }
//                }
//                return true;

//                if(waitList.size()>=0)
//                {
//                   AlertDialog.Builder builder=new  AlertDialog.Builder(this);
//                   builder.setMessage("Delete"+waitList.size()+"item");
//                   builder.setTitle("Confirm");
//                   builder.setCancelable(true);
//                   builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            DeleteNote(selectedList);
//                            Intent a = new Intent(DeleteButton.this, DeleteButton.class);
//                            startActivity(a);
//                            Toast.makeText(getApplicationContext(), "selected notes are deleted", Toast.LENGTH_LONG).show();
//
//
//                           finish();
//                        }
//                    })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(getApplicationContext(), "You clicked on Cancel", Toast.LENGTH_LONG).show();
//                                    // if this button is clicked, Cancel
//                                    dialog.cancel();
//                                }
//                            });
//                    builder.show();
//
//                }
//                if (!isActionMode)
//                    Toast.makeText(getApplicationContext(), "You checked no task", Toast.LENGTH_LONG).show();
//                return true;
            case R.id.action_deleteall:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Delete Confirm");
                alertDialogBuilder.setMessage("Do you want to Delete all Task ?");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent a = new Intent(DeleteButton.this, MainActivity.class);
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
            case R.id.aboutudeletes:
                Intent intent1 = new Intent(DeleteButton.this, AboutUs.class);
                startActivity(intent1);
                return true;
            case R.id.changethemedeletes:
                Intent intent = new Intent(DeleteButton.this, ChangeTheme.class);
                startActivity(intent);
                return true;
            case R.id.refreshdeletes:
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

 public  void loadPush()
    {
        Intent getbundel=getIntent();
        ArrayList<Integer> arr=getIntent().getIntegerArrayListExtra("Mylist");
        Toast.makeText(context, " Checked vi tri "+arr.get(0), Toast.LENGTH_SHORT).show();
    }


    void LoadBundel()
{

    Intent getbundel=getIntent();
    Bundle takePacket=getbundel.getBundleExtra("Mylist");
    int value=-1;
    //Neu bundo tu 0-> array note luu gia vi tri thi them vao hang doi xoa
    for(int i=0;i<list.size();i++)
    {
        value=takePacket.getInt("Vitri"+i);
          if (value>0)
              tackbundel.add(value-1);
    }
}


    //co task duoc check
    void checkItemisSelecting()
    {
        if (countitem>0) {
            Toast.makeText(getApplicationContext(), "position" + String.valueOf(countitem++), Toast.LENGTH_LONG).show();
            Intent a = new Intent(DeleteButton.this, DeleteButton.class);
            startActivity(a);
        }

            //khong co task duoc check
        else if(countitem<=0)
            {
                Toast.makeText(getApplicationContext(), "position"+String.valueOf(countitem++), Toast.LENGTH_LONG).show();
                Intent a = new Intent(DeleteButton.this, DeleteButton.class);
                startActivity(a);
        }
    }

    //DeleteList Item
    void DeleteNote(ArrayList<Integer> selectedList) {
//        //Bat dau
//        // xoa
        for (int i = 0; i < positionList.size(); i++) {
            int vitri = positionList.get(i);
            //Nhan gia tri vi tri va xoa
            //log thu key does ra de xem dung ko
            final String keykeyDoes = list.get(vitri).getKeydoes();
            reference = FirebaseDatabase.getInstance().getReference("BoxDoes").child("Does" + keykeyDoes);
            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent a = new Intent(DeleteButton.this, DeleteButton.class);
                        startActivity(a);
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't delete task.Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            list.remove(vitri);
            Intent intent1 = new Intent(DeleteButton.this, DeleteButton.class);
            startActivity(intent1);
        }
    }

    ///Ket thuc cacch chua chay

//Phan o duoi bi loi do DoesAdapter_Delete khong goi duoc cac phuong thuc
//    private void clearActionMode()
//{
//    isActionMode=false;
//    waitList.clear();
//    doesAdapter.notifyDataSetChanged();
//    counter=0;
//}
     public void startSelection(int index)
   {
//       if(!isActionMode)
//       {
//           isActionMode=true;
           waitList.add(list.get(index));
           counter++;
           updateToolbarText(counter);
           positionCheck=index;
            doesAdapter.notifyDataSetChanged();
//       }
   }
//
//   //Kiem Tra checkbox
    public void selectedCheck(View v, int index)
    {
        if(((CheckBox)v).isChecked())
        {
            waitList.add(list.get(index));
            counter++;
            updateToolbarText(counter);
            doesAdapter.notifyDataSetChanged();
        }
        else
        {
            waitList.remove(list.get(index));
            counter--;
            updateToolbarText(counter);
        }
    }
    public  Boolean Getactionmode(Boolean isActionMode)
{
    if(isActionMode)
    {
        return true;
    }
    else
        return false;
}

////Thonng bao so luoc item selected
    private void updateToolbarText(int counter) {
        if(counter==0)
        {    Toast.makeText(getApplicationContext(), "You checked no task", Toast.LENGTH_LONG).show();
        }
        if(counter==1) {
            Toast.makeText(getApplicationContext(), "1 Task is waitting to delete:", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), counter+" Task is waitting to delete:", Toast.LENGTH_LONG).show();
        }
    }
}