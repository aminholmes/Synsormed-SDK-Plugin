package com.synsormed.mobile;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.List;

import android.util.Log;
import android.content.Context;
import android.os.Bundle;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import com.bde.parentcyTransport.ACSUtility.blePort;
import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate.Wave;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;

public class sdkPlugin extends CordovaPlugin {


	private BLEOpertion mBleOpertion;
	private Context context;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova,webView);

		context = this.cordova.getActivity().getApplicationContext();
						
		try {
			mBleOpertion = new BLEOpertion(context, new BleCallBack());
			Log.d("Aminlog","I just created mBleOpertion");
		} catch (Exception e) {
			Log.d("Aminlog","There was an error creating mBleOpertion: " + e);
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

    	

        if (action.equals("sayHello")) {

        	Log.d("AminLog", "I am trying to say hello");

        	if(mBleOpertion.isCanUseBLE(context)){
        		Log.d("AminLog", "BLE can be used");


        		//mBleOpertion.startDiscover();

        	}else{
        		Log.d("AminLog", "BLE no where");
        	}

			new Thread() {

				@Override
				public void run() {
					super.run();
					final String podString = "84:EB:18:7B:79:38";
					Log.d("AminLog","About to try to connect to address: " + podString);
					mBleOpertion.connect(podString);
				}
			}.start();

            String message = "Hello, Amin" ;
            callbackContext.success(message);

            return true;

        } else if (action.equals("init")){
            
            Log.d("Aminlog","I am waiting for the init to finish");
            callbackContext.success();

            return true;

        } else {
        	return false;
        }
    }


    class BleCallBack implements IBLECallBack {

    	@Override
    	public void onDiscoveryCompleted(List<blePort> device) {
			Log.d("AminLog", "onDiscoveryCompleted");
		}

		@Override
		public void onFindDevice(final blePort port) {
			Log.d("AminLog", "I just found a Device");
		}

		@Override
		public void onConnected(blePort port) {
			Log.d("AminLog","Successfully connected BLE");
		}

		@Override
		public void onConnectFail() {
			Log.d("AminLog", "Connection failed to BLE");
		}

		@Override
		public void onSended(boolean isSend) {
		}

		@Override
		public void onDisConnect(blePort prot) {
			Log.d("AminLog","Was disconnected to BLE");
		}

		@Override
		public void onReadyForUse() {
			System.out.println("onReadyForUse");
		}


    }

}