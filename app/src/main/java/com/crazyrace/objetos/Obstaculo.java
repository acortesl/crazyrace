package com.crazyrace.objetos;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.crazyrace.util.Posicion;
import com.crazyrace.util.Recta;
import com.crazyrace.util.Segmento;
import com.crazyrace.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Obstaculo {
	private Vector centro;
	private Vector[] vertices;
	private Segmento[] segmentos;
	private Path path;
	private Paint p;
	Paint p2;
	private double radio;
	private Posicion pos;


	///////////////

	//BORRAR CLASE

	///////////////
	public Obstaculo(){
		this.p = new Paint();
		this.p.setColor(Color.BLUE);
		this.p.setStrokeWidth(5);
		this.p.setStyle(Style.FILL_AND_STROKE);
		pos = new Posicion(500,400);
		p2= new Paint();
		p2.setColor(Color.RED);
		p2.setStyle(Style.FILL);
		p2.setTextSize(15);
	}

	
	public Obstaculo(Vector centro, Vector[] vertices, Segmento[] segmentos){
		this.centro = centro;
		this.vertices = vertices;
		this.segmentos = segmentos;
		this.path = path;
		this.p = new Paint();
		this.p.setColor(Color.GREEN);
		this.p.setStrokeWidth(5);
		this.p.setStyle(Style.FILL_AND_STROKE);
		this.radio = Vector.distancia(centro, vertices[0]);
		p2= new Paint();
		p2.setColor(Color.GREEN);
		p2.setStyle(Style.FILL);
		p2.setTextSize(15);
	}
	
	double last =0;
	public int getRadio(){
		return 100;
	}
	public Vector getPosicion() {
		return pos.getPosicion();
	}
	public void draw(Canvas canvas, double offset){
		canvas.drawRect(400,300,600,500,p);
		canvas.drawCircle((int)pos.getPosicion().getX(),(int)pos.getPosicion().getY(),2,p2);
	}
	 public Vector getReboteVector(Vector posP){
		double xR = pos.getPosicion().getX();
		double yR = pos.getPosicion().getY();
		double xP = posP.getX();
		double yP = posP.getY();
		 List<Vector> rebotes = new ArrayList<Vector>();
		if(yR - yP >= 0 && xR-getRadio() <= xP && xR+getRadio()>= xP){
			rebotes.add(new Vector(0,-5));
		}else if(yR - yP <= 0 && xR-getRadio() <= xP && xR+getRadio()>= xP){
			rebotes.add(new Vector(0,5));
		}
		 if(xR - xP >= 0 && yR-getRadio() <= yP && yR+getRadio()>=yP){
			 rebotes.add(new Vector(-5,0));
		 }else if(xR - xP <= 0&& yR-getRadio() <= yP && yR+getRadio()>=yP){
			 rebotes.add(new Vector(5,0));
		 }
		 if(rebotes.isEmpty()){
		 	if(xP >= xR+getRadio() && yP <= yR-getRadio()){
				rebotes.add(new Vector(5,-5));
			}else if(xP >= xR+getRadio() && yP >= yR+getRadio()){
				rebotes.add(new Vector(5,5));
			}else if(xP <= xR-getRadio() && yP >= yR+getRadio()){
				rebotes.add(new Vector(-5,5));
			}else if(xP <= xR-getRadio() && yP <= yR-getRadio()){
				rebotes.add(new Vector(-5,-5));
			}
		 }
		 if(!rebotes.isEmpty())
		 	return rebotes.get(0);
		 return null; //imposible..
	 }
	public void draw2(Canvas canvas, double offset){
		
		//canvas.drawPath(path, p);
		for(int i = 0; i<vertices.length; i++){
			if(i == vertices.length -1){
				canvas.drawLine((float)(vertices[i].getX()-offset),
						(float) vertices[i].getY(),
						(float)(vertices[0].getX()-offset), 
						(float)vertices[0].getY(), p);
			}else{
				canvas.drawLine((float)(vertices[i].getX()-offset),
						(float) vertices[i].getY(),
						(float)(vertices[i+1].getX()-offset), 
						(float)vertices[i+1].getY(), p);
			}
		}
		canvas.drawPoint((float)(centro.getX()-offset), (float)centro.getY(), p);
		canvas.drawText("Radio: " + radio, (float)centro.getX(), (float)centro.getY()+120, p2);
		canvas.drawText("Pos: " + centro.toString(), (float)centro.getX(), (float)centro.getY()+140, p2);
		canvas.drawText("dist: " + dist, (float)centro.getX(), (float)centro.getY()+160, p2);
		canvas.drawText("reboto: " + reb, (float)centro.getX(), (float)centro.getY()+180, p2);
		canvas.drawText("direccion: " + rebote, (float)centro.getX(), (float)centro.getY()+200, p2);
		
		if(posiblesPuntosChoque != null){
			for(int i = 0,j=0; i< segmentos.length; i++,j+=20){
				if(posiblesPuntosChoque[i] != null)
				canvas.drawText("posiblesPuntosChoque: " + posiblesPuntosChoque[i].toString(), (float)centro.getX()+200, (float)centro.getY()+j, p2);
			}
			
		}
		
		
		if(puntosChoque != null){
			for(int i = 0,j=0; i< segmentos.length; i++,j+=20){
				if(puntosChoque[i] != null)
				canvas.drawText("puntosChoque: " + puntosChoque[i].toString(), (float)centro.getX()+500, (float)centro.getY()+j, p2);
				canvas.drawText("index: " + index,(float)centro.getX()+500, (float)centro.getY()-100, p2);
			}
			
		}
		
		
	}
	double dist;
	boolean reb;
	Vector rebote;
	Vector[] posiblesPuntosChoque;
	Vector[] puntosChoque;
	int index;
	public Vector rebotar(Player p){
		//atencion es diametro y el centro es la esquina superior
		int radioPlayer = p.getRadio()/2;
//		System.out.println("radioplayer: " +radioPlayer);
		Vector posPlayer = new Vector(p.getPosicion().getX()+radioPlayer,p.getPosicion().getY()+radioPlayer);
//		System.out.println("posPlayer: " +posPlayer);
		//si la pelota esta lejos no calculo nada
		
		dist = Vector.distancia(posPlayer, centro);
//		System.out.println("radio + radioPlayer: " +(radio + radioPlayer));
//		System.out.println("dist: " +dist);
		//Log.v("OBSTACULO", "dist actual: " + dist);
		//Log.v("OBSTACULO", "dist para colisionar: " + (radio + radioPlayer));
		if(dist > (radio + radioPlayer)) {
			reb = false;
			return null;
		}
		reb = true;
//		System.out.println("calculo?: " +reb);
		
	
	
		//Log.v("OBSTACULO", "########### REBOTANDO ##########");
		//calculo la recta rc entre centros
		Recta rc = new Recta(posPlayer,centro);
//		System.out.println("recta entre centros: " +rc);
//		System.out.println("--------------------------");
		//calculo los posibles puntos de choque entre el player y el obstaculo
		//(puntos de corte entre segmentos y la recta rc)
//		Vector[] posiblesPuntosChoque = new Vector[segmentos.length];
		posiblesPuntosChoque = new Vector[segmentos.length];
		for(int i = 0; i< segmentos.length; i++){
//			System.out.println("Segmento: "+segmentos[i].getP1() +" " +segmentos[i].getP2());
			posiblesPuntosChoque[i] = segmentos[i].calcIntereccionRectaSegmento(rc);
//			System.out.println("posiblesPuntosChoque  " +i + ": " +posiblesPuntosChoque[i]);
			
			
		}
//		System.out.println("--------------------------");
		//compruebo la distancia entre el player y los posibles de puntos de choque 
//		Vector[] puntosChoque = new Vector[posiblesPuntosChoque.length];
		puntosChoque = new Vector[posiblesPuntosChoque.length];
		double d;
		for(int i = 0; i< posiblesPuntosChoque.length; i++){
			if(posiblesPuntosChoque[i] != null){
				d = Vector.distancia(posiblesPuntosChoque[i], posPlayer);
			//	Log.v("OBSTACULO", "Posible.Int: " + posiblesPuntosChoque[i].toString());
			//	Log.v("OBSTACULO", "posPlayer: " + posPlayer.toString());
			//	Log.v("OBSTACULO", "Dist player-punto: " + d);
				if(d <= radioPlayer){
					puntosChoque[i] = posiblesPuntosChoque[i];
				}else{
					puntosChoque[i] = null;
				}
			//	if(puntosChoque[i] != null )Log.v("OBSTACULO", "Interseccion: " + puntosChoque[i].toString());
			}
//			System.out.println("puntosChoque  " +i + ": " +puntosChoque[i]);
		}
//		System.out.println("--------------------------");
		//calculo la direccion del rebote para cada punto de choque
		Vector[] vectorRebote = new Vector[puntosChoque.length];
		for(int i = 0; i< puntosChoque.length; i++){
			if(puntosChoque[i] != null){
				index = i;
				Vector v = calcVectorRebote(i);
//				System.out.println("reboteee: " + v);
				v.unitario();
//				System.out.println("unitario: " + v);
//				vectorRebote[i] = p.rebotar(v);
				vectorRebote[i] = v;
				//vectorRebote[i] = calcVectorRebote(i);
				//vectorRebote[i].unitario();
			//	Log.v("OBSTACULO", "Rebote: " + vectorRebote[i].toString());
			}
//			System.out.println("vectorRebote  " +i + ": " +vectorRebote[i]);
		}
		//sumo rebotes y reboto
		rebote = Vector.sumarVectores(vectorRebote);
//		System.out.println("rebote : " +rebote);
		return rebote;
		//Log.v("OBSTACULO", "Rebote total: " + rebote.toString());
		/*if(rebote.getX() + rebote.getY() > 0){
		//	Log.v("OBSTACULO", "REBOTO");
			p.actualizarPosicion(rebote);
		}*/
		//Log.v("OBSTACULO", "########### FIN REBOTAR ##########");
		
	}
	
	private Vector calcVectorRebote(int index) {
		Vector puntoMedio = segmentos[index].puntoMedio();
//		System.out.println("pm: " +puntoMedio );
		double rx = puntoMedio.getX()-centro.getX();
		double ry = puntoMedio.getY()-centro.getY();
//		System.out.println("rx: " + rx + " ry: " + ry);
		return new Vector(rx,ry);
	}

	public static Obstaculo crearObstauclo(){
		Vector c = new Vector(500,500);
		Vector[] v = {
				new Vector(400,500),
				new Vector(500,400),
				new Vector(600,500),
				new Vector(500,600)
		};
		Segmento[] r = {
				new Segmento(v[0],v[1]),
				new Segmento(v[1],v[2]),
				new Segmento(v[2],v[3]),
				new Segmento(v[3],v[0])
		};
//		Path p = new Path();
//		p.moveTo((float)v[0].getX(), (float)v[0].getY());
//		p.lineTo((float)v[1].getX(), (float)v[1].getY());
//		p.lineTo((float)v[2].getX(), (float)v[0].getY());
//		p.lineTo((float)v[3].getX(), (float)v[0].getY());
//	//	p.lineTo((float)v[0].getX(), (float)v[0].getY());
//		p.close();
		return new Obstaculo(c,v,r);
	}

//	public double getRadio(){
//		return this.radio;
//	}
	
	public Vector getCentro(){
		return this.centro;
	}
}
