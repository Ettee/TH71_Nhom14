package com.example.mydoes;

import android.widget.CheckBox;

public class MyDoesApp {
    String titledoes;
    String datedoes;
    String timedoes;
    String descdoes;
    String keydoes;
    CheckBox checkbox;


    public MyDoesApp() {
    }

    public MyDoesApp(String titledoes, String datedoes, String timedoes, String descdoes, String keydoes,  CheckBox checkbox) {
        this.titledoes = titledoes;
        this.datedoes = datedoes;
        this.timedoes = timedoes;
        this.descdoes = descdoes;
        this.keydoes = keydoes;
       this.checkbox= checkbox;
    }

    public String getTimedoes() {
        return timedoes;
    }

    public void setTimedoes(String timedoes) {
        this.timedoes = timedoes;
    }

    public String getKeydoes() {
        return keydoes;
    }
    public void setKeydoes(String keydoes) {
        this.keydoes = keydoes;
    }

    public String getTitledoes() {
        return titledoes;
    }

    public void setTitledoes(String titledoes) {
        this.titledoes = titledoes;
    }

    public String getDatedoes() {
        return datedoes;
    }

    public void setDatedoes(String datedoes) {
        this.datedoes = datedoes;
    }

    public String getDescdoes() {
        return descdoes;
    }

    public void setDescdoes(String descdoes) {
        this.descdoes = descdoes;
    }
//Thinh
    public int getCheckbox()
    {
        if (checkbox.isChecked())
             return 1;
        else
            return 0;

    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
//Thinh
}
