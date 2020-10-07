package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.crazyrace.util.Vector;

public class Coin extends Collectable {

	private boolean visible;
	
	public Coin(Bitmap bitmap, Vector pos) {	
		super(bitmap, pos);
		visible = true;
	
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public void draw(Canvas c, double offset){
		if(visible)
			super.draw(c,offset);
	}

}
