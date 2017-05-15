package com.easyappointments.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by matte on 15/05/2017.
 */

public class Formatter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String formatDate(Date dt){
        return sdf.format(dt);
    }
}
