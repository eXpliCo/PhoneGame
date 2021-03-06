package com.example.wiiphone;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class LabyrinthActivity extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private PowerManager mPowerManager = null;
    private WakeLock mWakeLock = null;
    private TCPClient mTcpClient = null;
    private LabyrinthView LView = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	System.out.println("Labyrinth Activity: onCreate START");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.labyrinth_main);
        
        // Get an instance of the SensorManager.
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Get the sensor (Accelerometer).
        if(mSensorManager != null)
        {
        	mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        	// Register Delays and Listeners.
        	if(mAccelerometer != null)
        	{
        		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        	}
        }
        // Get an instance of the PowerManager.
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        
        if(mPowerManager != null)
        {
        	mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        }
        
        Button btnTemp = (Button) findViewById(R.id.buttonrestart);
        btnTemp.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				String message = "RESTART";
				if(mTcpClient != null)
				{
					mTcpClient.sendMessage(message);
				}
			}
		});
        
        LView = (LabyrinthView) findViewById(R.id.labyrinthview);
        mTcpClient = TCPClient.getTCP();
        LView.setTcpClient(mTcpClient);
        
        
        System.out.println("Labyrinth Activity: onCreate END");
    }
    // Overriding all onX events for debugging cause I'm a noob at Android programming.
    @Override
    public void onResume()
    {
    	System.out.println("Labyrinth Activity: onResume START");
        super.onResume(); 
        if(mWakeLock != null)
        {
        	mWakeLock.acquire();
        }
        System.out.println("Labyrinth Activity: onResume END");
    }
    
    @Override
    public void onPause()
    {
    	System.out.println("Labyrinth Activity: onPause START");
        super.onPause();
        
        LView.setTcpClient(null);
        
        if(mWakeLock != null)
        {
        	mWakeLock.release();
        }
        System.out.println("Labyrinth Activity: onPause END");
    }
    
    @Override
    public void onStart()
    {
    	System.out.println("Labyrinth Activity: onStart START");
        super.onStart();
        System.out.println("Labyrinth Activity: onStart END");
    }
    
    @Override
    public void onStop()
    {
    	System.out.println("Labyrinth Activity: onStop START");
        super.onStop();
        System.out.println("Labyrinth Activity: onStop END");
    }
    
    @Override
    public void onDestroy()
    {
    	System.out.println("Labyrinth Activity: onDestroy START");
        super.onDestroy();
        if(mTcpClient != null)
		{
			String message = "QUIT";
			mTcpClient.sendMessage(message);
			mTcpClient = null;
		}
        System.out.println("Labyrinth Activity: onDestroy END");
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) 
    {
    	if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
    	
    	if (mTcpClient != null) 
    	{
    		/*float tempFloats[] = {event.values[0], event.values[1], event.values[2]};
    		for(int i = 0; i < 3; i++)
    		{
    			int temp = (int) (tempFloats[i] * 100);
    			tempFloats[i] = (float) (temp * 0.01);
    			Log.d("TEST", "TEST: " + tempFloats[i]);
    			Log.d("TEST", "TEST1: " + event.values[i]);
    		}
    		
    		String message = "ACC " + Float.toString(tempFloats[0]) + " "
    	   			 + Float.toString(tempFloats[1]) + " " + Float.toString(tempFloats[2]);*/
    		String message = "ACC " + Float.toString(event.values[0]) + " "
   	   			 + Float.toString(event.values[1]) + " " + Float.toString(event.values[2]);
    		
    		mTcpClient.sendMessage(message);
    		
    		LView.InvalidateView(event.values[0], event.values[1], event.values[2]);
    	}
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) 
    {
    	// Have to override this
    }
}