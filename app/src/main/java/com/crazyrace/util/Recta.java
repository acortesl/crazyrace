package com.crazyrace.util;

public class Recta {

	/*
	 * Recta Ax + By + c = 0
	 */
	private double a;
	private double b;
	private double c;
	
	public Recta(double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Recta(Vector p1, Vector p2){
		Vector v12 = new Vector(p2.getX()-p1.getX(), p2.getY()-p1.getY());
		this.a = v12.getY();
		this.b = (-1)*v12.getX();
		this.c = (-1) * v12.getY() * p1.getX() + v12.getX() * p1.getY();
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
	
	private boolean isSecante(Recta r){
//		System.out.println("is secante: " + r);
//		System.out.println("is secante: a" + a + " b: " +b);
		if((r.getA() * b) != (r.getB() * a)) {
//			System.out.println("secante");
			return true;
		}
//		System.out.println("no secante");
		return false;
	}
	
	//interseccion entre Ax+By+C=0 y Dx+Ey+F=0
	public Vector calcInterseccion(Recta r){
		if(!isSecante(r)) return null;
//		System.out.println("aaa");
		double d = r.getA();
		double e = r.getB();
		double f = r.getC();
		double y = (d * c - f * a) / (a * e - b * d);
		double x = ((-b) / a) * y - (c / a);
//		System.out.println("x: " + x + " y: "+y);
		return new Vector(x,y);
	}
	
	@Override
	public String toString(){
		return a + "x " + b + "y " + c;
	}
	
}
