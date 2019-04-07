package com.patrickmumot.eatingdice.Models;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/26/2018.
 */

public class OpenHours {
    public boolean OpenNow = false;
    public List<Period> Periods;

    public OpenHours(){
        Periods = new ArrayList<>();
    }
}
