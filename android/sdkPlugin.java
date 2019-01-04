package com.synsormed.mobile;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class sdkPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("sayHello")) {

        	Log.d("AminLog", "I am trying to say hello");
            String message = "Hello, Amin" ;
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }
}