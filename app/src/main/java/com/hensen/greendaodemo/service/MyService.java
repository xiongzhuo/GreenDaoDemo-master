package com.hensen.greendaodemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyService extends Service {
    private String data = "默认消息";
    private boolean serviceRunning = false;

    // 必须实现的方法，用于返回Binder对象
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("--onBind()--");
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

        public void setData(String data) {
            MyService.this.data = data;
        }
    }

    // 创建Service时调用该方法，只调用一次
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("--onCreate()--");
        serviceRunning = true;
        new Thread() {
            @Override
            public void run() {
                int n = 0;
                while (serviceRunning) {
                    n++;
                    String str = n + data;
                    System.out.println("onCrate" + str);
                    if (dataCallback != null) {
                        dataCallback.dataChanged(str);
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    // 每次启动Servcie时都会调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("--onStartCommand()--");
        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);
    }

    // 解绑Servcie调用该方法
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("--onUnbind()--");
        return super.onUnbind(intent);
    }

    // 退出或者销毁时调用该方法
    @Override
    public void onDestroy() {
        serviceRunning = false;
        System.out.println("--onDestroy()--");
        super.onDestroy();
    }

    DataCallback dataCallback = null;

    public DataCallback getDataCallback() {
        return dataCallback;

    }

    public void setDataCallback(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }


    // 通过回调机制，将Service内部的变化传递到外部

    public interface DataCallback {

        void dataChanged(String str);

    }

}
