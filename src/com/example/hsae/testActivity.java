package com.example.hsae;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2015/10/3.
 */
public class testActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the window
        // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.layout_diagnose);

        // Set result CANCELED in case the user backs out
    //    setResult(Activity.RESULT_CANCELED);
    }
}
