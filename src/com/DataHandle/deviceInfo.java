package com.DataHandle;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.hsae.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/7.
 */
public class deviceInfo extends Activity {


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deviceinfo);

        ListView list = (ListView) findViewById(R.id.list);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();


//        listItem.add(thedata.DeviceSet);



        for(int i=0;i<5;i++)
        {
            HashMap<String, Object>  map = new HashMap<String, Object>();
//            map.put("ItemEdit","1111");//图像资源的ID
//            map.put("ItemTitle", "Level "+i);
////            map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
//            listItem.add(map);

            map.put("ItemTitle", "车牌号码:");
            map.put("ItemEdit", ((CustomApplication) getApplicationContext()).getCar().toString());
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.list_info,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"ItemEdit","ItemTitle"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemEdit,R.id.ItemTitle}
        );

        //添加并且显示
        list.setAdapter(listItemAdapter);



        //添加点击
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                setTitle("点击第" + arg2 + "个项目");
            }
        });

        //添加长按点击
        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("长按菜单-ContextMenu");
                menu.add(0, 0, 0, "弹出长按菜单0");
                menu.add(0, 1, 0, "弹出长按菜单1");
            }
        });
    }
    //长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
        return super.onContextItemSelected(item);
    }
}
