package com.example.bleAdvertise;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BluetoothAdvertise extends Activity {
    public static final String TAG = "BluetoothAdvertise";
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothLeAdvertiser mAdvertiser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (initPara()) {
            startAdvertise();
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mAdvertiser.stopAdvertising(mAdvCallback);
    }

    private boolean initPara() {
        if (android.os.Build.VERSION.SDK_INT < 21) {
            Log.e(TAG, "The BLE Advertise need the sdk version above 21");
            return false;
        }

        BluetoothManager tManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = tManager.getAdapter();

        // The Emulator doesn't has the Adapter.
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Your Device doesn't support Bluetooth");
            return false;
        }

        if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            Log.e(TAG, "Pls turn on Your BT first");
            return false;

        }
        return true;
    }

    public void startAdvertise() {
        try {
            if (mAdvertiser == null) {
                mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
            }
            if (mAdvertiser != null) {
                mAdvertiser.startAdvertising(advSettings(), advtiseData(this), mAdvCallback);
            } else {
                Log.e(TAG, "Your Device may not support BLE Adv or it is in OFF State");
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.d(TAG, "startAdvertise e=" + e.getMessage());

        }
    }

    public static AdvertiseSettings advSettings() {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW);
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        builder.setConnectable(true);
        return builder.build();
    }

    // advtiseData less than 31 byte
    public static AdvertiseData advtiseData(Context context) {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.setIncludeTxPowerLevel(false);
        // let device name enable
        builder.setIncludeDeviceName(true);
        return builder.build();
    }

    private AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        public void onStartFailure(int errorCode) {
            Log.d(TAG, "AdvertiseCallback onStartFailure errorCode=" + errorCode);
            // switch (errorCode) {
            // case ADVERTISE_FAILED_INTERNAL_ERROR:
            // break;
            // default:
            // break;
            // }
        };

        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(TAG, "AdvertiseCallback onStartSuccess");
        };

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
