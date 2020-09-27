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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private String requestVehicleIdKey;
    private String requestBrakePressedKey;
    private String requestOdometerKey;
    private String requestRpmKey;
    private String requestGearKey;
    private String requestDoorKey;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConnectionCallback();

        Button btnSubscribeVehicleId = (Button) findViewById(R.id.btn_subscribeVehicleId);
        final TextView tvVehicleId = findViewById(R.id.tv_vehicleId);

        Button btnSubscribeBrakePressed = (Button) findViewById(R.id.btn_subscribeBrake);
        final TextView tvBrakePressed = findViewById(R.id.tv_brakePressed);

        Button btnSubscribeOdometer = (Button) findViewById(R.id.btn_subscribeOdometer);
        final TextView tvOdometer = findViewById(R.id.tv_odometer);


        Button btnSubscribeRpm = (Button) findViewById(R.id.btn_subscribeRpm);
        final TextView tvRpm = findViewById(R.id.tv_rpm);

        Button btnSubscribeSpeed = (Button) findViewById(R.id.btn_subscribeSpeed);
        final TextView tvSpeed = findViewById(R.id.tv_speed);

        Button btnSubscribeGear = (Button) findViewById(R.id.btn_subscribeGear);
        final TextView tvGear = findViewById(R.id.tv_gear);

        Button btnSubscribeDoor = (Button) findViewById(R.id.btn_subscribeDoor);
        final TextView tvDoor = findViewById(R.id.tv_door);

        Button btnUnsubscribe = (Button) findViewById(R.id.btn_unsubscribe);

        btnSubscribeVehicleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick", "isConnected " + isConnected);
                if (isConnected) {
                    try {
                        requestVehicleIdKey = mVehicle.getVehicleId(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle Id monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    final String vin = value.getString("vin");
                                    final String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "vehicle Id : "+ vin);
                                    // TODO : Use vehicleID value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvVehicleId.setText(vin + ", updateTime = " + updateTime);
                                        }
                                    });
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
        btnSubscribeBrakePressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestBrakePressedKey = mVehicle.getBrakeOperation(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "BrakePressed monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    /**
                                     * brakePedalDepressed : 2(Pressed), 1(Not Pressed)
                                     */
                                    final Integer brakePedalDepressed = value.getInt("brakePedalDepressed");
                                    String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "brakePedalDepressed : "+ brakePedalDepressed);
                                    // TODO : Use brakePedalDepressed value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvBrakePressed.setText(brakePedalDepressed.toString());
                                        }
                                    });
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

        btnSubscribeOdometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestOdometerKey = mVehicle.getOdometer(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "Odometer monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    final Double distanceTotal = value.getDouble("distanceTotal");
                                    final Integer distanceIncrement = value.getInt("distanceIncrement");
                                    String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "distanceIncrement : "+ distanceIncrement);
                                    // TODO : Use Odometer value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvOdometer.setText(distanceTotal + ", " + distanceIncrement);
                                        }
                                    });
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

        btnSubscribeRpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestRpmKey = mVehicle.getEngineSpeed(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "Engine Speed monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    final Double rpmSpeed = value.getDouble("speed");
                                    String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "distanceIncrement : "+ rpmSpeed);
                                    // TODO : Use Odometer value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvRpm.setText(rpmSpeed.toString());
                                        }
                                    });
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

        btnSubscribeSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestVehicleSpeedKey = mVehicle.getVehicleSpeed(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle speed monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    final Double vehicleSpeed = value.getDouble("speed");
                                    final String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "vehicle speed : "+ vehicleSpeed);
                                    // TODO : Use vehicleSpeed value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvSpeed.setText(vehicleSpeed.toString() + ", " + updateTime);
                                        }
                                    });
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

        btnSubscribeGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestGearKey = mVehicle.getTransmission(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "vehicle gear monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    final Integer currentGear = value.getInt("gear");
                                    final Integer gearPosition = value.getInt("mode");
                                    String updateTime = value.getString("updateTime");
                                    /**
                                     * gearPosition : 5(Drive), 6(Normal), 7(Reverse), 0(Park), 14(안걸렸을 때), 15(시동 꺼졌을 때)
                                     */
                                    Log.d(TAG, "currentGear : "+ currentGear);
                                    // TODO : Use Gear value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvGear.setText(currentGear + ", " + gearPosition);
                                        }
                                    });
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

        btnSubscribeDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    try {
                        requestDoorKey = mVehicle.getDoor(Vehicle.EVENT, new Response() {
                            @Override
                            public void onResponse(String s) {
                                Log.d(TAG, "Door monitoring : "+s);
                                try {
                                    JSONObject obj1 = new JSONObject(s);
                                    JSONObject value = obj1.getJSONObject("value");
                                    /**
                                     * Door Status : 1(Open), 0(Closed)
                                     */
                                    final Integer doorFrontRight = value.getInt("doorFrontRight");
                                    final Integer doorRearRight = value.getInt("doorRearRight");
                                    String updateTime = value.getString("updateTime");
                                    Log.d(TAG, "Door : "+ doorFrontRight);
                                    // TODO : Use Gear value for something
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvDoor.setText(doorFrontRight + ", " + doorRearRight);
                                        }
                                    });
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
                        NCommunicator.getInstance().removeCallbackByKey(requestVehicleIdKey);
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
                Log.d("setConnectionCallback", "onConnected");
                isConnected = true;
                mVehicle = NCommunicator.getInstance().getVehicle();
            }

            @Override
            public void onDisconnected() {
                Log.d("setConnectionCallback", "onDisconnected");
                isConnected = false;
                mVehicle = null;

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isConnected) {
            ClientApplication.getInstance().connectService();
        }
    }
    @Override
    protected void onDestroy() {
        NCommunicator.getInstance().removeAllCallback();
        NCommunicator.getInstance().disconnect();
        Log.d("Main", "onDestroy");
        super.onDestroy();
    }
}
