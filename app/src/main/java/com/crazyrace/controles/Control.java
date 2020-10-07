package com.crazyrace.controles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public abstract class Control {

	protected Bitmap bitmap;
	protected Rect bounds; //limites del boton
	protected boolean pressed = false;

	protected Control(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public void draw(Canvas canvas){
		if(bounds!=null){
			if(bitmap != null)
				drawBitmap(canvas);
			else
				drawDefaultButton(canvas);
		}
	}
	
	private void drawBitmap(Canvas canvas){
		if(pressed){
			drawPressBitmap(canvas);
		}else{
			drawNotPressBitmap(canvas);
		}
	}

	private void drawDefaultButton(Canvas canvas){
		if(pressed){
			drawPressDefaultButton(canvas);
		}else{
			drawNotPressDefaultButton(canvas);
		}
	}

	private boolean isPressed(float x, float y){
		if(bounds!=null){
			return bounds.contains((int)x, (int)y);
		}
		return false;
	}
	
	public boolean actuate(int event, float x, float y){
		if(this.isPressed(x, y)){
			if(event == MotionEvent.ACTION_DOWN || event == MotionEvent.ACTION_POINTER_DOWN){
				pressed = true;
				this.actionDown();
				return true;
			}else if(event == MotionEvent.ACTION_UP || event == MotionEvent.ACTION_POINTER_UP){
				pressed = false;
				this.actionUp();
				return true;
			}
		}
		return false;
	}

	public void setBounds(Rect bounds){
		this.bounds = bounds;
	}
	
	public Rect getBounds(){
		return bounds;
	}

	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public abstract void initButton(int screenWidth, int screenHeigth);
	protected abstract void actionDown();
	protected abstract void actionUp();
	protected abstract void drawPressBitmap(Canvas canvas);
	protected abstract void drawNotPressBitmap(Canvas canvas);
	protected abstract void drawPressDefaultButton(Canvas canvas);
	protected abstract void drawNotPressDefaultButton(Canvas canvas);
}
