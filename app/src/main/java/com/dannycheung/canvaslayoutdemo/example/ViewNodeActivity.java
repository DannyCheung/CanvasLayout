package com.dannycheung.canvaslayoutdemo.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.dannycheung.canvaslayout.layout.CanvasLayout;
import com.dannycheung.canvaslayout.node.ViewNode;

public class ViewNodeActivity extends AppCompatActivity {

    void render() {
        CanvasLayout canvasLayout = new CanvasLayout(this);
        canvasLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(canvasLayout);

        ViewNode root = new ViewNode();
        root.setWidth(50);
        root.setFlexGrow(0);
        root.setHeight(50);
        root.backgroundColor = 0xffffff00;
        canvasLayout.render(root);
    }

    /**
     * it doesn't matter
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("ViewNode");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //
        render();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
