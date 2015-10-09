package com.example.hsae;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.Bluetooth.Constants;
import com.animations.RandomTextView;
import com.zhy.common.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Administrator on 2015/10/5.
 */
public class RadarActivity extends Activity {

    public static int  RESULT_CODE;
    private RandomTextView randomTextView;


    /**
     * Tag for Log
     */
    private static final String TAG = "RadarActivity";
    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;


    final  Map map = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_radar);

        setResult(Activity.RESULT_CANCELED);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//
       randomTextView = (RandomTextView)findViewById(R.id.RandomTextView);
//
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();



        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                randomTextView.addKeyWord(device.getName());
                randomTextView.show();
                map.put(device.getName() , device.getAddress());
            }
        }
        doDiscovery();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                randomTextView.addKeyWord("zizi");
//                randomTextView.show();
//                randomTextView.addKeyWord("sim800");
//                randomTextView.show();
//                randomTextView.addKeyWord("wang");
//          //      randomTextView.show();
//                randomTextView.show();

                randomTextView.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
                    @Override

                    public void onRippleViewClicked(View view) {

                        String name = (String)view.getTag(R.id.tag_second);

                        String address = map.get(name).toString();
                        if(address!=null) {
                              mBtAdapter.cancelDiscovery();
                              System.out.println("RadarActivy" + "点击了" + address);

                            // Create the result Intent and include the MAC address

                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
//                            intent.putExtra("name",name);
                            // Set result and finish this Activity
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                    }
                });

            }
        }, 2 * 1000);



        randomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
//        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    map.put(device.getName(), device.getAddress());
                    randomTextView.addKeyWord(device.getName());
                    randomTextView.show();
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);

                if (map.size() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    Toast.makeText(RadarActivity.this,noDevices,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RadarActivity.this,"发现"+map.size()+"个设备",Toast.LENGTH_SHORT).show();
                    for (Object obj:map.keySet()
                         ) {
                        System.out.println(obj);
                        
                    }
                    Vector<String>  vector=  randomTextView.getKeyWords();
                    for (String item:vector
                            ) {
                        System.out.println("雷达名称:"+item);
                    }
                }
            }
        }
    };


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent();
                intent.putExtra("back", "Back Data");//点击按钮后的返回参数，提示显示

                setResult(RESULT_CODE, intent);//RESULT_CODE是一个整型变量
                finish();//结束第二个activity，返回其调用它的activity
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
