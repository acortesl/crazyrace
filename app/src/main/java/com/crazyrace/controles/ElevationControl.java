package com.crazyrace.controles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.crazyrace.objetos.Player;
import com.crazyrace.objetos.Rebotable;

public class ElevationControl extends Control {
	private static final int xAccel = 0;
	private static final int yAccel = 12000;
	private static final int downXAccel = -xAccel;
	private static final int downYAccel = -yAccel;
	private static final int upXAccel = xAccel;
	private static final int upYAccel = yAccel;

	private Player player;
	private Bitmap pressedBitmap;

	//DIBUJAR BOTON
	private Path arrow;
	private Paint arrowPaint;

	protected final static int RADIO = 75;
	protected final static int RADIOINT = 65;
	protected float cx; //centro x del boton
	protected float cy; //centro y del boton

	protected Paint paintFirstCircle; //paint circunferencia exterior
	protected Paint paintSecondCircle; //paint circunferencia interior



	public ElevationControl(Player player, Bitmap bitmap, Bitmap pressedBitmap) {
		super(bitmap);
		this.player = player;
		this.pressedBitmap = pressedBitmap;
		if(bitmap == null){
			initPaint();
		}
	}

	private void initPaint(){
		//Paint circunferencia exterior
		paintFirstCircle = new Paint();
		paintFirstCircle.setStyle(Style.FILL);
		paintFirstCircle.setStrokeCap(Paint.Cap.ROUND);
		paintFirstCircle.setShader(null);
		paintFirstCircle.setStrokeWidth(RADIO);
		paintFirstCircle.setColor(Color.GRAY);
		paintFirstCircle.setMaskFilter(new EmbossMaskFilter(new float[] { 0, 1, 1 },0.6f, 10, 7.5f));

		//Paint circunferencia interior
		paintSecondCircle = new Paint();
		paintSecondCircle.setStyle(Style.FILL);
		paintSecondCircle.setStrokeCap(Paint.Cap.ROUND);
		paintSecondCircle.setStrokeWidth(RADIOINT);
		paintSecondCircle.setColor(Color.GRAY);
		paintSecondCircle.setMaskFilter(new EmbossMaskFilter(new float[] { 0, 1, 1 },0.6f, 10, 7.5f));
		int[] color = {Color.DKGRAY,Color.GREEN,Color.GREEN,Color.DKGRAY};
		float[] order = {0f,0.3f,0.7f,1f};
		paintSecondCircle.setShader(new LinearGradient(1000/2,0,1000/2,RADIO/2,color,order, Shader.TileMode.CLAMP));

		arrowPaint = new Paint();
		paintSecondCircle.setStyle(Style.FILL);
		arrowPaint.setStrokeWidth(5);
		arrowPaint.setColor(Color.WHITE);
		arrowPaint.setPathEffect(null);
	}



	@Override
	public void initButton(int screenWidth, int screenHeigth) {		
		int x,y,x2,y2;
		x = 10;
		if(bitmap != null){
			y = screenHeigth - this.bitmap.getHeight() - 10;
			x2 = x + this.bitmap.getWidth();
			y2 = y + this.bitmap.getHeight();		
		}else{
			y = screenHeigth - 2*RADIO - 10;
			x2 = x + 2*RADIO;
			y2 = y + 2*RADIO;
		}
		this.setBounds(new Rect(x,y,x2,y2));
		this.setCenter(bounds.left + RADIO, bounds.top + RADIO);
		createArrowPath(bounds);
	}

	public void setCenter(float cx, float cy){
		this.cx = cx;
		this.cy = cy;
	}

	private void createArrowPath(Rect bounds){
		int externalMargin = RADIO - RADIOINT;
		int internalMargin = 30;
		int rectangleWidth = 25;
		int leftRec, topRec, rightRec, bottomRec;
		int desplazRecY = 10;
		float radRound = 5;
	
		arrow = new Path();
		
		//rectangulo de la flecha
		leftRec = bounds.centerX() - (rectangleWidth /2);
		rightRec = bounds.centerX() + (rectangleWidth /2);
		topRec = bounds.top + RADIO ;//(bounds.bottom - bounds.top) /2;//bounds.top + externalMargin + internalMargin + desplazRecY;
		bottomRec = bounds.bottom - externalMargin - internalMargin + desplazRecY;
		RectF rect = new RectF(leftRec, topRec, rightRec, bottomRec);
		arrow.addRoundRect(rect, radRound, radRound, Direction.CCW);//(leftRec, topRec, rightRec, bottomRec, Direction.CW);
		arrow.addRoundRect(rect, new float[]{0,0,0,0,radRound,radRound,radRound,radRound}, Direction.CCW);
		//triangulo de la flecha
		int xLeftCorner, yLeftCorner, xRightCorner, yRightCorner, xTopCorner, yTopCorner;
		int desplazTriangY = 0;
		int topMargin = 15;
		int lrMargin = 25;
		xLeftCorner = 10 /*margin x*/ + externalMargin + lrMargin;
		yLeftCorner = bounds.centerY();
		xRightCorner = bounds.right - externalMargin - lrMargin;
		yRightCorner = bounds.centerY();
		xTopCorner = bounds.centerX();
		yTopCorner = bounds.top + externalMargin + topMargin;
	
		arrow.moveTo(xLeftCorner, yLeftCorner);
		arrow.lineTo(xTopCorner, yTopCorner);
		arrow.lineTo(xRightCorner, yRightCorner);
		arrow.close();
	}

	@Override
	protected void actionDown() {
		this.player.setAceleracion(downXAccel, downYAccel);
	}

	@Override
	protected void actionUp() {
		this.player.setAceleracion(upXAccel, upYAccel);
	}

	@Override
	protected void drawPressBitmap(Canvas canvas) {
		canvas.drawBitmap(pressedBitmap, bounds.left, bounds.top, null);
	}

	@Override
	protected void drawNotPressBitmap(Canvas canvas) {
		canvas.drawBitmap(bitmap, bounds.left, bounds.top, null);
	}

	@Override
	protected void drawPressDefaultButton(Canvas c) {
		c.drawCircle(cx, cy, RADIO, paintFirstCircle);
		c.drawCircle(cx, cy, RADIOINT, paintSecondCircle);
		if (arrow != null && arrowPaint != null){
			arrowPaint.setColor(Color.RED);
			c.drawPath(arrow, arrowPaint);
		}
	}

	@Override
	protected void drawNotPressDefaultButton(Canvas c) {
		c.drawCircle(cx, cy, RADIO, paintFirstCircle);
		c.drawCircle(cx, cy, RADIOINT, paintSecondCircle);
		if (arrow != null && arrowPaint != null){
			arrowPaint.setColor(Color.WHITE);
			c.drawPath(arrow, arrowPaint);
		}
	}

}
