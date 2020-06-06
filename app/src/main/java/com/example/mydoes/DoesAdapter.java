package com.example.mydoes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder>{

    Context context;
    ArrayList<MyDoesApp> myDoes;

    public DoesAdapter(Context c, ArrayList<MyDoesApp> p) {
        context = c;
        myDoes = p;
    }

    //create a task base on item_does layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does,viewGroup, false));
    }

    //bind thông tin đã nhập vào task (item_does) và hiển thị ra
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.titledoes.setText(myDoes.get(i).getTitledoes());
        myViewHolder.descdoes.setText(myDoes.get(i).getDescdoes());

        myViewHolder.datedoes.setText(myDoes.get(i).getDatedoes());
        myViewHolder.timedoes.setText(myDoes.get(i).getTimedoes());

        //lấy title,desc, date của task để chuyển qua edit task
        final String getTitleDoes=myDoes.get(i).getTitledoes();
        final String getDescDoes=myDoes.get(i).getDescdoes();
        final String getDateDoes=myDoes.get(i).getDatedoes();
        final String getKeyDoes=myDoes.get(i).getKeydoes();
        final String getTimeDoes=myDoes.get(i).getTimedoes();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent EditTask = new Intent(context,EditTaskDesk.class);
                EditTask.putExtra("titledoes",getTitleDoes);
                EditTask.putExtra("descdoes",getDescDoes);
                EditTask.putExtra("datedoes",getDateDoes);
                EditTask.putExtra("timedoes",getTimeDoes);
                EditTask.putExtra("keydoes",getKeyDoes);
                context.startActivity(EditTask);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titledoes, descdoes, datedoes,timedoes;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titledoes = (TextView) itemView.findViewById(R.id.titledoes);
            descdoes = (TextView) itemView.findViewById(R.id.descdoes);
            datedoes = (TextView) itemView.findViewById(R.id.datedoes);
            timedoes = (TextView) itemView.findViewById(R.id.timedoes);


        }
    }

}