/**
 * Copyright 2020 obigo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        Button btn_subscribe = (Button) findViewById(R.id.btn_subscribe);
        Button btn_unsubcribe = (Button) findViewById(R.id.btn_unsubscribe);

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
