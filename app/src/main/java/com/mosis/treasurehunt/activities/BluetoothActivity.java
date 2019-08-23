package com.mosis.treasurehunt.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.services.BluetoothService;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ListView mDevicesListView;
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    private BluetoothService mBluetoothService = null;
    private Set<BluetoothDevice> mPairedDevices;
    private SharedPreferencesWrapper mSharedPrefWrapper;

    ArrayList devicesList;
    ArrayAdapter adapter;
    ProgressBar discoveringProgressBar;

    private static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        mDevicesListView = findViewById(R.id.lv_paired_devices);
        devicesList = new ArrayList();
        mBluetoothService = new BluetoothService(BluetoothActivity.this, mHandler);
        mOutStringBuffer = new StringBuffer(" ");
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "This device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        this.doDiscovery();

        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            discoveringProgressBar = findViewById(R.id.pg_bluetooth);
            discoveringProgressBar.setVisibility(View.GONE);
            for (BluetoothDevice device : mPairedDevices) {
                String deviceName = device.getName();
                devicesList.add(deviceName);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesList);
        mDevicesListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothService.Constants.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                discoveringProgressBar = findViewById(R.id.pg_bluetooth);
                discoveringProgressBar.setVisibility(View.GONE);

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    devicesList.add(deviceName);
                }
                adapter = new ArrayAdapter(BluetoothActivity.this, android.R.layout.simple_list_item_1, devicesList);
                mDevicesListView.setAdapter(adapter);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                discoveringProgressBar = findViewById(R.id.pg_bluetooth);
                discoveringProgressBar.setVisibility(View.GONE);

                if (adapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    adapter.add(noDevices);
                }
            }
        }
    };

    private void doDiscovery() {
        discoveringProgressBar = findViewById(R.id.pg_bluetooth);
        discoveringProgressBar.setVisibility(View.VISIBLE);

        if (mBluetoothAdapter.isDiscovering())  {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) mBluetoothAdapter.cancelDiscovery();

        unregisterReceiver(mReceiver);

        if (mBluetoothService != null) mBluetoothService.stop();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mBluetoothAdapter.cancelDiscovery();
            sendFriendRequest();
            // Get the MAC address which is last 17 chars in the View
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    mBluetoothService = new BluetoothService(BluetoothActivity.this, mHandler);
                    mOutStringBuffer = new StringBuffer(" ");
                } else {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                    BluetoothActivity.this.finish();
                }
        }
    }

    /**
     * Handler that gets information back from Bluetooth Service
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(BluetoothActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
        }
    };


    private void connectDevice(Intent data) {
        String address = data.getExtras().getString(this.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBluetoothService.connect(device);
    }

    private void sendFriendRequest() {
        if (mBluetoothService.getState() != BluetoothService.Constants.STATE_CONNECTED) {
            Toast.makeText(BluetoothActivity.this, "Devices not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = mSharedPrefWrapper.getUsername();
        String message = "User " + username + " wants to be your friend in Treasure Hunt.";
        byte[] toSend = message.getBytes();
        mBluetoothService.write(toSend);

        mOutStringBuffer.setLength(0);
    }
}
