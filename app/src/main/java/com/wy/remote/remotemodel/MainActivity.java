package com.wy.remote.remotemodel;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wy.remote.remotemodel.adapter.RecyclerViewAdapter;
import com.wy.remote.remotemodel.book.Book;
import com.wy.remote.remotemodel.book.IBookManager;
import com.wy.remote.remotemodel.book.IONNewBookArrivedListener;
import com.wy.remote.remotemodel.service.BookService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText name_edt, pass_edt, phone_edt;
    private Button add_btn, delete_btn, change_btn, search_btn,open_service_btn,close_service_btn;
    private RecyclerView rv;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<Map<String, Object>> mData = new ArrayList<>();
    private IBookManager mIBookManager;
    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_edt = findViewById(R.id.name_edt);
        pass_edt = findViewById(R.id.pass_edt);
        phone_edt = findViewById(R.id.phone_edt);
        add_btn = findViewById(R.id.add_btn);
        delete_btn = findViewById(R.id.delete_btn);
        change_btn = findViewById(R.id.change_btn);
        search_btn = findViewById(R.id.search_btn);
        open_service_btn = findViewById(R.id.open_service_btn);
        close_service_btn = findViewById(R.id.close_service_btn);
        rv = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(mData);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(mAdapter);
        add_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        change_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        open_service_btn.setOnClickListener(this);
        close_service_btn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIBookManager && mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.unregisterListener(ionNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(isBind){
            if(null != connection){
                unbindService(connection);
                isBind = false;
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            mIBookManager = iBookManager;
            try {
                mIBookManager.registerListener(ionNewBookArrivedListener);
                mIBookManager.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private IONNewBookArrivedListener ionNewBookArrivedListener = new IONNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(final Book newBook) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + newBook);
                }
            });
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(null == mIBookManager){
                return;
            }
            mIBookManager.asBinder().unlinkToDeath(deathRecipient,0);
            mIBookManager = null;
            Intent intent = new Intent("com.wy.remote.remotemodel.local.service");
            intent.setPackage("com.wy.remote.remotemodel");
            isBind = bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                if (TextUtils.isEmpty(name_edt.getText().toString()) || TextUtils.isEmpty(pass_edt.getText().toString())) {
                    Toast.makeText(this, "请输入完整的用户数据", Toast.LENGTH_LONG).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("NAME", name_edt.getText().toString());
                values.put("PASS", pass_edt.getText().toString());
                values.put("PHONE", phone_edt.getText().toString());
                getContentResolver().insert(Uri.parse("content://com.wy.remote.remotemodel/user"), values);
                name_edt.setText("");
                pass_edt.setText("");
                phone_edt.setText("");
                break;
            case R.id.delete_btn:
                if (TextUtils.isEmpty(name_edt.getText().toString())) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
                    return;
                }
                int delete = getContentResolver().delete(Uri.parse("content://com.wy.remote.remotemodel/user"), "NAME=?", new String[]{name_edt.getText().toString()});
                if (delete > 0) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                    name_edt.setText("");
                    pass_edt.setText("");
                    phone_edt.setText("");
                }
                break;
            case R.id.change_btn:
                if (TextUtils.isEmpty(name_edt.getText().toString()) && TextUtils.isEmpty(pass_edt.getText().toString())) {
                    Toast.makeText(this, "请输入完整的用户数据", Toast.LENGTH_LONG).show();
                    return;
                }
                values = new ContentValues();
                values.put("PASS", pass_edt.getText().toString());
                values.put("PHONE", phone_edt.getText().toString());
                int update = getContentResolver().update(Uri.parse("content://com.wy.remote.remotemodel/user"), values, "NAME=?", new String[]{name_edt.getText().toString()});
                if (update > 0) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                    name_edt.setText("");
                    pass_edt.setText("");
                    phone_edt.setText("");
                }
                break;
            case R.id.search_btn:
                mData.clear();
                Cursor userCursor = getContentResolver().query(Uri.parse("content://com.wy.remote.remotemodel/user"), new String[]{"NAME", "PASS", "PHONE"}, null, null, null);
                while (userCursor.moveToNext()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", userCursor.getString(userCursor.getColumnIndex("NAME")));
                    hashMap.put("pass", userCursor.getString(userCursor.getColumnIndex("PASS")));
                    hashMap.put("phone", userCursor.getLong(userCursor.getColumnIndex("PHONE")));
                    mData.add(hashMap);
                }
                mAdapter.updateData(mData);
                userCursor.close();
                if (mData.size() == 0) {
                    Toast.makeText(this, "暂无用户数据", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.open_service_btn:
                Intent intent = new Intent("com.wy.remote.remotemodel.local.service");
                intent.setPackage("com.wy.remote.remotemodel");
                isBind = bindService(intent, connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.close_service_btn:
                if(isBind){
                    if(null != connection){
                        unbindService(connection);
                        isBind = false;
                    }
                }else{
                    Toast.makeText(this, "请先启动监听服务", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
