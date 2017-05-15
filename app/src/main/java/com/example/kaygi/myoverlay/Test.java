package com.example.kaygi.myoverlay;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;

import static com.example.kaygi.myoverlay.StaticService.getAlarmtime;
import static com.example.kaygi.myoverlay.StaticService.getVib;
import static com.example.kaygi.myoverlay.StaticService.isStop;


/**
 * Created by kaygi on 02.05.2016.
 */
public class Test extends AppCompatActivity {
    boolean on=false;
    long b, c=0, d=0, e=0;
    String text;
    long[] pattern = {0, 100, 1000};



    public void getDateAndTime(final Button myb, final long a){

        Thread dtThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isStop()){
                    try{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String date = new Date().toString();
                                b=new Date().getTime();
                                c=b-a;
                                if((((c+e)/1000)%60)==getAlarmtime())
                                {
                                    getVib().vibrate( pattern, 0);
                                }
                                myb.setText((((((c+e)/60000)<10)?"0":"")+((c+e)/60000)+":"+(((((c+e)/1000)%60)<10)?"0":"")+((c+e)/1000)%60));
                            }
                        });
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Log.d("getDateAndTime", "thread interrupted !");
                    }
                }
                getVib().cancel();
            }
        });
        dtThread.start();
    }
}
