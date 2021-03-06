package com.example.wiiphone;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

@SuppressWarnings("deprecation")
public class SpaceShipActivity extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private PowerManager mPowerManager = null;
    private WakeLock mWakeLock = null;
    private TCPClient mTcpClient = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	System.out.println("SpaceShip Activity: onCreate START");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.spaceship_main);
        
        // Get an instance of the SensorManager.
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        // Get the sensor (Accelerometer).
        if(mSensorManager != null)
        {
        	mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        	// Register Delays and Listeners.
        	if(mAccelerometer != null)
        	{
        		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        	}
        }
        // Get an instance of the PowerManager.
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        
        if(mPowerManager != null)
        {
        	mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        }
        
        SpaceShipView SSView = (SpaceShipView) findViewById(R.id.spaceshipview);
        mTcpClient = TCPClient.getTCP();
        SSView.setTcpClient(mTcpClient);
        
        
        System.out.println("SpaceShip Activity: onCreate END");
    }

    // Overriding all onX events for debugging cause I'm a noob at Android programming.
    @Override
    public void onResume()
    {
    	System.out.println("SpaceShip Activity: onResume START");
        super.onResume(); 
        if(mWakeLock != null)
        {
        	mWakeLock.acquire();
        }
        System.out.println("SpaceShip Activity: onResume END");
    }
    
    @Override
    public void onPause()
    {
    	System.out.println("SpaceShip Activity: onPause START");
        super.onPause();
       
    	SpaceShipView SSView = (SpaceShipView)findViewById(R.id.spaceshipview);
    	SSView.setTcpClient(null);
        
        if(mWakeLock != null)
        {
        	mWakeLock.release();
        }
        System.out.println("SpaceShip Activity: onPause END");
    }
    
    @Override
    public void onStart()
    {
    	System.out.println("SpaceShip Activity: onStart START");
        super.onStart();
        System.out.println("SpaceShip Activity: onStart END");
    }
    
    @Override
    public void onStop()
    {
    	System.out.println("SpaceShip Activity: onStop START");
        super.onStop();
        System.out.println("SpaceShip Activity: onStop END");
    }
    
    @Override
    public void onDestroy()
    {
    	System.out.println("SpaceShip Activity: onDestroy START");
        super.onDestroy();
        if(mTcpClient != null)
		{
			String message = "QUIT";
			mTcpClient.sendMessage(message);
			mTcpClient = null;
		}
        System.out.println("SpaceShip Activity: onDestroy END");
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) 
    {
    	if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
    	
    	if (mTcpClient != null) 
    	{
    		String message = "ACC " + Float.toString(event.values[0]) + " "
    	   			 + Float.toString(event.values[1]) + " " + Float.toString(event.values[2]);
    		mTcpClient.sendMessage(message);
    	}
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) 
    {
    	// Have to override this
    }
}