package com.example.swg.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Entry implements Serializable {
    public String key;
    public String data;
    public String date;
    public String time;
    public List<String> images;

    public Entry() {
    }

    public Entry(String data) {
        images=new ArrayList<>();
        this.data = data;
        setTime();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addImages(List<String> images){
        this.images=images;
    }
    @SuppressLint("SimpleDateFormat")
    private void setTime(){
        Date date = Calendar.getInstance().getTime();
        this.date=date.getDay()+" ";
        switch (date.getMonth()){
            case 1:
                this.date+="січня";
                break;
            case 2:
                this.date+="лютого";
                break;
            case 3:
                this.date+="березня";
                break;
            case 4:
                this.date+="квітня";
                break;
            case 5:
                this.date+="травня";
                break;
            case 6:
                this.date+="червня";
                break;
            case 7:
                this.date+="липня";
                break;
            case 8:
                this.date+="серпня";
                break;
            case 9:
                this.date+="вересня";
                break;
            case 10:
                this.date+="жовтня";
                break;
            case 11:
                this.date+="листопада";
                break;
            case 12:
                this.date+="грудня";
                break;
        }
        DateFormat dateFormat = new SimpleDateFormat(" yyy");
        this.date += dateFormat.format(Calendar.getInstance().getTime());
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        time = dateFormat.format(Calendar.getInstance().getTime());
    }
}
