package mx.com.vialogika.mistclient.Utils;

import mx.com.vialogika.mistclient.R;

public class Color {

    public static int reportIconColor(String grade){
        int color = R.color.dsc_gray;
        switch(grade){
            case "gray":
                color = R.color.dsc_gray;
                break;
            case "green":
                color = R.color.dsc_success;
                break;
            case "yellow":
                color =  R.color.dsc_warning;
                break;
            case "red":
                color =  R.color.dsc_danger;
                break;
        }
        return color;
    }
}
