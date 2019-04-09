package com.wy.remote.remotemodel.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wy.remote.remotemodel.pool.BinderPoolImpl;

public class BinderPoolService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl();
    }

}
