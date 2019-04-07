package com.patrickmumot.eatingdice.Models;

/**
 * Created by User on 3/26/2018.
 */

public class Period {
    public PeriodDetails PeriodOpen;
    public PeriodDetails PeriodClose;
    public Period(){
        PeriodClose = new PeriodDetails();
        PeriodOpen = new PeriodDetails();
    }
}
