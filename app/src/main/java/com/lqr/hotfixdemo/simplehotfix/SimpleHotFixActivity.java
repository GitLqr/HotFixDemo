package com.lqr.hotfixdemo.simplehotfix;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lqr.hotfixdemo.R;


public class SimpleHotFixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_hot_fix);
    }

    public void fix(View view) {
        FixDexUtils.loadFixedDex(this, Environment.getExternalStorageDirectory());
    }

    public void clac(View view) {
        SimpleHotFixBugTest test = new SimpleHotFixBugTest();
        test.getBug(this);
    }
}
