package de.tjanneck.migronitor.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Janna on 23.12.2014.
 */
public class ZeitSklave {
    private static SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
    public static double DateToGraph(Date d){
        String hString = dateFormater.format(d);
        String[] helper = hString.split(":");
        double graph = Double.parseDouble(helper[0]) *60*60;
        graph += Double.parseDouble(helper[1]) *60;
        return  graph;
    }
    public static String LongToTime(long l){
        long h = l / (1000 * 60 * 60);
        long m = (l - h * 60 * 60 * 1000) / (1000 * 60);
        String zeit = "";
        if(h < 10){
            zeit += "0" + h;
        }else{
            zeit += h;
        }
        zeit += ":";
        if(m < 10){
            zeit += "0" + m;
        }else{
            zeit += m;
        }
        //Date d = new Date((long) (1000* value));
        return zeit;
    }
}
