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
		} catch (Exception e) {
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

			mBleOpertion.startDiscover();

            String message = "Hello, Amin" ;
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }


    class BleCallBack implements IBLECallBack {

    	@Override
    	public void onDiscoveryCompleted(List<blePort> device) {
			System.out.println("onDiscoveryCompleted");
		}

		@Override
		public void onFindDevice(final blePort port) {
		}

		@Override
		public void onConnected(blePort port) {
		}

		@Override
		public void onConnectFail() {
		}

		@Override
		public void onSended(boolean isSend) {
		}

		@Override
		public void onDisConnect(blePort prot) {
		}

		@Override
		public void onReadyForUse() {
			System.out.println("onReadyForUse");
		}


    }

}