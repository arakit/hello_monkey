/*
 *  Copyright (c) 2011 by Twilio, Inc., all rights reserved.
 *
 *  Use of this software is subject to the terms and conditions of
 *  the Twilio Terms of Service located at http://www.twilio.com/legal/tos
 */

package com.twilio.example.hellomonkey;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.twilio.client.Connection;
import com.twilio.client.Device;
import com.twilio.client.Twilio;

public class MonkeyPhone implements Twilio.InitListener
{
    private static final String TAG = "MonkeyPhone";

    private Device device;
    private Connection connection;

    public MonkeyPhone(Context context)
    {
        Twilio.initialize(context, this /* Twilio.InitListener */);
    }
    
    
    
    public void connect(String phoneNumber)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("PhoneNumber", phoneNumber);
        connection = device.connect(parameters, null /* ConnectionListener */);
        if (connection == null)
            Log.w(TAG, "Failed to create new connection");
    }

    public void disconnect()
    {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    /* Twilio.InitListener method */
    @Override
    public void onInitialized()
    {
        Log.d(TAG, "Twilio SDK is ready");

        try {
        	//http://limitless-tor-7151.herokuapp.com/auth
            //String capabilityToken = HttpHelper.httpGet("http://companyfoo.com/auth.php");
            String capabilityToken = HttpHelper.httpGet("http://limitless-tor-7151.herokuapp.com/auth");

            device = Twilio.createDevice(capabilityToken, null /* DeviceListener */);
        } catch (Exception e) {
            Log.e(TAG, "Failed to obtain capability token: " + e.getLocalizedMessage());
        }
    }

    /* Twilio.InitListener method */
    @Override
    public void onError(Exception e)
    {
        Log.e(TAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
    }

    @Override
    protected void finalize()
    {
        if (device != null)
            device.release();
    }
}
