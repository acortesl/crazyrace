package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.crazyrace.util.Posicion;
import com.crazyrace.util.Vector;

public abstract class Collectable {
	protected Bitmap bitmap;
	protected Rect bounds;
	protected Vector pos;
	protected boolean isCollected;
	
	
	protected Collectable(Bitmap bitmap, Vector pos){
		this.pos = pos;
		this.bitmap = bitmap;
		this.isCollected = false;
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		bounds = new Rect(x,y,x+bitmap.getWidth(),y+bitmap.getHeight());
	}
	
	public void draw(Canvas canvas, double offset){
		if(bounds!=null){
			canvas.drawBitmap(bitmap, bounds.left-(int)offset, bounds.top, null);
		}
	}
	
	public boolean collect(float x, float y){
		if(bounds!=null && !isCollected){
			if(bounds.contains((int)x, (int)y)){
				this.isCollected = true;
				return true;
			}		
		}
		return false;
	}
	
	public boolean collect(Rect player){
		if(bounds!=null && !isCollected){
			if(bounds.intersect(player)){
				this.isCollected = true;
				return true;
			}		
		}
		return false;
	}

	public Vector getPosicion(){
		return pos;
	}

	public int getRadio(){
		return bitmap.getWidth();
	}
}
