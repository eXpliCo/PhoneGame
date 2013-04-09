package com.example.wiiphone;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
 
import java.util.ArrayList;
 
public class MainActivity extends Activity
{
    private ListView mList = null;
    private ArrayList<String> arrayList = null;
    private MyCustomAdapter mAdapter = null;
    private TCPClient mTcpClient = null;
    private connectTask mConnectTask = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        arrayList = new ArrayList<String>();
 
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);
 
        //relate the listView from java to the one created in xml
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);
 
        // connect to the server
        new connectTask().execute("");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
                String message = editText.getText().toString();
 
                //add the text in the arrayList
                arrayList.add("c: " + message);
 
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
 
                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
 
    }
    @Override
    public void onResume()
    {
    	System.out.println("onResume");
        super.onResume(); 
    }
    
    public void onPause()
    {
    	System.out.println("onPause");
        super.onPause();
        
    }
    public void onStart()
    {
    	System.out.println("onStart");
        super.onStart();
        
    }
    
    public void onStop()
    {
    	System.out.println("onStop");
        super.onStop();
    }
    
    public void onDestroy()
    {
    	System.out.println("onDestroy START");
        super.onDestroy();
        mTcpClient.stopClient();
        System.out.println("onDestroy DONE");
    }
 
    public class connectTask extends AsyncTask<String,String,TCPClient> 
    {
    	
        @Override
        protected TCPClient doInBackground(String... message) 
        {
            //we create a TCPClient object and
            System.out.println("Connection to Server");
    		mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
        	mTcpClient.run();
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
 
            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
}