package com.example.wiiphone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;



public class TCPClient {
	public static Context context = null;
	public static TCPClient mTCPClient = null;
    private String mServerMessage;
    public static final String SERVERIP = "192.168.1.122"; //your computer IP address
    public static final int SERVERPORT = 10000;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private Socket socket = null;
    private boolean mReadyToSend = false;
    PrintWriter mBufferOut = null;
    BufferedReader mBufferIn = null;
 
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener, Context c) {
        mMessageListener = listener;
        context = c;
        mTCPClient = this;
    }
    public boolean GetReadyToSend()
    {
    	return mReadyToSend;
    }
    public static TCPClient getTCP()
    {
    	if(mTCPClient == null)
    		Log.e("ERROR", "TCP == null");
    	return mTCPClient;
    }
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message)
    {
        if (mBufferOut != null && !mBufferOut.checkError()) 
        {
        	Log.e("SEND MSG: ", "MSG: " + message);
        	mBufferOut.println(message);
        	mBufferOut.flush();
        }
    }
 
    public void stopClient()
    {
    	 mRun = false;
    	 try{
    		 if (socket != null) 
        	 {
        		 socket.close();
        		 socket = null;
             }
    	 }
    	 catch (IOException e) 
    	 {
    	 }
    	 
         if (mBufferOut != null) {
             mBufferOut.flush();
             mBufferOut.close();
         }
  
         mMessageListener = null;
         mBufferIn = null;
         mBufferOut = null;
         mServerMessage = null;
    }
 
    public void run() 
    {
 
        mRun = true;
 
        try 
        {
			// Get your IP Adress in int
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();

			// Convert it to string
			String ipBinary = Integer.toBinaryString(ipAddress);
			//Leading zeroes are removed by toBinaryString, this will add them back.
			while(ipBinary.length() < 32) {
				ipBinary = "0" + ipBinary;
			}
			//get the four different parts
			String a=ipBinary.substring(0,8);
			String b=ipBinary.substring(8,16);
			String c=ipBinary.substring(16,24);
			String d=ipBinary.substring(24,32);
			//Convert to numbers
			String actualIpAddress = Integer.parseInt(d,2)+"."+Integer.parseInt(c,2)+"."+Integer.parseInt(b,2)+".";

			// Remove the last digits
/*
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("TCP Client", "C: Connecting... ");
 
            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVERPORT);
*/			
			int i = 0;
			int sleep = 5;
			do
			{
				try
				{
					Log.e("DEBUG", "Trying to reach: " + actualIpAddress+i);
					InetAddress serverAddr = InetAddress.getByName(actualIpAddress+i);
					if(serverAddr.isReachable(sleep))
					{
						Log.e("DEBUG", "Trying: " + actualIpAddress+i);
						socket = new Socket(serverAddr, SERVERPORT);
					}
				}
				catch(IOException e)
				{
					Log.e("DEBUG", "Couldn't connect to: " + actualIpAddress+i);
					socket = null;
				}
				finally
				{
					i++;
					
					if(i > 255)
					{
						Thread.sleep(sleep);
						if(sleep < 100)
						{
							sleep += 5;
						}
						i = 0;
					}
				}
			}
			while(socket == null);
			i--;
			Log.e("DEBUG", "Connected to: " + actualIpAddress+i);

            Log.e("TCP Client", "C: Connecting done");
            mReadyToSend = true;
            try {
 
                //send the message to the server
            	mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            	
            	String message1 = "GET MODE";
            	mBufferOut.println(message1);
            	
                Log.e("TCP Client", "C: Sent. ");
 
                
 
                //receive the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                	mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
                    mServerMessage = null;
                }
 
 
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
 
 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: Error", e);
 
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}