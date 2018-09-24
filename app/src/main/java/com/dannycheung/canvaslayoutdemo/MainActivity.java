package com.dannycheung.canvaslayoutdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dannycheung.canvaslayoutdemo.example.ImageNodeActivity;
import com.dannycheung.canvaslayoutdemo.example.NetImageNodeActivity;
import com.dannycheung.canvaslayoutdemo.example.TextNodeActivity;
import com.dannycheung.canvaslayoutdemo.example.ViewNodeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.viewnode).setOnClickListener(nodeClickListener);
        findViewById(R.id.textnode).setOnClickListener(nodeClickListener);
        findViewById(R.id.imagenode).setOnClickListener(nodeClickListener);
        findViewById(R.id.netimagenode).setOnClickListener(nodeClickListener);

    }

    private View.OnClickListener nodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.viewnode:
                    startActivity(new Intent(MainActivity.this, ViewNodeActivity.class));
                    break;
                case R.id.textnode:
                    startActivity(new Intent(MainActivity.this, TextNodeActivity.class));
                    break;
                case R.id.imagenode:
                    startActivity(new Intent(MainActivity.this, ImageNodeActivity.class));
                    break;
                case R.id.netimagenode:
                    startActivity(new Intent(MainActivity.this, NetImageNodeActivity.class));
                    break;
            }
        }
    };


}
