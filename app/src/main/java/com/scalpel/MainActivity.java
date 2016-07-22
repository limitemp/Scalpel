package com.scalpel;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private int contrbution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contrbution = 0;
        setContentView(R.layout.activity_main);
    }

}