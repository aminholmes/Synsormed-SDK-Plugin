package com.synsormed.mobile;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate.Wave;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;

public class sdkPlugin extends CordovaPlugin {


	private BLEOpertion mBleOpertion;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("sayHello")) {

        	Log.d("AminLog", "I am trying to say hello");
        	try {
				mBleOpertion = new BLEOpertion(this, new IBLECallBack());
			} catch (Exception e) {
				e.printStackTrace();
			}

			mBleOpertion.discovery();

            String message = "Hello, Amin" ;
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }
}