package com.crazyrace.util;

public class Segmento extends Recta {

	private Vector p1;
	private Vector p2;
	
	public Segmento(double a, double b, double c, Vector p1, Vector p2) {
		super(a, b, c);
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Segmento(Vector p1, Vector p2){
		super(p1,p2);
		this.p1 = p1;
		this.p2 = p2;
	}

	public Vector getP1() {
		return p1;
	}

	public void setP1(Vector p1) {
		this.p1 = p1;
	}

	public Vector getP2() {
		return p2;
	}

	public void setP2(Vector p2) {
		this.p2 = p2;
	}

	public Vector puntoMedio(){
		double mx = (p1.getX()+p2.getX())/2;
		double my = (p1.getY()+p2.getY())/2;
		return new Vector(mx,my);
	}
	public Vector calcIntereccionRectaSegmento(Recta r){
//		System.out.println("*****************************");
//		System.out.println("segmento: " + this);
//		System.out.println("recta centros: " + r);
		Vector interseccion = super.calcInterseccion(r);
//		System.out.println("interseccion:" + interseccion);
		if(interseccion == null){
//			System.out.println("A*****************************");
			return null;
		}
		//x1 < xi < x2 && y1 < yi < y2
		if(	((p1.getX() >= interseccion.getX() && interseccion.getX() >= p2.getX()) ||
			(p1.getX() <= interseccion.getX() && interseccion.getX() <= p2.getX())) &&
			((p1.getY() >= interseccion.getY() && interseccion.getY() >= p2.getY())||
			(p1.getY() <= interseccion.getY() && interseccion.getY() <= p2.getY()))) {
//			System.out.println("B*****************************");
			return interseccion;
		}
//		System.out.println("C*****************************");
		return null;
	}
}
