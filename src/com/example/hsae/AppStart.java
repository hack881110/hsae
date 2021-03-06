package com.example.hsae;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AppStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = View.inflate(this, R.layout.activity_app_start, null);
        setContentView(view);

//渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(2500);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                 redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
    }
        private void redirectTo(){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

}
