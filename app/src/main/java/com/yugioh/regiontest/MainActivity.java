package com.yugioh.regiontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private CanvasView mCanvasView;
    private CheckBox mOnDownCb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCanvasView = findViewById(R.id.view_canvas);
        mOnDownCb = findViewById(R.id.cb_down);

        mOnDownCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCanvasView.setOnDown(b);
            }
        });
    }

    public void onIsRegionClick(View view) {
        mCanvasView.setRegion();
    }
}