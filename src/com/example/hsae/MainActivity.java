package com.example.hsae;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.Bluetooth.BluetoothChatService;
import com.Bluetooth.Constants;
import com.Bluetooth.DeviceListActivity;
import com.DataHandle.deviceInfo;
import com.animations.ProgressWheel;
import com.zhy.common.Log;
import com.Bluetooth.SampleActivityBase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends SampleActivityBase implements View.OnClickListener
{
	private static final String TAG = "MianActivityBluetooth";
	//布局相关
	private SlidingMenu mMenu;

	//动画
	protected static final int SCAN_LODING = 1;
	protected static final int FINSH_SCAN = 2;
	private ImageView im_scan;
	private ImageView im_dian;
	private TextView tv_lodingApk;
	private TextView tv_count;
	private LinearLayout ll_scanText;
	private ProgressBar pb_loding;

	RotateAnimation animation;
	AlphaAnimation animation2;
	private int count;

	private ProgressWheel pwTwo;
	boolean wheelRunning ;
	int wheelProgress=0;

	//蓝牙相关
	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	/**
	 * Name of the connected device
	 */
	private String mConnectedDeviceName = null;

	/**
	 * String buffer for outgoing messages
	 */
	private StringBuffer mOutStringBuffer;

	/**
	 * Local Bluetooth adapter
	 */
	private BluetoothAdapter mBluetoothAdapter = null;

	/**
	 * Member object for the chat services
	 */
	public static BluetoothChatService mChatService = null;


	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SCAN_LODING:
					tv_lodingApk.setText("正在扫描:第"+count+"个BaseAnimation动画");
//					TextView tv = new TextView(MainActivity.this);
//					tv.setTextSize(14);
//					tv_count.setText("已扫描:"+count+"个BaseAnimation动画");
//					ll_scanText.addView(tv,0);
					break;
				case FINSH_SCAN:
					tv_lodingApk.setText("扫描完毕");
					im_scan.setVisibility(View.INVISIBLE);
					im_scan.clearAnimation();//清除此ImageView身上的动画
					im_dian.clearAnimation();//清除此ImageView身上的动画
					break;
			}
		};
	};

	void anim()
	{

		im_scan = (ImageView) findViewById(R.id.im_scan);
		im_dian = (ImageView) findViewById(R.id.im_dian);
		tv_lodingApk = (TextView) findViewById(R.id.tv_lodingApk);
//		ll_scanText = (LinearLayout) findViewById(R.id.ll_scanText);
		im_scan.setVisibility(View.INVISIBLE);
		pwTwo = (ProgressWheel) findViewById(R.id.progress_bar_two);
//		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
//		tv_count = (TextView) findViewById(R.id.tv_count);

		animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(3000);

		animation.setRepeatCount(Animation.INFINITE);


		animation2 = new AlphaAnimation(0.0f, 1.0f);
		animation2.setDuration(3000);
		animation2.setRepeatCount(Animation.INFINITE);
		count = 0;

	}
	private void fillData() {
		if (!wheelRunning) {
			wheelProgress = 0;
			pwTwo.resetCount();

			im_scan.setVisibility(View.VISIBLE);
			im_scan.startAnimation(animation);
			im_dian.startAnimation(animation2);
			tv_lodingApk.setText("开始准备释放空闲CPU线程");
			new Thread() {
				public void run() {
					wheelRunning = true;
					PackageManager pm = getPackageManager();
//				pb_loding.setMax(177);
					for (int i = 1; i <= 361; i++) {
						Message msg = Message.obtain();
						msg.what = SCAN_LODING;
						handler.sendMessage(msg);
						count = i;
						wheelProgress++;

						pwTwo.incrementProgress();
//					pb_loding.setProgress(count);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

//				wheelRunning = true;
//				while (wheelProgress < 361) {
//					pwTwo.incrementProgress();
//					wheelProgress++;
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
					wheelRunning = false;


					Message msg = Message.obtain();
					msg.what = FINSH_SCAN;
					handler.sendMessage(msg);
				}

				;
			}.start();
		}


	}

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("HSAE");
		getActionBar().setSubtitle("not connect");

//修改Actionbar 背景颜色
//		Drawable d = getResources().getDrawable(R.drawable. ic_launcher );
//		getActionBar() .setBackgroundDrawable(d);

//		getActionBar() .setBackgroundDrawable(new ColorDrawable(#12345690));

		mMenu = (SlidingMenu) findViewById(R.id.id_menu);

		findViewById(R.id.openOnLine).setOnClickListener(this);
		findViewById(R.id.openDiagnose).setOnClickListener(this);
		findViewById(R.id.theMain).setOnClickListener(this);
		findViewById(R.id.progress_bar_two).setOnClickListener(this);
		findViewById(R.id.imageButton1).setOnClickListener(this);
		findViewById(R.id.imageButton2).setOnClickListener(this);
		findViewById(R.id.imageButton3).setOnClickListener(this);
		findViewById(R.id.imageButton4).setOnClickListener(this);


		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(MainActivity.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			MainActivity.this.finish();
		}

		anim();
	}
	@Override
	public void onStart() {
		super.onStart();
		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else if (mChatService == null) {
//			Toast.makeText(MainActivity.this,"",
//					Toast.LENGTH_SHORT).show();
			//setupChat();
			// Initialize the BluetoothChatService to perform bluetooth connections
//			mChatService = new BluetoothChatService(MainActivity.this, mHandler);
			mChatService = BluetoothChatService.getInstance();
			mChatService.setmHandler(mHandler);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mChatService != null) {
			mChatService.stop();
		}
	}
	@Override
	public void onResume() {
		super.onResume();

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}
	/**
	 * Sends a message.
	 *
	 * @param message A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(MainActivity.this, R.string.not_connected, Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);

		}
	}
	/**
	 * Updates the status on the action bar.
	 *
	 * @param resId a string resource ID
	 */
	private void setStatus(int resId) {
		FragmentActivity activity = MainActivity.this;
		if (null == activity) {
			return;
		}
		final ActionBar actionBar = activity.getActionBar();
		if (null == actionBar) {
			return;
		}
		actionBar.setSubtitle(resId);
	}

	/**
	 * Updates the status on the action bar.
	 *
	 * @param subTitle status
	 */
	private void setStatus(CharSequence subTitle) {
		FragmentActivity activity = MainActivity.this;
		if (null == activity) {
			return;
		}
		final ActionBar actionBar = activity.getActionBar();
		if (null == actionBar) {
			return;
		}
		actionBar.setSubtitle(subTitle);
	}
	/**
	 * The Handler that gets information back from the BluetoothChatService
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case Constants.MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothChatService.STATE_CONNECTED:

							setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));

							String   writeMessage = "SIMCOMSPPFORAPP";
							byte b[] = writeMessage.getBytes();
							mChatService.write(b);
							break;
						case BluetoothChatService.STATE_CONNECTING:
							setStatus(R.string.title_connecting);
							break;
						case BluetoothChatService.STATE_LISTEN:
						case BluetoothChatService.STATE_NONE:
							setStatus(R.string.title_not_connected);
							break;
					}
					break;
				case Constants.MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					// construct a string from the buffer
					String writeMessage = new String(writeBuf);

					System.out.println(writeMessage);

					break;
				case Constants.MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);

					System.out.println(readMessage);

					break;
				case Constants.MESSAGE_DEVICE_NAME:
					// save the connected device's name
					mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
					if (null != MainActivity.this) {
						Toast.makeText(MainActivity.this, "Connected to "
								+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					}
					break;
				case Constants.MESSAGE_TOAST:
					if (null != MainActivity.this) {
						Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST),
								Toast.LENGTH_SHORT).show();
					}
					break;
			}
		}
	};

	/**
	 *  滑动状态改变回调
	 * @author zxy
	 *
	 */


	public void toggleMenu(View view)
	{
		mMenu.toggle();
		//btnTest(view);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras()
				.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);
	}

	/**
	 * Establish connection with other divice
	 *
	 * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
	 *	 */

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CONNECT_DEVICE_SECURE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {

					connectDevice(data, true);

				}
				break;
			case REQUEST_CONNECT_DEVICE_INSECURE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					connectDevice(data, false);
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					// Bluetooth is now enabled, so set up a chat session
					//setupChat();
				} else {
					// User did not enable Bluetooth or an error occurred
					Log.d(TAG, "BT not enabled");
					Toast.makeText(MainActivity.this, R.string.bt_not_enabled_leaving,
							Toast.LENGTH_SHORT).show();
					MainActivity.this.finish();
				}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				toggleMenu(mMenu);
				return true;
			case R.id.secure_connect_scan: {
				// Launch the DeviceListActivity to see devices and do scan
//				Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
//
//				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

				Intent serverIntent = new Intent(MainActivity.this, RadarActivity.class);

				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			//	startActivity(serverIntent);
				return true;
			}
			case R.id.discoverable: {
				// Ensure this device is discoverable by others
				ensureDiscoverable();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Makes this device discoverable.
	 */
	private void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() !=
				BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}
	/**
	 * 设置menu显示icon
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if (menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.openOnLine:
				Intent intent1 = new Intent(MainActivity.this, OnLineActivity.class);
				startActivity(intent1);
				break;

			case R.id.openDiagnose:
				Intent intent2 = new Intent(MainActivity.this, DiagnoseActivity.class);
				startActivity(intent2);
				break;
			case R.id.theMain:
				mMenu.toggle();
				break;
			case R.id.progress_bar_two:
				fillData();
				break;
			case R.id.imageButton4:
				Intent intent5 = new Intent(MainActivity.this, deviceInfo.class);
				startActivity(intent5);
		}
	}


}
