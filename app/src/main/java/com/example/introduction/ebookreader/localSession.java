package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class localSession {
    public  static  final  String SESSION ="Session";
    public  static  final  String U_EMAIL ="email";
    public  static  final  String U_ID ="user_id";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public localSession(Context context){
        //to define the interfaces
        pref= PreferenceManager.getDefaultSharedPreferences(context); //allocating space for the app context
        editor=pref.edit();// to provide the writable access
    }
    public  void addData(String property, String data){
        editor.putString(property,data);
        editor.commit();
    }

    public String checkData(String prop){
        return pref.getString(prop,"");
    }

    public  void  deleteSpace(){
        editor.clear();
        editor.commit();
    }

    public  void  delSpecific(String prop){
        editor.remove(prop);
        editor.commit();
    }

    public  void  holdSession(){
        editor.putBoolean(SESSION,true);
        editor.commit();
    }

    public  boolean checkSession(){
        return  pref.getBoolean(SESSION,false);
    }

}
