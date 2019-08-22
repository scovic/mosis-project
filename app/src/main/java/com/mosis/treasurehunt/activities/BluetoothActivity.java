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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ListView mDevicesListView;
    private Set<BluetoothDevice> mPairedDevices;
    ArrayList devicesList;
    private static String EXTRA_DEVICE_ADDRESS = "device_address";
    ArrayAdapter adapter;
    ProgressBar discoveringProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        mDevicesListView = findViewById(R.id.lv_paired_devices);
        devicesList = new ArrayList();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "This device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        this.doDiscovery();

        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            discoveringProgressBar = findViewById(R.id.pg_bluetooth);
            discoveringProgressBar.setVisibility(View.GONE);
            for (BluetoothDevice device : mPairedDevices) {
                String deviceName = device.getName();
                String deviceMacAddress = device.getAddress();
                devicesList.add(deviceName);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesList);
        mDevicesListView.setAdapter(adapter);
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
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mBluetoothAdapter.cancelDiscovery();

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
}
