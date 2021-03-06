package com.example.wiiphone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class LabyrinthView extends View
{
	private int mSwingMultiplier = 50;
	private TCPClient mTcpClient = null;
	private ShapeDrawable mDrawableOuter = null;
	private ShapeDrawable mDrawableInner = null;
	
	public static final int AXIS_Y = 1;
	
    public LabyrinthView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        
        mTcpClient = null;
    }
    public void InvalidateView(float x, float y, float z)
    {
    	int mMiddleX = super.getWidth() / 2;
    	int mMiddleY = super.getHeight() / 2;
    	postInvalidate();
    	
    	int width = 200;
    	int height = 200;
    	
    	int lY = (int)x * mSwingMultiplier + mMiddleY - (height / 2);
    	int lX = (int)y * mSwingMultiplier + mMiddleX - (width / 2);
    	
    	mDrawableInner = new ShapeDrawable(new OvalShape());
    	mDrawableInner.getPaint().setARGB(255,255,0,0);
    	mDrawableInner.setBounds(lX, lY, lX + width, lY + height);
    	
    	mDrawableOuter = new ShapeDrawable(new OvalShape());
    	mDrawableOuter.getPaint().setARGB(255,0,255,0);
    	mDrawableOuter.getPaint().setStyle(Paint.Style.STROKE);
    	mDrawableOuter.getPaint().setStrokeWidth(10);
    	mDrawableOuter.setBounds(mMiddleX - (width / 2)-5, mMiddleY - (height / 2)-5,
    			mMiddleX - (width / 2) + width+5, mMiddleY - (height / 2) + height+5);
    	
        postInvalidate();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) 
    {
        super.onSizeChanged(h, w, oldh, oldw);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)  
        {
			if (mTcpClient != null) 
			{
            }
			else
			{
				Log.e("ERROR SSView", "TCP === null");
			}
        }
        return true;
    }
    
    @Override
    protected void onDraw(Canvas canvas) 
    {
		if(mDrawableInner != null)
		{
			mDrawableInner.draw(canvas);
    	}
		if(mDrawableOuter != null)
    	{
    		mDrawableOuter.draw(canvas);
    	}
    }
    
    public void setTcpClient( TCPClient tcp )
    {
    	mTcpClient = tcp;
    }
}
