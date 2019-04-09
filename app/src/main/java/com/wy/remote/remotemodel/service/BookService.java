package com.wy.remote.remotemodel.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.wy.remote.remotemodel.book.Book;
import com.wy.remote.remotemodel.book.IBookManager;
import com.wy.remote.remotemodel.book.IONNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 版本：V1.2.5
 * 时间： 2018/5/23 15:51
 * 创建人：laoqb
 * 作用：
 */
public class BookService extends Service {
    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private RemoteCallbackList<IONNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (PackageManager.PERMISSION_DENIED == checkCallingOrSelfPermission("com.ryg.self.SERVICE")) {
            return null;
        }
        return mBinder;
    }

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return books;
        }

        @Override
        public void add(Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public void registerListener(IONNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unregisterListener(IONNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        atomicBoolean.set(true);
    }

    private class ServiceWorker implements Runnable {
        int price = 50;

        @Override
        public void run() {
            while (!atomicBoolean.get()) {
                try {
                    Thread.sleep(3000);
                    int begin = listeners.beginBroadcast();
                    for (int i = 0; i < begin; i++) {
                        IONNewBookArrivedListener broadcastItem = listeners.getBroadcastItem(i);
                        if (null != broadcastItem) {
                            broadcastItem.onNewBookArrived(new Book("c", price++));
                        }
                    }
                    listeners.finishBroadcast();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
