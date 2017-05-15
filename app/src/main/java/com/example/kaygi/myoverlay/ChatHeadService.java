package com.example.kaygi.myoverlay;

/**
 * Created by kaygi on 16.04.2016.
 *
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;

import static com.example.kaygi.myoverlay.StaticService.getVib;
import static com.example.kaygi.myoverlay.StaticService.isActive;
import static com.example.kaygi.myoverlay.StaticService.setActive;
import static com.example.kaygi.myoverlay.StaticService.setStop;
import static com.example.kaygi.myoverlay.StaticService.setVib;
import static java.lang.Math.abs;


public class ChatHeadService extends Service implements SensorEventListener {

    private WindowManager windowManager;
    private Button chatHead, deletion, counter;
    private boolean resize=false;
    private boolean init=false;
    private boolean click2=false;
    boolean on=false;
    long a, c=0, d=0, e=0;
    private long shortclick, doubleclick;
    WindowManager.LayoutParams params, paramsDel, params1;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;




    @Override
    public void onCreate() {
        super.onCreate();
        setVib((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        counter = new Button(this);
        chatHead = new Button(this);
        deletion= new Button(this);
        counter.setText("test");
        deletion.setText("Delete");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        //chatHead.setImageResource(R.drawable.face1);

        chatHead.setText("Hallooo");
        //chatHead.setBackgroundResource(R.drawable.roundedbutton);
        final Handler handler = new Handler();
        final Runnable mLongPressed = new Runnable() {
            public void run() {
                Log.i("", "Long press!");
                resize=!resize;
                //chatHead.setText(resize+"");

            }
        };


        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params1= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params1.x=params.x+params.width;


        paramsDel= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //paramsDel.gravity = Gravity.BOTTOM | Gravity.CENTER;
        paramsDel.x = 0;
        paramsDel.y = 1600;
        final Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private float width;
            private float height;
            private float newX;
            private float newY;
            private boolean delAdd=false, move=false;
            Test atest =new Test();





            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shortclick=new Date().getTime();
                        if((shortclick-doubleclick)>500)
                        {
                            click2=false;
                        }
                        handler.postDelayed(mLongPressed, 1000);
                        init=true;
                        width = v.getWidth() / 2;
                        height =v.getHeight()/2;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(params.y>=1500)
                        {
                            setStop(true);
                            getVib().cancel();
                            stopService(new Intent(getApplication(), ChatHeadService.class));
                        }
                        if(click2 && (new Date().getTime()-shortclick)<200)//doubleclick
                        {
                            if(move)
                            {}
                            else if(isActive()) {
                                Toast.makeText(ChatHeadService.this, "test2", Toast.LENGTH_SHORT).show();
                                setActive(false);
                                sendBroadcast(new Intent("xyz"));//finish activity
                                //windowManager.addView(counter, params1);  ###test###
                                //windowManager.removeView(counter);  ###test###
                            }
                            else{
                                Toast.makeText(ChatHeadService.this, "test3", Toast.LENGTH_SHORT).show();
                                setActive(true);
                                startActivity(dialogIntent);//start acitivity
                                //windowManager.addView(counter, params1);  ###test###
                            }

                        }
                        else if((new Date().getTime()-shortclick)>200 && (new Date().getTime()-shortclick)<700)//1 shortclick
                        {
                            if(!move)
                            {
                                Toast.makeText(ChatHeadService.this, "test1", Toast.LENGTH_SHORT).show();
                                on=!on;
                                if(on)
                                {
                                    a=new Date().getTime();
                                    setStop(false);
                                    Toast.makeText(ChatHeadService.this,"on",Toast.LENGTH_SHORT).show();
                                    atest.getDateAndTime(chatHead, a);
                                }
                                else
                                {
                                    d= new Date().getTime();
                                    setStop(true);
                                    Toast.makeText(ChatHeadService.this,"off",Toast.LENGTH_SHORT).show();
                                    //chatHead.setText((((c+e)/60000)+":"+((c+e)/1000)%60));
                                    chatHead.setText("00:00");
                                    e+=c;
                                }
                            }


                        }
                        if(delAdd==true)
                        {
                            windowManager.removeView(deletion);
                        }
                        handler.removeCallbacks(mLongPressed);
                        resize=false;
                        click2=true;
                        delAdd=false;
                        move=false;
                        doubleclick=new Date().getTime();
                        //chatHead.setText(resize+"");
                        //chatHead.setText(params.y+"");
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        delAdd=true;

                        if(init==true)
                        {
                            windowManager.addView(deletion, paramsDel);
                            init=false;
                        }
                        newX=event.getRawX();
                        newY=event.getRawY();
                        if(abs(newX-initialTouchX)<10 && abs(newY-initialTouchY)<10)
                        {}
                        else{
                            move=true;
                            handler.removeCallbacks(mLongPressed);
                        }

                        if(resize==false)
                        {
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);

                        }
                        else
                        {
                            if(newX>params.x)
                                if((newX-params.x)>100)
                                    params.width = (int) ((newX-params.x));

                            if(newY>params.y)
                                if((newY-params.y)>100)
                                    params.height = (int) ((newY-params.y));
                        }
                        params1.x=params.x+params.width;
                        windowManager.updateViewLayout(chatHead, params);

                        return true;
                }
                return false;
            }
        });

        windowManager.addView(chatHead, params);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                setStop(true);

            } else {
                //far
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }

            System.out.printf("Proximity value: "+String.valueOf(event.values[0]));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
