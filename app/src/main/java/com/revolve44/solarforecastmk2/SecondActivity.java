package com.revolve44.solarforecastmk2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class SecondActivity extends AppCompatActivity {
    float finally_res = 0;
    private TextView f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finally_forecast);
        f = findViewById(R.id.finally_result);
        Calculating();
    }

    public void Calculating (){
//        finally_res = MainActivity.a-MainActivity.b;
//        f.setText(""+finally_res);
    }




}