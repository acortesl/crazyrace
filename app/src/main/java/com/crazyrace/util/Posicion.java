package com.crazyrace.util;

import android.util.Log;

import com.crazyrace.util.Vector;

import java.util.Random;


public class Posicion {
	private final static float FRICCION = 0.9999f;//0.99f;
	private final static float SLFRICCION = 0.89f;
	private final static float G = 500; //500
	//private final static long timestep = 10;
	
	private Vector posicion;
	private Vector aceleracion;
	private Vector posAnterior;
	private int masa;

	public Posicion(){
		crearPosicion(0,0, G, 1);
	}

	public Posicion(double x, double y){
		crearPosicion(x,y,G, 1);
	}

	public Posicion(double x, double y, int masa){
		crearPosicion(x,y,G,masa);
	}

	public Posicion(double x, double y, float gravity){
		crearPosicion(x,y,gravity,1);
	}

	private void crearPosicion(double x, double y, float gravity,int masa){
		this.masa = masa;
		this.posicion = new Vector(x,y);
		this.posAnterior = new Vector(x,y);
		this.aceleracion = new Vector(0,5000+gravity*masa);
	}
	
	
	public Vector getPosicion() {
		return posicion;
	}
	
	public synchronized void setPosicion(double x, double y) {
		this.posicion.setX(x);
		this.posicion.setY(y);
	}
	
	public  Vector getAceleracion() {
		return aceleracion;
	}

	public void setAceleracion(double x, double y) {
		this.aceleracion.setX(x/(masa*1.0));
		this.aceleracion.setY(y/(masa*1.0)+G*masa);
	}
	public Vector getPosAnterior() {
		return posAnterior;
	}
	public synchronized void setPosAnterior(double x, double y) {
		this.posAnterior.setX(x);
		this.posAnterior.setY(y);
	}
	
	public synchronized void moverse(double timestep){
		double t = timestep; 
		double x = posicion.getX() + FRICCION*(posicion.getX()-posAnterior.getX()) + aceleracion.getX()*t*t;
		double y = posicion.getY() + FRICCION*(posicion.getY()-posAnterior.getY()) + aceleracion.getY()*t*t;
		
		posAnterior.setX(posicion.getX());
		posAnterior.setY(posicion.getY());	
		posicion.setX(x);
		posicion.setY(y);	
	}
	
	public  synchronized Vector rebotar(Vector normalPlano){	
		//Vector aux = posicion;
		
		Vector paraleloPlano = new Vector(-normalPlano.getY(),normalPlano.getX());
		
		double dx = posicion.getX() - posAnterior.getX();
		double dy = posicion.getY() - posAnterior.getY();
		
		double normProyeccion = dx * normalPlano.getX() + dy * normalPlano.getY();
		double normx= normProyeccion * normalPlano.getX();
		double normy= normProyeccion * normalPlano.getY();
		
		double parProyeccion = dx * paraleloPlano.getX() + dy * paraleloPlano.getY();
		double parx = parProyeccion * paraleloPlano.getX();
		double pary = parProyeccion * paraleloPlano.getY();
		
		float coeff = 0.995f;
			
		double x = FRICCION *(-0.01*normalPlano.getX()-0.8*normx)*(1-coeff)+coeff * SLFRICCION *(2*parx)+posAnterior.getX();
		double y = FRICCION *(-0.01*normalPlano.getY()-0.8*normy)*(1-coeff)+coeff * SLFRICCION *(2*pary)+posAnterior.getY();
		
//		Random rr = new Random(System.nanoTime());
//		double mod =  (0 + (0.008 - 0) * rr.nextDouble());
//		double ang =  (0 + (2.0*Math.PI - 0) * rr.nextDouble());
//		Log.v("CHECK POS","anterior: " +posicion.toString() + "nueva: " + x + "," +y);

//		int incFriccion = 10;
//		int i = 0;
//		while((posicion.getX() - x > 1 || posicion.getY() - y > 1)&&i<10 ){
//			Log.v("CHECK POS", "Ajustando... " + incFriccion);
//			x = FRICCION / incFriccion *(-0.01*normalPlano.getX()-0.8*normx)*(1-coeff)+coeff * SLFRICCION *(2*parx)+posAnterior.getX();
//			y = FRICCION / incFriccion *(-0.01*normalPlano.getY()-0.8*normy)*(1-coeff)+coeff * SLFRICCION *(2*pary)+posAnterior.getY();
//			Log.v("CHECK POS","recal:" + x + "," +y);
//			incFriccion *= 10;
//			i++;
//		}

		//posicion = new Vector((x+mod *Math.cos(ang)),(y+mod*Math.sin(ang)));
//		return new Vector((x+mod *Math.cos(ang)),(y+mod*Math.sin(ang)));

//		if(i==10)return null;
		return new Vector((x),(y));
		//posAnterior = aux;
		
		
	}
	
	public synchronized void actualizarPosicion(Vector newPos){
		this.posAnterior = this.posicion;
		this.posicion = newPos;
	}

	public void initPosAnterior(float timeStep){
		//x_{i-1} = x_{i} - v_{i} dt + 1/2 a dt²
		//v = 0
		double x = posicion.getX() + 0.5 * aceleracion.getX()*timeStep*timeStep;
		double y = posicion.getY() + 0.5 * aceleracion.getY()*timeStep*timeStep;
		posAnterior.setX(x);
		posAnterior.setY(y);
	}

	public int getMasa() {
		return masa;
	}

	public String toString(){
		return "Posicion=[" + posicion.getX() + "," + posicion.getY() + "], "+
				"Posicion anterior=[" + posAnterior.getX() + "," + posAnterior.getY() + "], "+
				"Aceleracion=[" + aceleracion.getX() + "," + aceleracion.getY() + "]";
	}
	
}
