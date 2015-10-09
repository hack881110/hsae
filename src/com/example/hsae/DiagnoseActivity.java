package com.example.hsae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Bluetooth.BluetoothChatService;
import com.animations.RippleView;

/**
 * Created by Administrator on 2015/10/1.
 */
public class DiagnoseActivity extends FragmentActivity {


    float x1,y1;

    RippleView RV;
    EditText ET;
    Button BT;
    public static int  RESULT_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //	getActionBar().setDisplayShowHomeEnabled(false);
        	getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setTitle("back");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_diagnose);
        RV = (RippleView)findViewById(R.id.testRip);
        RV.setText("测试雷达");


        ET =(EditText)findViewById(R.id.edit);
        BT=(Button)findViewById(R.id.send);
        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               BluetoothChatService mChatService = BluetoothChatService.getInstance();
                byte[]  b= ET.getText().toString().getBytes();
                mChatService.write(b);
            }
        });



        RV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        RV.setShadowLayer(1, 1, 1, 0xdd696969);
        RV.setGravity(Gravity.CENTER);
        RV.startRippleAnimation();


        RV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RV.startRippleAnimation();

            }
        });
        System.out.println("TOP"+":"+RV.getTop()+"\r\n"+
                            "Button"+":"+RV.getBottom()+"" + "\r\n"+
                            "Right"+":" +RV.getRight()+"\r\n"+
                            "Left"+":"+RV.getLeft()+"\r\n");


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //当手指按下的时候
                x1 = event.getRawX();
                y1 = event.getRawY();
                break;
        }
        return super.onTouchEvent(event);
    }

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
