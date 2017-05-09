package com.easyappointments.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by matteo on 09/05/17.
 */

@Table(name = "SettingsModel")
public class SettingsModel extends Model {

    @Column
    public String url;
    @Column
    public String username;
    @Column
    public String password;
    @Column
    public int providerId;
    @Column
    public String email;
    @Column
    public String firstName;
    @Column
    public String lastName;
    @Column
    public boolean rememberme;

    public static SettingsModel loadSettings(){
        SettingsModel s = new Select().from(SettingsModel.class).executeSingle();

        if(s == null){
            s = new SettingsModel();
            s.rememberme = true;
            s.save();
        }

        return s;
    }
}
