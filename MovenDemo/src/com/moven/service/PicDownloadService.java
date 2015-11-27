package com.moven.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PicDownloadService extends Service
{
    private static final String TAG = "PicDownloadService";
    
    private Binder mBinder = new PicDownloadBinder();
    
    private int picNum = 10;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "onStartCommand startId="+startId);
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d(TAG, "onBind process="+android.os.Process.myPid());
        Log.d(TAG, "onBind");
        return mBinder;
    }
    
    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate");
    }
    
    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy");
    }
    
    public class PicDownloadBinder extends Binder
    {
        public PicDownloadService getService()
        {
            return PicDownloadService.this;
        }
    }
    
    public int getDownloadedPicNum()
    {
        return picNum++;
    }
    
    @Override
    protected void finalize()
    {
        Log.d(TAG, "finalize");
    }
    
}
