package com.example.movendemo;

import com.moven.service.PicDownloadService;
import com.moven.service.PicDownloadService.PicDownloadBinder;
import com.moven.service.RemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    private static final String TAG = "MainActivity"; 
    
    private Button startServiceBtn;
    
    private Button stopServiceBtn;
    
    private Button bindServiceBtn;
    
    private Button bindRemoteServiceBtn;
    
    private Button unbindServiceBtn;
    
    private Button getPicNumBtn;
    
    private Button getRemoteResultBtn;
    
    private ServiceConnection mServiceConnection;
    
    private ServiceConnection mRemoteServiceConnection;
    
    private PicDownloadService mService;
    
    private Messenger mRemoteService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initServiceConnection();
    }
    
    private void initView()
    {
        startServiceBtn = (Button)findViewById(R.id.start_service);
        stopServiceBtn = (Button)findViewById(R.id.stop_service); 
        bindServiceBtn = (Button)findViewById(R.id.bind_service);
        getPicNumBtn = (Button)findViewById(R.id.get_pic_num);
        unbindServiceBtn = (Button)findViewById(R.id.unbind_service);
        getRemoteResultBtn = (Button)findViewById(R.id.get_remote_result);
        bindRemoteServiceBtn = (Button)findViewById(R.id.bind_remote_service);
    }
    
    private void setListener()
    {
        startServiceBtn.setOnClickListener(this);
        stopServiceBtn.setOnClickListener(this);
        bindServiceBtn.setOnClickListener(this);
        getPicNumBtn.setOnClickListener(this);
        unbindServiceBtn.setOnClickListener(this);
        getRemoteResultBtn.setOnClickListener(this);
        bindRemoteServiceBtn.setOnClickListener(this);
    }
    
    private void initServiceConnection()
    {
        mServiceConnection = new ServiceConnection()
        {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Log.d(TAG, "onServiceConnected");
                PicDownloadBinder picBinder = ((PicDownloadBinder)service);
                mService = picBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                Log.d(TAG, "onServiceDisconnected");
            }
        };
        
        mRemoteServiceConnection = new ServiceConnection()
        {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                mRemoteService = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                Log.d(TAG, "onServiceDisconnected");
            }
            
        };
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.start_service)
        {
            Intent intent = new Intent(MainActivity.this, PicDownloadService.class);
            startService(intent);
        }
        else if (v.getId() == R.id.stop_service)
        {
            Intent intent = new Intent(MainActivity.this, PicDownloadService.class);
            stopService(intent);
        }
        else if (v.getId() == R.id.bind_service)
        {
            Log.d(TAG, "bind_service process="+android.os.Process.myPid());
            Intent intent = new Intent(MainActivity.this, PicDownloadService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        }
        else if (v.getId() == R.id.get_pic_num)
        {
            int picNum = mService.getDownloadedPicNum();
            Toast.makeText(MainActivity.this, "get "+picNum+" pic", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.unbind_service)
        {
            unbindService(mServiceConnection);
        }
        else if (v.getId() == R.id.get_remote_result)
        {
            sendRequestToRemote();
        }
        else if (v.getId() == R.id.bind_remote_service)
        {
            Log.d(TAG, "jz--- bind_service process="+android.os.Process.myPid());
            Intent intent = new Intent(MainActivity.this, RemoteService.class);
            bindService(intent, mRemoteServiceConnection, BIND_AUTO_CREATE);
        }
    }
    
    private void sendRequestToRemote()
    {
        Message msg = Message.obtain();
        msg.what = RemoteService.REQUEST_RESULT;
        try
        {
            mRemoteService.send(msg);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
    
    public void onDestroy()
    {
        unbindService(mServiceConnection);
        unbindService(mRemoteServiceConnection);
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
