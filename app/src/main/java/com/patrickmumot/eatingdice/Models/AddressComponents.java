package com.patrickmumot.eatingdice.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/26/2018.
 */

public class AddressComponents {
    public String LongName;
    public String ShortName;
    public List<String> Types;

    public AddressComponents(){
        Types = new ArrayList<>();
    }
}
