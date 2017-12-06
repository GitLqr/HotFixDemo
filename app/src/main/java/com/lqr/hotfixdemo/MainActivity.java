package com.lqr.hotfixdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lqr.hotfixdemo.simplehotfix.SimpleHotFixActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void simpleHostFix(View view) {
        startActivity(new Intent(this, SimpleHotFixActivity.class));
    }

}
