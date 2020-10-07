package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Matrix;

import com.crazyrace.util.Conversor;
import com.crazyrace.util.Vector;

/**
 * Created by alex on 5/11/17.
 */

public class Player extends Rebotable {

    private Bitmap escudoBmp;
    private boolean hasEscudo;
    private float xEscudo;
    private float yEscudo;

    private Paint pTemp;


    public Player(Bitmap bmp, double x, double y, float density, float timeStep, int masa){
        super(bmp, Conversor.dpToPixel(x,density), Conversor.dpToPixel(y,density),masa);
        this.initPosAnterior(timeStep);
        initPlayer(bmp,  x,  y,  density,  timeStep, masa);

    }

    public Player(Bitmap bmp, double x, double y, float density, float timeStep){
        super(bmp, x, y, 1);
        this.initPosAnterior(timeStep);
        initPlayer(bmp,  x,  y,  density,  timeStep, 1);

    }
    private void initPlayer(Bitmap bmp, double x, double y, float density, float timeStep, int masa){
        this.hasEscudo = false;
        pTemp = new Paint();
        pTemp.setColor(Color.GREEN);
        pTemp.setStyle(Paint.Style.STROKE);
        pTemp.setStrokeWidth(5);
        pTemp.setTextSize(11.25f*density);
    }

    Matrix rotator = new Matrix();
    int degree = 10;
    @Override
    public synchronized void draw(Canvas c,float offset, boolean debugMode){
        Vector pos = posicion.getPosicion();
        if(this.escudoBmp != null && hasEscudo){

         //   degree+=10;
//        double dx1 = pos.getX()- posicion.getPosAnterior().getX();
//        double dy1 = pos.getY()- posicion.getPosAnterior().getY();
//            rotator.preRotate(degree,escudoBmp.getWidth()/2,escudoBmp.getHeight()/2);
//            rotator.postTranslate((float)posicion.getIncremento().getX()-getRadio()-offset- xEscudo,(float)posicion.getIncremento().getY()-getRadio()- yEscudo /2-25);
//            c.drawBitmap(escudoBmp,rotator,null);
            c.drawBitmap(escudoBmp, (float)pos.getX()-getRadio()-offset- xEscudo, (float)pos.getY()-getRadio()- yEscudo /2-25, null);
        }
        c.drawBitmap(bmp, (float)pos.getX()-getRadio()-offset, (float)pos.getY()-getRadio(), null);
        if(debugMode){
            c.drawCircle((float)pos.getX()-offset, (float)pos.getY(), getRadio(), pTemp);
            c.drawCircle((float)pos.getX()-offset, (float)pos.getY(), 4, pTemp);

            pTemp.setStrokeWidth(1);
            c.drawText(this.toString(), (float)pos.getX()+bmp.getWidth()/2-offset, (float)pos.getY()+bmp.getHeight()/2+20, pTemp);
            c.drawText("RAD: "+getRadio()/2 + " MASA: " + posicion.getMasa(), (float)pos.getX()+bmp.getWidth()/2-offset, (float)pos.getY()+bmp.getHeight()/2+50, pTemp);
            pTemp.setStrokeWidth(5);
            double dx = (pos.getX() - posicion.getPosAnterior().getX())*1000;
            double dy = (pos.getY() - posicion.getPosAnterior().getY())*1000;
            c.drawLine((float)pos.getX()-offset,(float)pos.getY(),(float)(pos.getX()+dx-offset),(float)(pos.getY()+dy),pTemp);
            pTemp.setColor(Color.RED);
            c.drawCircle((float)posicion.getPosAnterior().getX()-offset, (float)posicion.getPosAnterior().getY(), 2, pTemp);
            pTemp.setColor(Color.GREEN);
        }


    }

    public Bitmap getEscudoBmp() {
        return escudoBmp;
    }

    public void setEscudoBmp(Bitmap escudoBmp) {
        this.escudoBmp = escudoBmp;
        xEscudo = (escudoBmp.getWidth() - bmp.getWidth())/2;
        yEscudo = (escudoBmp.getHeight() - bmp.getHeight())/2;
    }

    public boolean hasEscudo() {
        return hasEscudo;
    }

    public void setHasEscudo(boolean hasEscudo) {
        this.hasEscudo = hasEscudo;
    }
}
