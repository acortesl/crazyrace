package com.crazyrace.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



public class Vector {


	private double x;
	private double y;

	public Vector(){
		this.x=0;
		this.y=0;
	}

	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString(){
		return "x: " + x + " y: " + y;
	}

	public void unitario(){
		double mod = modulo(this);
		this.x = this.x / mod;
		this.y = this.y / mod;
	}

	public static double modulo(Vector v1){
		return Math.sqrt(Math.pow(v1.getX(),2)+Math.pow(v1.getY(),2));
	}

	public static double distancia(Vector v1, Vector v2){
		return Math.sqrt(Math.pow((v2.getX()-v1.getX()),2)+Math.pow((v2.getY()-v1.getY()),2));
	}

	public static Vector sumarVectores(Vector v1, Vector v2){
		return new Vector(v1.getX()+v2.getX(), v1.getY() + v2.getY());
	}

	public static Vector sumarVectores(Vector[] vectores){
		double x=0;
		double y=0;
		for(Vector v: vectores){
			if(v!=null){
				x+=v.getX();
				y+=v.getY();
			}
		}
		return new Vector(x, y);
	}

	public static Vector sumarVectores(ArrayList<Vector> vectores){
		double x=0;
		double y=0;
		for(Vector v: vectores){
			x+=v.getX();
			y+=v.getY();
		}
		return new Vector(x, y);
	}

	public void writeTo(DataOutputStream out) throws IOException{	
		out.writeDouble(x);
		out.writeDouble(y);			
	}

	public static Vector readFrom(DataInputStream in) throws IOException{
		double x = in.readDouble();
		double y = in.readDouble();
		return new Vector(x,y);
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof Vector) {
			Vector v = (Vector) o;			
			if (v.getX() == this.x && v.getY() == this.getY())return true; 
		} 
		return false; 
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (int) (Double.doubleToLongBits(this.x)^((Double.doubleToLongBits(this.x) >>> 32)));
		result = 31 * result + (int) (Double.doubleToLongBits(this.y)^((Double.doubleToLongBits(this.y) >>> 32)));
		return result;
	}

	public static Vector getRandomVector(int sizeX, int sizeY, Random r){
		int x = r.nextInt(sizeX - (2 * Constants.SCREEN_MARGIN_X)) + Constants.SCREEN_MARGIN_X;
		int y = r.nextInt(sizeY - (2 * Constants.SCREEN_MARGIN_Y)) + Constants.SCREEN_MARGIN_Y;
		return new Vector(x,y);
	}
}
