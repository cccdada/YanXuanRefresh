package com.sheng.yanxuanrefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.autoRefresh(400);
        refreshLayout.setReboundDuration(1000);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.finishRefresh(8000);
    }
}
