package com.mosis.treasurehunt.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.services.BluetoothService;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ListView mDevicesListView;
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    private BluetoothService mBluetoothService = null;
    private Set<BluetoothDevice> mPairedDevices;
    private SharedPreferencesWrapper mSharedPrefWrapper;
    private UserRepository mUserRepo;
    private Gson gson;

    ArrayList devicesList;
    ArrayAdapter adapter;
    ProgressBar discoveringProgressBar;

    private static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final String TAG = "Bluetooth_Activity_TAG";
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "This device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        mUserRepo = UserRepository.getInstance();
        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        mDevicesListView = findViewById(R.id.lv_paired_devices);
        devicesList = new ArrayList();
        mOutStringBuffer = new StringBuffer(" ");
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

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
                String deviceMacAddress = device.getAddress();
                if (!devicesList.contains(deviceName)) {
                    devicesList.add(deviceName + " - " + deviceMacAddress);
                }
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesList);
        mDevicesListView.setAdapter(adapter);

        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothService.Constants.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothService = new BluetoothService(BluetoothActivity.this, mHandler);

            if (mBluetoothService.getState() == BluetoothService.Constants.STATE_NONE) {
                mBluetoothService.start();
            }

        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null) mBluetoothService.stop();
        if (mBluetoothAdapter != null) mBluetoothAdapter.cancelDiscovery();

        unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                discoveringProgressBar = findViewById(R.id.pg_bluetooth);
                discoveringProgressBar.setVisibility(View.GONE);

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceMacAddress = device.getAddress();
                String deviceFullName = deviceName + " - " + deviceMacAddress;
                if (device.getBondState() != BluetoothDevice.BOND_BONDED && !devicesList.contains(deviceFullName)) {
                    devicesList.add(deviceFullName);
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

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mBluetoothAdapter.cancelDiscovery();

            // Get the MAC address
            String info = ((TextView) view).getText().toString();
            String macAddress = info.substring(info.length() - 17);

            Intent intent = new Intent(BluetoothActivity.this, BluetoothActivity.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, macAddress);
            connectDevice(intent);
        }
    };

    private void connectDevice(Intent data) {
        String address = data.getExtras().getString(this.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBluetoothService.connect(device);
    }

    /**
     * Handler that gets information back from Bluetooth Service
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = BluetoothActivity.this;
            switch (msg.what) {
                case BluetoothService.Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.Constants.STATE_CONNECTED:
                            Log.d(TAG, "DEVICES CONNECTED");
                            break;

                        case BluetoothService.Constants.STATE_CONNECTING:
                            Toast.makeText(BluetoothActivity.this,
                                    "Connecting devices...", Toast.LENGTH_SHORT).show();
                            break;

                        case BluetoothService.Constants.STATE_NONE:
                            Log.d(TAG, "DEVICES NOT CONNECTED");
                            break;
                    }
                    break;

                case BluetoothService.Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.Constants.NAME);
                    if (null != BluetoothActivity.this) {
                        String introMessage = "Nice to meet you! My username is @" + mSharedPrefWrapper.getUsername();
                        User myDetails = mUserRepo.getUserByUsername(mSharedPrefWrapper.getUsername());
                        JSONObject message = new JSONObject();
                        try {
                            message.put("intro", introMessage);
                            message.put("userDetails", myDetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sendBtMessage(message);
                    }
                    break;

                case BluetoothService.Constants.MESSAGE_RECEIVED:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    try {
                        JSONObject jsonMessage = new JSONObject(readMessage);
                        if (jsonMessage.getString("intro") != null) {
                            Toast.makeText(BluetoothActivity.this, jsonMessage.getString("intro"), Toast.LENGTH_LONG).show();
                        }
                        if (jsonMessage.getString("userDetails") != null) {
                            User user = gson.fromJson(jsonMessage.getJSONObject("userDetails").toString(), User.class);
                            mUserRepo.addFriend(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // TODO: handle if it isn't JSON
                    }
                    break;

                case BluetoothService.Constants.MESSAGE_SENT:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
//                    Toast.makeText(BluetoothActivity.this, "MESSAGEEE SENT", Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothService.Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(BluetoothService.Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void sendBtMessage(JSONObject message) {
        if (mBluetoothService.getState() != BluetoothService.Constants.STATE_CONNECTED) {
            Toast.makeText(BluetoothActivity.this, "Devices not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.toString().getBytes();
            mBluetoothService.write(send);
        }
    }

    private void sendBtMessage(String message) {
        if (mBluetoothService.getState() != BluetoothService.Constants.STATE_CONNECTED) {
            Toast.makeText(BluetoothActivity.this, "Devices not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mBluetoothService.write(send);
        }
    }
}
