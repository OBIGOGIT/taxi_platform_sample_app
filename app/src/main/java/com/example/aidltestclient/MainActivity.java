/**
 * Copyright 2020 OBIGO Inc.
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

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private boolean isConnected = false;
    private Vehicle mVehicle;

    private String requestVehicleSpeedKey;
    private String requestLocationKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConnectionCallback();

        Button btnSubscribe = (Button) findViewById(R.id.btn_subscribe);
        Button btnUnsubscribe = (Button) findViewById(R.id.btn_unsubscribe);

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    try {
                        requestVehicleSpeedKey = mVehicle.getVehicleSpeed(Vehicle.MONITORING, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle speed monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    Integer vehicleSpeed = value.getInt("speed");
                                    Log.d(TAG, "vehicle speed : "+ vehicleSpeed);
                                    // TODO : Use vehicleSpeed value for something

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        requestLocationKey = mVehicle.getCurrentLocation(Vehicle.MONITORING, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "current location MONITORING : " + s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    Double lat = value.getDouble("latitude");
                                    Double lon = value.getDouble("longitude");
                                    Log.d(TAG, "current location : "+ lat + ", " + lon);
                                    // TODO : Use lat, lon value for something

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    try {
                        NCommunicator.getInstance().removeCallbackByKey(requestVehicleSpeedKey);
                        NCommunicator.getInstance().removeCallbackByKey(requestLocationKey);
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
