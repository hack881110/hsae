package com.example.hsae;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/9/28.
 */
public class AttrViewPager extends ViewPager  {


    boolean myOkLeft=false;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;


    public AttrViewPager(Context context) {

        super(context);
    }

    public AttrViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(myOkLeft==true) {

        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
        }
        else
        {
            return super.dispatchTouchEvent(ev);
        }

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        if(myOkLeft==true)
        {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onInterceptTouchEvent(arg0);

        } else
        {
            return super.onInterceptTouchEvent(arg0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        int action = event.getAction();
        switch (action){
        case MotionEvent.ACTION_DOWN:
            //当手指按下的时候
            x1 = event.getRawX();
            y1 = event.getRawY();
            break;
        case MotionEvent.ACTION_UP:
            x2 = event.getRawX();
            y2 = event.getRawY();
            break;

        case MotionEvent.ACTION_MOVE:


            break;
        }

        System.out.println("当前："+getCurrentItem());
        System.out.println("偏移："+"x1="+x1+" "+"x2="+x2+" " +((x1)-x2));
        if(getCurrentItem()==0&&x1-x2>20)
        {
            myOkLeft=true;
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(event);

        }
        if(getCurrentItem()!=0)
        {
            myOkLeft=true;
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(event);
        }
        if(getCurrentItem()==0&&x2-x1>20)
        {
            x2=0;
            myOkLeft=false;
           // setCurrentItem(0);
            return super.onTouchEvent(event);

        }
        return super.onTouchEvent(event);

    }


}
