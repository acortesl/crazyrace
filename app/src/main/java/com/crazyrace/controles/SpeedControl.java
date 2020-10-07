package com.crazyrace.controles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.crazyrace.objetos.Player;
import com.crazyrace.objetos.Rebotable;

public class SpeedControl extends Control {
	private static final int xAccel = 12000;
	private static final int yAccel = 0;
	private static final int downXAccel = xAccel;
	private static final int downYAccel = yAccel;
	private static final int upXAccel = -xAccel;
	private static final int upYAccel = -yAccel;

	private Player player;
	private Bitmap pressedBitmap;

	public SpeedControl(Player player, Bitmap notPressedBitmap, Bitmap pressedBitmap) {
		super(notPressedBitmap);
		this.player = player;
		this.pressedBitmap = pressedBitmap;
	}

	@Override
	public void initButton(int screenWidth, int ScreenHeight) {
		int x = screenWidth - this.bitmap.getWidth() -10;
		int y = ScreenHeight - this.bitmap.getHeight() - 10;
		int x2 = x + this.bitmap.getWidth();
		int y2 = y + this.bitmap.getHeight();
		this.bounds = new Rect(x,y,x2,y2);
	}

	@Override
	protected void actionDown() {
		this.player.setAceleracion(downXAccel, downYAccel);
	}

	@Override
	protected void actionUp() {
		this.player.setAceleracion(0, upYAccel);
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
	protected void drawPressDefaultButton(Canvas canvas) {}

	@Override
	protected void drawNotPressDefaultButton(Canvas canvas) {}

}
