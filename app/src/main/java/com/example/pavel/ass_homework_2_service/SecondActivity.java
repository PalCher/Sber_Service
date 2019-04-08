package com.example.pavel.ass_homework_2_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private TextView tvServiceResult;
    public static boolean myBound = false;
    final String LOG_TAG = "myLogs";
    private Messenger myService;
    final Messenger myMessenger = new Messenger(new IncomingHandler());
    public static final int MSG_VALUE = 3;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        tvServiceResult = findViewById(R.id.tv_service_result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
        Log.d(LOG_TAG,"Binded");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindservice();
    }

    public void bindService(){
        bindService(MyService.newIntent(SecondActivity.this),
                myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindservice(){
        Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = myMessenger;
        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_VALUE:
                    tvServiceResult.setText(msg.obj.toString());
            }
        }
    }


    public ServiceConnection getMyServiceConnection() {
        return myServiceConnection;
    }

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = myMessenger;
            try {
                myService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            myBound = true;
            Log.d(LOG_TAG,"SecondActivity connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
            myBound = false;
            Log.d(LOG_TAG,"SecondActivity disconnected");
        }
    };

    public static final Intent newIntent (Context context){
        Intent intent = new Intent(context, SecondActivity.class);
        return intent;
    }
}
