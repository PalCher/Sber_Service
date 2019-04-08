package com.example.pavel.ass_homework_2_service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;

    private Messenger mMessenger = new Messenger(new IncomingHandler());
    private List<Messenger> myClients = new ArrayList<>();
    boolean isInterupted;


    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (true) {
                    if (isInterupted){
                        return;
                    }
                    int n = random.nextInt(10);
                    Log.d("MyLog",Integer.toString(n) );
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        if(SecondActivity.myBound){
                            Message msg = Message.obtain(null,3,n);
                            for (Messenger messenger: myClients) {

                                messenger.send(msg);

                            }
                        }
                    } catch (InterruptedException | RemoteException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    myClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    myClients.remove(msg.replyTo);
                    isInterupted = true;
                    break;
            }
        }
    }


    public static final Intent newIntent (Context context){
        Intent intent = new Intent(context, MyService.class);
        return intent;
    }
}
