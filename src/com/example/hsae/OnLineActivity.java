package com.example.hsae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/1.
 */
public class OnLineActivity extends FragmentActivity {

    private AttrViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTitles = new String[]
            { "First Fragment !", "Second Fragment !", "Third Fragment !",
                    "Fourth Fragment !" };
    private FragmentPagerAdapter mAdapter;
    public static int  RESULT_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getActionBar().setDisplayShowHomeEnabled(false);
        	//getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setTitle("Back");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_online);
        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
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
    private void initDatas()
    {
        for (String title : mTitles)
        {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);

            mTabs.add(tabFragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {

                return mTabs.size();

            }

            @Override
            public Fragment getItem(int position)
            {

                return mTabs.get(position);
            }


        };
    }

    private void initView()
    {
        mViewPager = (AttrViewPager) findViewById(R.id.id_viewpager);


    }



}
