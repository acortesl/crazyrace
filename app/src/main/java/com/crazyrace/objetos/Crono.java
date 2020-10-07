package com.crazyrace.objetos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex on 30/10/17.
 */

public class Crono {

    private Date startTime;
    private Date endTime;
    private Date currentDate;
    private SimpleDateFormat formatter;

    public Crono(){
        currentDate = new Date();
        this.formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
    }
    public void start(){
        startTime = new Date();
    }

    public String stop(){
        endTime = new Date();
        return getCrono();
    }
    public String getCrono(){
        if(startTime == null) return "";
        currentDate.setTime(getTime());
        return formatter.format(currentDate);
    }
    public long getTime(){
        if(startTime == null) return 0;
        long time;
        if(endTime != null){
            time = (endTime.getTime() - startTime.getTime());
        }else {
            time = ((new Date()).getTime() - startTime.getTime());
        }
        return time;
    }
}
