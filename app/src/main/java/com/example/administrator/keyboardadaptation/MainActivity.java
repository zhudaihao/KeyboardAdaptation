package com.example.administrator.keyboardadaptation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Button bt_yes;
    LinearLayout ll_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_yes = findViewById(R.id.bt_yes);
        ll_layout=findViewById(R.id.ll_layout);

        KeyBoardUtils.instance().setKeyboardLayout(ll_layout,bt_yes,this);


    }
}
