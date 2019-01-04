package com.synsormed.mobile;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.List;

import android.util.Log;
import android.content.Context;

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


	Context context = this.cordova.getActivity().getApplicationContext();

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("sayHello")) {

        	Log.d("AminLog", "I am trying to say hello");
        	try {
				mBleOpertion = new BLEOpertion(context, new BleCallBack());
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


    class BleCallBack implements IBLECallBack {

    	@Override
    	public void onDiscoveryCompleted(List<blePort> device) {
			System.out.println("onDiscoveryCompleted");
		}

		@Override
		public void onFindDevice(final blePort port) {
			System.out.println("搜索到设备：" + port._device.getAddress() + "  " + port._device.getName());
			
			myHandler.obtainMessage(MSG_BLUETOOTH_STATE,"搜索到设备：" + port._device.getAddress() + "  " + port._device.getName() + " " /*+ port.devInfo*/)
					.sendToTarget();
			if ("PC-60NW-1".equals(port._device.getName().trim())||
					"POD".equals(port._device.getName().trim())||
					"PC-68B".equals(port._device.getName().trim()) ) {
				mBleOpertion.stopDiscover();
				myHandler.obtainMessage(MSG_BLUETOOTH_STATE, "开始连接").sendToTarget();
				new Thread() {

					@Override
					public void run() {
						super.run();
						mBleOpertion.connect(port);
					}
				}.start();
			}
		}

		@Override
		public void onConnected(blePort port) {
			myHandler.obtainMessage(MSG_BLUETOOTH_STATE, "连接成功：" + port._device.getName()).sendToTarget();
			mFingerOximeter = new FingerOximeter(new BLEReader(mBleOpertion), new BLESender(mBleOpertion), new FingerOximeterCallBack());
			mFingerOximeter.Start();
			//发送波形请求
			mFingerOximeter.SetWaveAction(true);
		}

		@Override
		public void onConnectFail() {
			myHandler.obtainMessage(MSG_BLUETOOTH_STATE, "连接失败").sendToTarget();
			if (mFingerOximeter != null)
				mFingerOximeter.Stop();
			mFingerOximeter = null;
		}

		@Override
		public void onSended(boolean isSend) {
			System.out.println("发送数据:" + isSend);
			myHandler.obtainMessage(MSG_BLUETOOTH_STATE, "发送数据:" + isSend).sendToTarget();
		}

		@Override
		public void onDisConnect(blePort prot) {
			myHandler.obtainMessage(MSG_BLUETOOTH_STATE, "连接断开").sendToTarget();
			mFingerOximeter.Stop();
			mFingerOximeter = null;
		}

		@Override
		public void onReadyForUse() {
			System.out.println("onReadyForUse");
		}


    }

}