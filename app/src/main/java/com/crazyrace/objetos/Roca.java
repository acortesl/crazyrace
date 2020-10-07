package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.crazyrace.util.Vector;

/**
 * Created by alex on 5/11/17.
 */

public class Roca extends Rebotable {

    private Paint debugPaint;

    public Roca(Bitmap bmp, double x, double y, float density){
        super(bmp, x, y,0);

        debugPaint = new Paint();
        debugPaint.setColor(Color.GREEN);
        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setStrokeWidth(5);
        debugPaint.setTextSize(11.25f*density);
    }

    @Override
    public synchronized void draw(Canvas c,float offset, boolean debugMode) {
        c.drawBitmap(bmp, (float)posicion.getPosicion().getX()-bmp.getWidth()/2-offset, (float)posicion.getPosicion().getY()-bmp.getHeight()/2, null);
        if(debugMode){
            c.drawCircle((float)posicion.getPosicion().getX()-offset, (float)posicion.getPosicion().getY(), getRadio(), debugPaint);
            c.drawCircle((float)posicion.getPosicion().getX()-offset, (float)posicion.getPosicion().getY(), 2, debugPaint);
            debugPaint.setStrokeWidth(1);
            c.drawText(this.toString(), (float)posicion.getPosicion().getX()+bmp.getWidth()/2-offset, (float)posicion.getPosicion().getY()+bmp.getHeight()/2+20, debugPaint);
            c.drawText("RAD: "+getRadio()/2, (float)posicion.getPosicion().getX()+bmp.getWidth()/2-offset, (float)posicion.getPosicion().getY()+bmp.getHeight()/2+50, debugPaint);
            debugPaint.setStrokeWidth(5);
        }
    }

}
