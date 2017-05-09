package com.easyappointments.common;


import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by matteo on 09/05/17.
 */

public final class Validator {
    public static boolean absoluteUrlIsValid(String uri){

        if(!uri.endsWith("/"))
            uri += "/";

        Uri u = Uri.parse(uri);

        return u.isAbsolute() && !TextUtils.isEmpty(u.getPath());
    }
}
