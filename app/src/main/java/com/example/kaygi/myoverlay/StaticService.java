package com.example.kaygi.myoverlay;

import android.os.Vibrator;

/**
 * Created by kaygi on 15.05.2017.
 */

public class StaticService {
    private static int alarmtime=30;
    private static Vibrator vib;
    private static boolean active = true;
    private static boolean stop;

    public static boolean isStop() {
        return stop;
    }
    public static void setStop(boolean stop) {
        StaticService.stop = stop;
    }

    public static boolean isActive() {
        return active;
    }
    public static void setActive(boolean active) {
        StaticService.active = active;
    }



    public static Vibrator getVib() {
        return vib;
    }
    public static void setVib(Vibrator vib) {
        StaticService.vib = vib;
    }



    public static int getAlarmtime() {
        return alarmtime;
    }
    public static void setAlarmtime(int alarmtime) {
        StaticService.alarmtime = alarmtime;
    }




}
