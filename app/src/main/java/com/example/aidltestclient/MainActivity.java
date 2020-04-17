package com.example.aidltestclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.obigo.nativelib.Response;
import com.obigo.nativelib.Vehicle;
import com.obigo.nativelib.constant.IConnection;
import com.obigo.nativelib.NCommunicator;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btn_aidl;
    private Button btn_subscribe;
    private Button btn_unsubcribe;

    private boolean isConnected = false;
    private Vehicle mVehicle;

    private String constRequestId;
    private String secondRequestId;
    private String eventRequestId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConnectionCallback();

        btn_aidl = findViewById(R.id.btn_aidl);
        btn_subscribe = (Button) findViewById(R.id.btn_subscribe);
        btn_unsubcribe = findViewById(R.id.btn_unsubscribe);

        btn_aidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    try {
                         mVehicle.getFuelInformation(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "fuel event: " + s);
                            }
                        });
                        constRequestId = mVehicle.getVehicleSpeed(Vehicle.CONST, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle speed const : "+s);
                            }
                        });
                        secondRequestId = mVehicle.getVehicleSpeed(Vehicle.MONITORING, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle speed monitoring : "+s);
                            }
                        });
                        eventRequestId = mVehicle.getVehicleSpeed(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle speed event : "+s);
                            }
                        });
                        mVehicle.getCurrentLocation(Vehicle.MONITORING, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "location monitoring : " + s);
                            }
                        });
                        mVehicle.getCurrentLocation(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "location event : " + s);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_unsubcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    try {
                        NCommunicator.getInstance().removeCallbackByKey(constRequestId);
                        NCommunicator.getInstance().removeCallbackByKey(secondRequestId);
                        NCommunicator.getInstance().removeCallbackByKey(eventRequestId);
                        NCommunicator.getInstance().removeAllCallback();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setConnectionCallback() {
        NCommunicator.getInstance().setConnectionCallback(new IConnection.Connection() {
            @Override
            public void onConnected() {
                isConnected = true;
                mVehicle = NCommunicator.getInstance().getVehicle();
            }

            @Override
            public void onDisconnected() {
                isConnected = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        NCommunicator.getInstance().removeAllCallback();
        NCommunicator.getInstance().disconnect();
        Log.d("Main", "onDestroy");
        super.onDestroy();
    }
}
