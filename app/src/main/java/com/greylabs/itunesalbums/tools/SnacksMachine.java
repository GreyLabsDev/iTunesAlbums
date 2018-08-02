package com.greylabs.itunesalbums.tools;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.HashMap;

public class SnacksMachine {

    private static HashMap<String, Integer> colorMap = new HashMap<>();

    private static View currentView;

    public static void initColors(){
        colorMap.put("red", 0xfff44336);
        colorMap.put("green", 0xff4caf50);
        colorMap.put("blue", 0xff2195f3);
        colorMap.put("orange", 0xffffc107);
    }

    public static void setCurrentView(View inputView) {
        currentView = inputView;
    }

    public static void showSnackbar(String text, String color){
        Snackbar msgSnackbar = Snackbar.make(currentView, text, Snackbar.LENGTH_SHORT);
        msgSnackbar.getView().setBackgroundColor(colorMap.get(color));
        msgSnackbar.show();
    }

    public static void showLongSnackbar(String text, String color){
        Snackbar msgSnackbar = Snackbar.make(currentView, text, Snackbar.LENGTH_LONG);
        msgSnackbar.getView().setBackgroundColor(colorMap.get(color));

        msgSnackbar.show();
    }

    public static void showActionSnackbar(String text, String color, String button, View.OnClickListener clickListener){
        Snackbar msgSnackbar = Snackbar.make(currentView, text, Snackbar.LENGTH_SHORT);
        msgSnackbar.setAction(button, clickListener);
        msgSnackbar.getView().setBackgroundColor(colorMap.get(color));
        msgSnackbar.show();
    }
}
