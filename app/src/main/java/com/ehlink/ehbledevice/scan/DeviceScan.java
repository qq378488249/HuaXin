package com.ehlink.ehbledevice.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;


public class DeviceScan implements BluetoothAdapter.LeScanCallback{
	private BluetoothAdapter mBluetoothAdapter;
	private Handler mHandler;
	
	/** check if BLE Supported device */
    public static boolean isBLESupported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /** get BluetoothManager */
    public static BluetoothManager getManager(Context context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }
	
	
	private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, 10000);

            //mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            //mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
	
	// Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        		

        }
    };


	@Override
	public void onLeScan(BluetoothDevice newDevice, int arg1, byte[] ScanData) {
		// TODO Auto-generated method stub
		
	}

}
