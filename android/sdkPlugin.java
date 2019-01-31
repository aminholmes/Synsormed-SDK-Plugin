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
import org.apache.cordova.PluginResult;

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
	private FingerOximeter mFingerOximeter;

	/* Callback linked to giving data back from CMI_POD1W device */
	private CallbackContext CMI_POD1W_Connect_Callback = null;
	private CallbackContext CMI_POD1W_Subscribe_Callback = null;
	private CallbackContext CMI_POD1W_Disconnect_Callback = null;
	private CallbackContext CMI_POD1W_Unsubscribe_Callback = null;
	private boolean CMI_POD1W_Connected = false;

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
		if (mBleOpertion != null) {
			mBleOpertion.disConnect();
			mBleOpertion.closeACSUtility();
		}
	}

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

		if (action.equals("init")){
            
            Log.d("Aminlog","I am waiting for the init to finish");
            callbackContext.success();

            return true;

        } else if(action.equals("CMI_POD1W_Connect")){

        	Log.d("SynsorMed","*** Going to attempt to connect to CMI_POD1W");
        	String podAddress = null;

        	try{
        		/*The connection function is called with only one value passed, so we get the 0 element for the address*/
        		podAddress = data.getString(0);
        	}catch(JSONException e){
        		Log.d("Synsormed","** Exception parsing JSON: " + e);
        	}

        	if(podAddress != null){
        		final String finalPodAddress = podAddress;
        		new Thread() {
					@Override
					public void run() {
						super.run();
						Log.d("AminLog","About to try to connect to address: " + finalPodAddress);
						mBleOpertion.connect(finalPodAddress);
					}
				}.start();

				CMI_POD1W_Connect_Callback = callbackContext;
        	}
        	

        	return true;

        } else if(action.equals("CMI_POD1W_Disconnect")){

        	if (mBleOpertion != null) {
				mBleOpertion.disConnect();
				mBleOpertion.closeACSUtility();
			}

			CMI_POD1W_Disconnect_Callback = callbackContext;
			CMI_POD1W_Disconnect_Callback.success("Disconnect Complete");

        	return true;

        } else if(action.equals("CMI_POD1W_Subscribe")){

        	CMI_POD1W_Subscribe_Callback = callbackContext;

        	if(CMI_POD1W_Connected){ 		
        		mFingerOximeter = new FingerOximeter(new BLEReader(mBleOpertion), new BLESender(mBleOpertion), new FingerOximeterCallBack());
				mFingerOximeter.Start();
        	}else{
        		CMI_POD1W_Subscribe_Callback.error("CMI_POD1W not connected so cannot subscribe");
        	}
        	
        	return true;

        } else if(action.equals("CMI_POD1W_Unsubscribe")){
        	CMI_POD1W_Unsubscribe_Callback = callbackContext;
        	if(mFingerOximeter != null){
        		mFingerOximeter.Stop();
        		mFingerOximeter = null;
        		CMI_POD1W_Unsubscribe_Callback.success("CMI_POD1W Subscription stopped");
        	}else{
        		CMI_POD1W_Unsubscribe_Callback.error("CMI_POD1W cannot unsubscribe, not currently subscribed");
        	}
        	return true;
        }
        else {
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
			Log.d("Synsormed","*** Successfully connected BLE with CMI_POD1W");
			CMI_POD1W_Connected = true;
			PluginResult result = new PluginResult(PluginResult.Status.OK, "Successfully Connected");
			result.setKeepCallback(true);
			CMI_POD1W_Connect_Callback.sendPluginResult(result);
		}

		@Override
		public void onConnectFail() {
			Log.d("Synsormed", "*** Connection failed to BLE with CMI_POD1W");
			PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Failed to connect to CMI_POD1W");
			result.setKeepCallback(true);
			CMI_POD1W_Connect_Callback.sendPluginResult(result);
		}

		@Override
		public void onSended(boolean isSend) {
		}

		@Override
		public void onDisConnect(blePort prot) {
			Log.d("Synsormed","Was disconnected to BLE");
		}

		@Override
		public void onReadyForUse() {
			System.out.println("onReadyForUse");
		}


    }

    class FingerOximeterCallBack implements IFingerOximeterCallBack {

		@Override
		public void OnGetSpO2Param(int nSpO2, int nPR, float fPI, boolean nStatus, int nMode, float nPower) {			
			Log.d("Aminlog","I just got an SPO2 Param");
			Bundle data = new Bundle();
			data.putInt("nSpO2", nSpO2);
			data.putInt("nPR", nPR);
			data.putFloat("fPI", fPI);
			data.putFloat("nPower", nPower);
			data.putBoolean("nStatus", nStatus); 
			data.putInt("nMode", nMode);
			data.putFloat("nPower", nPower);
			
			for (String key : data.keySet())
			{
			    Log.d("Bundle Debug", key + " = \"" + data.get(key) + "\"");
			}

			String dataString = "{\"SpO2\":" + String.valueOf(nSpO2) + ",\"pulse\":" + String.valueOf(nPR) + "}";

			PluginResult result = new PluginResult(PluginResult.Status.OK, dataString);
			result.setKeepCallback(true);
			CMI_POD1W_Subscribe_Callback.sendPluginResult(result);
		}

		@Override
		public void OnGetSpO2Wave(List<Wave> wave) {
		}

		@Override
		public void OnGetDeviceVer(int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
		}

		@Override
		public void OnConnectLose() {
		}
	}

}