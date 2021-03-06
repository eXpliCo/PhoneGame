package com.example.wiiphone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View
{
	private static final int AXIS_X = 0;
	private ShapeDrawable mDrawableInner = null;
	private ShapeDrawable mDrawableOuter = null;
	private ShapeDrawable mDrawCircle = null;
	private ShapeDrawable mDrawableCircleInner = null;
	private ShapeDrawable mDrawableCircleOuter = null;
	private int mSwingMultiplier = 10;
	private int mXPos = 50;
	private int mYPos = 250;
	private int mWidth = 250;
	private int mHeight = 50;
	private int mSteeringCircle = 175;
	private int mCircleWidth = 75;
	private TCPClient mTcpClient = null;
	
	public JoystickView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		mXPos = (int) 100;
		mYPos = (int) 310;
		mWidth = 500;
		mHeight = 125;
		mCircleWidth = 125;
		super.setRight(mXPos + mWidth);
		super.setLeft(mWidth);
		super.setTop(mYPos);
		super.setBottom(mYPos + mHeight);
		
		mSteeringCircle = mXPos + (mWidth / 2) - (mCircleWidth / 2);
		System.out.println("xPos: " + mXPos + "yPos" + mYPos );
		System.out.println("x: " + mWidth + "y" + mHeight );
		mDrawableInner = new ShapeDrawable(new RectShape());
		mDrawableInner.getPaint().setARGB(255,255,0,0);
		mDrawableInner.setBounds(mXPos, mYPos, mXPos + mWidth, mYPos + mHeight);
		
		mDrawableOuter = new ShapeDrawable(new RectShape());
		mDrawableOuter.setBounds(mXPos, mYPos, mXPos + mWidth, mYPos + mHeight);
        mDrawableOuter.getPaint().setARGB(255,255,255,255);
        mDrawableOuter.getPaint().setStyle(Paint.Style.STROKE);
		mDrawableOuter.getPaint().setStrokeWidth(10);
		
		mDrawCircle = new ShapeDrawable(new OvalShape());
		mDrawCircle.getPaint().setARGB(255,0,0,255);
		mDrawCircle.setBounds(mSteeringCircle, mYPos , mSteeringCircle + mCircleWidth, mYPos + mCircleWidth);
	}
	
	public void setTcpClient( TCPClient tcp )
    {
    	mTcpClient = tcp;
    }
	
	public void InvalidateView(float x, float y, float z)
    {
    	int mMiddleX = super.getWidth() / 2;
    	int mMiddleY = super.getHeight() / 4;
    	postInvalidate();
    	
    	int width = 100;
    	int height = 100;
    	
    	int lY = (int)x * mSwingMultiplier + mMiddleY - (height / 2);
    	int lX = (int)y * mSwingMultiplier + mMiddleX - (width / 2);
    	
    	mDrawableCircleInner = new ShapeDrawable(new OvalShape());
    	mDrawableCircleInner.getPaint().setARGB(255,255,0,0);
    	mDrawableCircleInner.setBounds(lX, lY, lX + width, lY + height);
    	
    	mDrawableCircleOuter = new ShapeDrawable(new OvalShape());
    	mDrawableCircleOuter.getPaint().setARGB(255,0,255,0);
    	mDrawableCircleOuter.getPaint().setStyle(Paint.Style.STROKE);
    	mDrawableCircleOuter.getPaint().setStrokeWidth(10);
    	mDrawableCircleOuter.setBounds(mMiddleX - (width / 2)-5, mMiddleY - (height / 2)-5,
    			mMiddleX - (width / 2) + width+5, mMiddleY - (height / 2) + height+5);
    	
        postInvalidate();
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN)  
		{
			
			System.out.println("LOLHEHE");
			mSteeringCircle = (int) event.getAxisValue(AXIS_X) - (mCircleWidth / 2);
			if(mSteeringCircle > (mXPos + mWidth) - (mCircleWidth / 2))
			{
				mSteeringCircle = mXPos + mWidth - (mCircleWidth / 2);
			}
			
			if(mSteeringCircle < (mXPos) - (mCircleWidth / 2))
			{
				mSteeringCircle = mXPos - (mCircleWidth / 2);
			}			
		}
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			mSteeringCircle = mXPos + (mWidth / 2) - (mCircleWidth / 2);
		}
		if(mTcpClient != null)
		{
			float middle = mXPos + (mWidth / 2) - (mCircleWidth / 2);
			float turn = mSteeringCircle - middle;
			float percent = turn / (500/2);
			String message = "TRN " + percent * 100;
			mTcpClient.sendMessage(message);
			Log.e("TEST", "MSG: " + message);
		}
		mDrawCircle.setBounds(mSteeringCircle, mYPos , mSteeringCircle + mCircleWidth, mYPos + mCircleWidth);
		postInvalidate();
		return true;
	} 
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		if(mDrawableInner != null)
		{
			mDrawableInner.draw(canvas);
		}
		if(mDrawableOuter != null)
		{
			mDrawableOuter.draw(canvas);
		}
		if(mDrawCircle != null)
		{
			mDrawCircle.draw(canvas);
		}
		if(mDrawableCircleInner != null)
		{
			mDrawableCircleInner.draw(canvas);
		}
		if(mDrawableCircleOuter != null)
		{
			mDrawableCircleOuter.draw(canvas);
		}
	}
}
