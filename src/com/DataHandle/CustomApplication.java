package com.DataHandle;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/8.
 */
public class CustomApplication extends Application {

    public Map DeviceSet=null;

    private String  car ="沪BD1001";

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DeviceSet =new HashMap();
        DeviceSet.put("省域ID：",112);
        DeviceSet.put("市域ID：",14);
        DeviceSet.put("车牌号码：","沪BD0001");
        DeviceSet.put("车牌颜色：","黄色");

    }
}
