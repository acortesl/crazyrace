package com.crazyrace.objetos;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.Typeface;

public class ProgressBar {
	private float progressbar_width;
	private double gameLenght;
	private double currentPos;
	
	private int screenWidth;
	private int screenHeight;
	private float xStart;
	private float yStart;
	private int paintStroke;
	private Paint painttxt;
	private int[] color = {Color.DKGRAY,Color.GREEN,Color.GREEN,Color.DKGRAY};
	private float[] order = {0f,0.3f,0.7f,1f};
	private Paint paintFondoMarcador;
	private Paint paintFondoBarraProg;
	private Paint paintBarra;
	
	public ProgressBar(double lenght){
		this.gameLenght = lenght;
		this.progressbar_width = 0;
		this.currentPos = 0;
		this.screenHeight = 0;
		this.screenWidth = 0;
		this.xStart = 0;
		this.yStart = 0;
		this.paintStroke = 0;
	}
	
	public void setCurrentPos(double currentPos){
		this.currentPos = currentPos;
	}
	
	public void setScreenSize(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		init();
	}
	
	private void init() {
		progressbar_width = screenWidth / 100 * 40;
		xStart = screenWidth / 2 - progressbar_width /2;
		yStart = 0;
		paintStroke = screenHeight / 100 * 15;

		this.painttxt = initPaintText();
		this.paintFondoMarcador = initPaintFondoMarcador();
		this.paintFondoBarraProg=initPaintFondoBarraProgreso();
		this.paintBarra=initPaintBarra();
	}


	private Paint initPaintText(){
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setTextSize(screenHeight*0.05f);
		p.setColor(Color.BLACK);
		p.setTypeface(Typeface.MONOSPACE);
		p.setTextAlign(Align.CENTER);
		return p;
	}

	private Paint initPaintFondoMarcador(){
		Paint p = new Paint();
		p.setStyle(Style.FILL_AND_STROKE);
		p.setStrokeCap(Paint.Cap.ROUND);
		p.setShader(null);
		p.setStrokeWidth(paintStroke+20);
		p.setColor(Color.GRAY);
		p.setMaskFilter(new EmbossMaskFilter(new float[] { 0, 1, 1 },0.6f, 10, 7.5f));
		return p;
	}

	private Paint initPaintFondoBarraProgreso(){
		Paint p = new Paint();
		p.setStyle(Style.FILL_AND_STROKE);
		p.setStrokeCap(Paint.Cap.ROUND);
		p.setShader(null);
		p.setMaskFilter(new EmbossMaskFilter(new float[] { 0, 1, 1 },0.6f, 10, 7.5f));
		p.setStrokeWidth(paintStroke);
		p.setColor(Color.YELLOW);
		return p;
	}
	private Paint initPaintBarra(){
		Paint p = new Paint();
		p.setStyle(Style.FILL_AND_STROKE);
		p.setStrokeCap(Paint.Cap.ROUND);
		p.setShader(null);
		p.setMaskFilter(new EmbossMaskFilter(new float[] { 0, 1, 1 },0.6f, 10, 7.5f));
		p.setStrokeWidth(paintStroke);
		p.setColor(Color.YELLOW);
		p.setShader(new LinearGradient(screenWidth/2,yStart,screenWidth/2,paintStroke/2,color,order,Shader.TileMode.CLAMP));
		return p;
	}

	public void draw(Canvas canvas){
		float percentage = calcPercentageComplete();
		float currentLength = calcLenght(percentage);
		
		//fondo del marcador

		canvas.drawLine(xStart-5, yStart, xStart+progressbar_width+5, yStart, paintFondoMarcador);
		
		//fondo de la barra de progreso

		canvas.drawLine(xStart, yStart, xStart+progressbar_width, yStart, paintFondoBarraProg);
		
		//barra de progreso

		canvas.drawLine(xStart, yStart, xStart+currentLength, yStart, paintBarra);
		
		//porcentaje
		canvas.drawText((int)percentage + "%", screenWidth/2, paintStroke/3, painttxt);
	}
	
	private float calcLenght(float percentage) {		
		return progressbar_width * percentage / 100;
	}

	private float calcPercentageComplete(){
		float progress = (float)(currentPos * 100 / gameLenght);
		if(progress > 100)return 100;
		if(progress < 0)return 0;
		return progress;
	}

	public boolean isPressed(float x, float y){
		boolean isPressed = false;
		if(
			x > xStart
			&& x < xStart + progressbar_width
			&& y < 	paintStroke
		){
			isPressed = true;
		}
		return isPressed;
	}
}
