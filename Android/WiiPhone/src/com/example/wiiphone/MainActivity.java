package com.example.wiiphone;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity
{
    private WakeLock mWakeLock = null;
    private TCPClient mTcpClient = null;
    private ConnectTask mConnectTask = null;
    
    public TCPClient GetTCP()
    {
    	return mTcpClient;
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	System.out.println("onCreate START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button tButton = (Button) findViewById(R.id.game_1);
        
        tButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(MainActivity.this, SpaceShipActivity.class));
			}
		});

        // connect to the server.
        mConnectTask = (ConnectTask) new ConnectTask().execute("");
        
        
        System.out.println("onCreate END");
    }
	private void startActivityByNr(int nr)
	{
		if(nr == 1)
			startActivity(new Intent(MainActivity.this, SpaceShipActivity.class));
	}
    // Overriding all onX events for debugging cause I'm a noob at Android programming.
    @Override
    public void onResume()
    {
    	System.out.println("onResume START");
        super.onResume(); 
        if(mWakeLock != null)
        {
        	mWakeLock.acquire();
        }
        System.out.println("onResume END");
    }
    
    @Override
    public void onPause()
    {
    	System.out.println("onPause START");
        super.onPause();
        if(mWakeLock != null)
        {
        	mWakeLock.release();
        }
        System.out.println("onPause END");
    }
    
    @Override
    public void onStart()
    {
    	System.out.println("onStart START");
        super.onStart();
        System.out.println("onStart END");
    }
    
    @Override
    public void onStop()
    {
    	System.out.println("onStop START");
        super.onStop();
        System.out.println("onStop END");
    }
    
    @Override
    public void onDestroy()
    {
    	System.out.println("onDestroy START");
        super.onDestroy();
        
        if(mTcpClient != null)
        {
	        mTcpClient.stopClient(); // Stop update if we close app
	    	mConnectTask.cancel(true);
	    	mConnectTask = null;
        	mTcpClient = null;
        }
        
        System.out.println("onDestroy END");
    }
    
    public class ConnectTask extends AsyncTask<String,String,TCPClient> 
    {
    	
        @Override
        protected TCPClient doInBackground(String... message) 
        {
            //we create a TCPClient object and
            System.out.println("Connection to Server");
    		mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() 
    		{
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
    		if(mTcpClient != null)
    		{
    			mTcpClient.run();
    		}
 
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) 
        {
            super.onProgressUpdate(values);
             // Somehow this needs to be here.
        }
    }
}