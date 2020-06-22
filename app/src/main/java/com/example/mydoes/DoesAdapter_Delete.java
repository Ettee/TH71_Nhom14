package com.example.mydoes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public  class DoesAdapter_Delete extends RecyclerView.Adapter<DoesAdapter_Delete.MyViewHolder> {


    //Chua Chay
   public ArrayList<Integer>selectedItem=new ArrayList<>();
    SharedPreferences sharedPreferences;
    //Ket Thuc Chua CHay
    DeleteButton deleteButton;
    Context context;
    ArrayList<MyDoesApp> myDoesdelete;
    ArrayList<String>WaitDelete;
    int count=0;

    public DoesAdapter_Delete(Context c, ArrayList<MyDoesApp> p) {
        this.context=c;
        this.myDoesdelete = p;

    }
    //create a task base on item_does layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_delete,viewGroup, false));
    }
    //bind thông tin đã nhập vào task (item_does) và hiển thị ra
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.titledoesdelete.setText(myDoesdelete.get(i).getTitledoes());
        myViewHolder.descdoesdelete.setText(myDoesdelete.get(i).getDescdoes());
        myViewHolder.datedoesdelete.setText(myDoesdelete.get(i).getDatedoes());
        myViewHolder.timedoesdelete.setText(myDoesdelete.get(i).getTimedoes());

        //lấy title,desc, date của task để chuyển qua edit task
        final String getTitleDoes=myDoesdelete.get(i).getTitledoes();
        final String getDescDoes=myDoesdelete.get(i).getDescdoes();
        final String getDateDoes=myDoesdelete.get(i).getDatedoes();
        final String getKeyDoes=String.valueOf(myDoesdelete.get(i).getKeydoes());
        final String getTimeDoes=myDoesdelete.get(i).getTimedoes();

//Chua Chay Bat Dau
//        myViewHolder.mview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               int position= myViewHolder.getLayoutPosition();
////                Log.d("size", "onClick:"+position);
////                Toast.makeText(context, "You choose :"+ myDoesdelete.get(position).getTitledoes(), Toast.LENGTH_SHORT).show();
//                if(selectedItem.size()==0)
//                {
//                    Toast.makeText(context, "Checked Item"+ myDoesdelete.get(position).getTitledoes(), Toast.LENGTH_SHORT).show();
//                    Log.d("size", "onClick:"+position);
//                    selectedItem.add(position);
//                }
//                else
//                {
//                    for(Object object:selectedItem)
//                    {
//                        if((Integer)object==position) {
//                            Toast.makeText(context, "Uncheck Item" + myDoesdelete.get(position).getTitledoes(), Toast.LENGTH_SHORT).show();
//                            selectedItem.remove(object);
//                        }
//                        else
//                        selectedItem.add(position);
//                        Toast.makeText(context, "Checked Item"+ myDoesdelete.get(position).getTitledoes(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//        });
// Chua Chay Ket Thuc

        //khong nhan duoc position check 1
//               if(deleteButton.positionCheck==i)
//        {
//            myViewHolder.ck.setChecked(true);
//            deleteButton.positionCheck=-1;
//        }
//Ket thuc 1

        //check action mode
    //Loi khong nhan duoc action mode tu Delete Button 2
//        if(deleteButton.isActionMode)
//        {
//            Toast.makeText(context, "Action mode is turning on please keep chose task or delete:", Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(context, "Action mode is turning on:", Toast.LENGTH_SHORT).show();
//        }
        //Ket thuc 2

        //Khong the goi phuong thuc deleteButton de xoa cac check item da check 3
//        myViewHolder.mview.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                deleteButton.startSelection(i);
// //               Toast.makeText(context, "You choose :"+ String.valueOf(deleteButton.startSelection(i)), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        //ThanhCong****************
        myViewHolder.mview.setOnClickListener(new View.OnClickListener() {
            Intent intent=new Intent(context,DeleteButton.class);
            Bundle bundle=new Bundle();
            int position=myViewHolder.getLayoutPosition();
            @Override
            public void onClick(View v) {
                {
                    Toast.makeText(context, " Checked vi tri " + position+ ""+getTitleDoes, Toast.LENGTH_SHORT).show();
                    //gui gia tri vao bundel
                    bundle.putInt(String.valueOf("Vitri"+position),position+1);
                    intent.putExtra("Mylist",bundle);
                    //Gui bundel di
                    context.startActivity(intent);
                }

            }
        });
//Ket Thuc Thanh Conggg************

//        myViewHolder.ck.setOnClickListener(new View.OnClickListener() {
//            Intent intent=new Intent(context,DeleteButton.class);
//            Bundle bundle=new Bundle();
//            int position=myViewHolder.getLayoutPosition();
//            @Override
//            public void onClick(View v)
//            {
//                    Toast.makeText(context, " Checked vi tri " + position+ ""+getTitleDoes, Toast.LENGTH_SHORT).show();
//                    //gui gia tri vao bundel
//                    bundle.putInt(String.valueOf("Vitri"+position),position+1);
//                    intent.putExtra("Mylist",bundle);
//                    //Gui bundel di
//                    context.startActivity(intent);
//            }
//
//        });


//        //Danh sach item doi xoa
//        WaitDelete=new ArrayList<>();
//
        //Ket thuc 3

    }

    public ArrayList select()
    {
        return selectedItem;
    }

    @Override
    public int getItemCount() {
        return myDoesdelete.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        View mview;
        TextView titledoesdelete, descdoesdelete, datedoesdelete,timedoesdelete;
        CheckBox ck;
//        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titledoesdelete =  itemView.findViewById(R.id.titledoesdelete);
            descdoesdelete = itemView.findViewById(R.id.descdoesdelete);
            datedoesdelete =  itemView.findViewById(R.id.datedoesdelete);
            timedoesdelete =  itemView.findViewById(R.id.timedoesdelete);
            ck= itemView.findViewById(R.id.checkBoxTaskdelete);
//            linearLayout=itemView.findViewById(R.id.linearLayout);

            mview=itemView;
        }
    }
}
