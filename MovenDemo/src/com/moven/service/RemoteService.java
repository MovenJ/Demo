package com.moven.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class RemoteService extends Service
{

    private static final String TAG = "RemoteService";
    
    final Messenger mMessenger = new Messenger(new MessageHandler());
    
    public static final int REQUEST_RESULT = 1;
    
    class MessageHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == REQUEST_RESULT)
            {
                sendReplyResult();
            }
        }
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d(TAG, "jz--- onBind="+android.os.Process.myPid());
        return mMessenger.getBinder();
    }
    
    private void sendReplyResult()
    {
        Log.d(TAG, "jz--- sendReplyResult");
    }
}
