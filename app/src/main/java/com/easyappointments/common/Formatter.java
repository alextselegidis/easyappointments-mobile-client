package com.easyappointments.common;

import android.content.res.Resources;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by matte on 15/05/2017.
 */

public class Formatter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static NumberFormat priceFormatter = new DecimalFormat("#0.00");

    public static String formatDate(Date dt){
        return sdf.format(dt);
    }

    public static String formatPrice(double price, String currency) {
        String s = null;
        try{
            Currency c = Currency.getInstance(currency);
            s = c.getSymbol(Locale.getDefault());
        }catch (Exception ex){
            s = currency;
        }
        return s+" "+priceFormatter.format(price);
    }
}
