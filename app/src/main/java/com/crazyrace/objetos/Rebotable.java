package com.crazyrace.objetos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.crazyrace.util.Posicion;
import com.crazyrace.util.Vector;


public class Rebotable {
	
	protected static int id = 0;

	protected int idPelota;
	protected Bitmap bmp;
	protected Posicion posicion;
	protected volatile boolean  estaRebotando = false;
	protected Rect bounds;

	public Rebotable(Bitmap bmp, double x, double y){
		this.bmp = bmp;
		idPelota = getId();
		this.posicion = new Posicion(x,y);
		this.bounds = setBounds();
	}
	public Rebotable(Bitmap bmp, double x, double y, int masa){
		this.bmp = bmp;
		idPelota = getId();
		this.posicion = new Posicion(x,y,masa);
		this.bounds = setBounds();
	}

	private Rect setBounds(){
		return new Rect(
		(int)posicion.getPosicion().getX() - getRadio(),
		 (int)posicion.getPosicion().getY() - getRadio(),
		 (int)posicion.getPosicion().getX() + getRadio(),
		 (int)posicion.getPosicion().getY() + getRadio()
		);
	}

//	public Rebotable(Bitmap bmp, boolean isPlayer){
//		this.bmp = bmp;
//		idPelota = getId();
//		this.posicion = new Posicion();
//	}
	
	public static int getId(){
		return id++;
	}
	
	public int getRadio(){
		return bmp.getHeight()/2;
	}
	public int getDiametro(){
		return bmp.getHeight();
	}

	public synchronized Vector getPosicion() {
		return posicion.getPosicion();
	}

	public synchronized void setPosicion(double x, double y) {
		this.posicion.setPosicion(x,y);
	}
	
	public synchronized Vector getAceleracion() {
		return posicion.getAceleracion();
	}

	public synchronized void setAceleracion(double x, double y) {
		this.posicion.setAceleracion(x,y);
	}
	
	public synchronized Vector getPosAnterior() {
		return posicion.getPosAnterior();
	}

	public synchronized void setPosAnterior(double x, double y) {
		this.posicion.setPosAnterior(x,y);
	}
	
	public synchronized void draw(Canvas c,float offset, boolean debugMode){}

	public synchronized void moverPelota(double tiempo){
		this.posicion.moverse(tiempo);
	}

	public synchronized Vector rebotar(Vector normalPlano){
		return this.posicion.rebotar(normalPlano);
	}

	public synchronized void actualizarPosicion(Vector newPos){
		this.posicion.actualizarPosicion(newPos);
//		this.bounds.set(
//				(int)posicion.getPosicion().getX() - getRadio() ,
//				(int)posicion.getPosicion().getY() - getRadio() ,
//				(int)posicion.getPosicion().getX() + getRadio(),
//				(int)posicion.getPosicion().getY() + getRadio()
//		);
	}
	
	public synchronized Rect getBounds(){
		this.bounds.set(
				(int)posicion.getPosicion().getX() - getRadio() ,
				(int)posicion.getPosicion().getY() - getRadio() ,
				(int)posicion.getPosicion().getX() + getRadio(),
				(int)posicion.getPosicion().getY() + getRadio()
		);
		return this.bounds;
	}

	@Override
	public String toString(){
		Vector r = this.posicion.getPosicion();
		Vector a = this.posicion.getAceleracion();
		String s = this.idPelota + " r=[" + String.format("%.2f", r.getX())+'|'+ String.format("%.2f", r.getY())+
				"], a=[" + String.format("%.2f", a.getX())+'|'+ String.format("%.2f", a.getY())+ "]";
		return s;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public Posicion getPos() {
		return this.posicion;
	}

	public void setEstaRebotando(boolean estaRebotando) {
		this.estaRebotando = estaRebotando;		
	}

	public void initPosAnterior(float timeStep){
		posicion.initPosAnterior(timeStep);
	}

}
