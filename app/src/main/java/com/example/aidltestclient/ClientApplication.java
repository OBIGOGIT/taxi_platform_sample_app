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

import androidx.multidex.MultiDexApplication;

import com.obigo.nativelib.NCommunicator;

public class ClientApplication extends MultiDexApplication {
    private static ClientApplication mInstance;

    public static ClientApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        NCommunicator.getInstance().init(this);
        NCommunicator.getInstance().connect();
    }

    public void connectService() {
        NCommunicator.getInstance().connect();
    }
}
