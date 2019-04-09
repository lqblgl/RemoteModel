package com.wy.remote.remotemodel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/7 17:28
 * 创建人：laoqb
 * 作用：
 */
public class StartByLocalActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TextView(this));
    }
}
