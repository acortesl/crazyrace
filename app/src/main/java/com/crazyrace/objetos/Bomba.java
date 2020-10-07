package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.crazyrace.util.Vector;

/**
 * Created by alex on 3/11/17.
 */

public class Bomba extends Collectable {

    private boolean visible;

    public Bomba(Bitmap bitmap, Vector pos) {
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
