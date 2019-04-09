package com.wy.remote.remotemodel.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.wy.remote.remotemodel.login.Login;
import com.wy.remote.remotemodel.login.LoginListener;

/**
 * 版本：V1.2.5
 * 时间： 2018/5/23 15:51
 * 创建人：laoqb
 * 作用：
 */
public class LoginService extends Service {
    private RemoteCallbackList<LoginListener> listeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private Binder binder = new Login.Stub() {

        @Override
        public void dologin(String name, String pass) throws RemoteException {
            Cursor userCursor = getContentResolver().query(Uri.parse("content://com.wy.remote.remotemodel/user"), new String[]{"NAME", "PASS", "PHONE"}, null, null, null);
            while (userCursor.moveToNext()) {
                if (name.equals(userCursor.getString(userCursor.getColumnIndex("NAME"))) && pass.equals(userCursor.getString(userCursor.getColumnIndex("PASS")))) {
                    int begin = listeners.beginBroadcast();
                    for (int i = 0; i < begin; i++) {
                        LoginListener broadcastItem = listeners.getBroadcastItem(i);
                        if (null != broadcastItem) {
                            broadcastItem.success();
                            listeners.finishBroadcast();
                            userCursor.close();
                            return;
                        }
                    }
                    listeners.finishBroadcast();
                }
            }
            userCursor.close();
            int begin = listeners.beginBroadcast();
            for (int i = 0; i < begin; i++) {
                LoginListener broadcastItem = listeners.getBroadcastItem(i);
                if (null != broadcastItem) {
                    broadcastItem.fail();
                }
            }
            listeners.finishBroadcast();
        }

        @Override
        public void registerListener(LoginListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unregisterListener(LoginListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

}
