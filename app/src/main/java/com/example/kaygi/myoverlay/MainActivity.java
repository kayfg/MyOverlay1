package com.example.kaygi.myoverlay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.kaygi.myoverlay.StaticService.getVib;
import static com.example.kaygi.myoverlay.StaticService.setActive;
import static com.example.kaygi.myoverlay.StaticService.setAlarmtime;
import static com.example.kaygi.myoverlay.StaticService.setStop;


public class MainActivity extends Activity {
    Button startService, stopService, stopApp;
    EditText alarmtime;
    private final BroadcastReceiver abcd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setActive(false);
            finish();
            System.exit(0);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService=(Button)findViewById(R.id.startService);
        stopService=(Button)findViewById(R.id.stopService);
        stopApp=(Button)findViewById(R.id.stopApp);
        alarmtime=(EditText)findViewById(R.id.alarmtime);
        registerReceiver(abcd, new IntentFilter("xyz"));

        if(startService!=null){
            startService.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startService(new Intent(getApplication(), ChatHeadService.class));

                }
            });
        }
        if(stopService!=null) {
            stopService.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    setStop(true);
                    stopService(new Intent(getApplication(), ChatHeadService.class));
                    getVib().cancel();
                }
            });

        if(alarmtime!=null){
            alarmtime.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    try
                    {
                        // you can call or do what you want with your EditText here
                        if (Integer.parseInt(alarmtime.getText().toString())>0) {
                        setAlarmtime(Integer.parseInt(alarmtime.getText().toString()));
                        }
                        else
                        {
                        setAlarmtime(30);
                        Toast.makeText(MainActivity.this, "Alarmtime set to 30", Toast.LENGTH_SHORT).show();
                        }
                    }catch(NumberFormatException nfe){
                        Toast.makeText(MainActivity.this, "NumberFormatException "+nfe.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println( "NumberFormatException "+nfe.getMessage());
                        setAlarmtime(30);
                    }
                    catch(Exception e){
                        Toast.makeText(MainActivity.this, "Another exception "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

        }

        }

        stopApp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setActive(false);
                finish();
                System.exit(0);

            }
        });

    }

    @Override
    public void onStart() {
        setActive(true);
        super.onStart();

    }

    @Override
    public void onStop() {
        setActive(false);
        super.onStop();

    }
    @Override
    public void onDestroy(){
        setActive(false);
        super.onDestroy();
        unregisterReceiver(abcd);
    }

}