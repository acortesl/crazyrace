package com.crazyrace.objetos;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class EndLine {
	
	private final static int PAINT_STROKE = 5;

	
	private BitmapDrawable bitmap;
	private double xPos;
	private int screenWidth;
	private int screenHeight;
	private int tileWidth;
	private Paint paint;
	private int firstLineX;
	private int secondLineX;
		
	public EndLine(double xPos, BitmapDrawable bitmapDrawable){
		this.xPos = xPos;
		this.bitmap = bitmapDrawable;
		this.bitmap.setTileModeX(Shader.TileMode.REPEAT);
		this.bitmap.setTileModeY(Shader.TileMode.REPEAT);
		this.tileWidth = bitmapDrawable.getBitmap().getWidth();
		this.paint = new Paint();	
		this.paint.setStyle(Style.STROKE);
		this.paint.setStrokeWidth(PAINT_STROKE);
		this.paint.setColor(Color.WHITE);
		this.firstLineX = (int) xPos -  PAINT_STROKE + 3;
		this.secondLineX = (int) xPos + tileWidth;
	}

	public Double getxPos() {
		return xPos;
	}
	
	public void draw(Canvas canvas, float offset){
		if(xPos-offset < screenWidth) {
			canvas.drawLine((int) (firstLineX- offset), 0, (int) (firstLineX - offset), screenHeight, paint);
			bitmap.setBounds((int) (xPos - offset), 0, (int) (secondLineX - offset), screenHeight);
			bitmap.draw(canvas);
			canvas.drawLine((int) (secondLineX - offset), 0, (int) (secondLineX - offset), screenHeight, paint);
		}

	}
	
	public void setScreenSize(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
	}

	public boolean isTheEnd(double currentX){
		if(currentX>=xPos){
			return true;
		}
		return false;
	}
}
