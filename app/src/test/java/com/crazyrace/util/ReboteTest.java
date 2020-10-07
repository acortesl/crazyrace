package com.crazyrace.util;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ReboteTest extends TestCase {



	@Test
	public void testCalculoRbote() {

		float FRICCION = 1;//0.99f;
		float SLFRICCION = 1;
		float G = 500;


		Vector posicion = new Vector(5,0);
		Vector posAnterior = new Vector(3,2);
		Vector vPlano = new Vector(5,0); //vector plano inicial
		System.out.println("P anterior: " + posAnterior);
		System.out.println("P inicial: " + posicion);

		System.out.println("vector pared de rebote: " + vPlano);

		Vector normalPlano = new Vector(-vPlano.getY(),vPlano.getX());
		System.out.println("vector normal a pared de rebote: " + normalPlano);


		normalPlano.unitario();
		System.out.println("vector normal pared de rebote unitario: " + normalPlano);


		//aqui empieza la funcion


		Vector paraleloPlano = new Vector(-normalPlano.getY(),normalPlano.getX());
		System.out.println("Vector paralelo pared de rebote: " + paraleloPlano);

		double dx = posicion.getX() - posAnterior.getX();
		double dy = posicion.getY() - posAnterior.getY();
		System.out.println("Vector desplazamiento posAnt -pos: " + dx + "," + dy);

		double normProyeccion = dx * normalPlano.getX() + dy * normalPlano.getY();
		System.out.println("Proyeccion normal: " + normProyeccion);
		double normx= normProyeccion * normalPlano.getX();
		double normy= normProyeccion * normalPlano.getY();
		System.out.println("normal x: " + normx);
		System.out.println("normal y: " + normy);

		double parProyeccion = dx * paraleloPlano.getX() + dy * paraleloPlano.getY();
		System.out.println("Proyeccion paralela: " + parProyeccion);
		double parx = parProyeccion * paraleloPlano.getX();
		double pary = parProyeccion * paraleloPlano.getY();
		System.out.println("par x: " + parx);
		System.out.println("par y: " + pary);


		float coeff = 1f;

//        double x = FRICCION *(-0.01*normalPlano.getX()-0.8*normx)*(1-coeff)+coeff * SLFRICCION *(2*parx)+posAnterior.getX();
		//      double y = FRICCION *(-0.01*normalPlano.getY()-0.8*normy)*(1-coeff)+coeff * SLFRICCION *(2*pary)+posAnterior.getY();
		double x = (2*parx)+posAnterior.getX();
		double y = (2*pary)+posAnterior.getY();

		System.out.println("nueva x: " + x);
		System.out.println("nueva y: " + y);

		Assert.assertTrue(x == 7.0d);
		Assert.assertTrue(y == 2.0d);
	}

	public void test() throws Exception {
		Recta r1 = new Recta(new Vector(7,10),new Vector(10,13));
		Recta r2 = new Recta(new Vector(10,7),new Vector(7,10));
		Vector inters = r1.calcInterseccion(r2);
		assertTrue(inters.equals(new Vector(7,10)));
	//	System.out.println(r1);
	//	System.out.println(r2);
		//secantes
		Segmento s1 = new Segmento(new Vector(7,10),new Vector(10,13));
		Segmento s2 = new Segmento(new Vector(10,7),new Vector(7,10));
		Vector intersSegmento = s1.calcIntereccionRectaSegmento(s2);
		assertTrue(intersSegmento.equals(new Vector(7,10)));
		
		//paralelas
		Segmento s3 = new Segmento(new Vector(7,10),new Vector(10,13));
		Segmento s4 = new Segmento(new Vector(10,7),new Vector(13,10));
		Vector intersSegmento2 = s3.calcIntereccionRectaSegmento(s4);
		assertTrue(intersSegmento2 == null);
		
		Vector m = s1.puntoMedio();
		assertTrue(m.equals(new Vector(8.5,11.5)));
		
		Vector v1 = new Vector(3,5);
		Vector v2 = new Vector(4,6);
		Vector v3 = new Vector(5,7);
		Vector[] vec = {
				v1,v2,v3
		};
		Vector v = Vector.sumarVectores(vec);
		//System.out.println(v);
	}
	
	public void testColisiones() throws Exception {
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
		
		int radioPlayer = 74;
		Vector posPlayer = new Vector(474,474);
		Recta rc = new Recta(posPlayer,c);
		System.out.println(rc.toString());
		
		Vector[] posiblesPuntosChoque = new Vector[r.length];
		for(int i = 0; i< r.length; i++){
			posiblesPuntosChoque[i] = r[i].calcIntereccionRectaSegmento(rc);
			System.out.println((posiblesPuntosChoque[i]==null)?"null":posiblesPuntosChoque[i].toString());
		}
		System.out.println(r[0]);
		
		Recta l = new Recta(v[0],v[1]);
		System.out.println(l);
		System.out.println(l.calcInterseccion(rc));
		
		Vector[] puntosChoque = new Vector[posiblesPuntosChoque.length];
		double d;
		for(int i = 0; i< posiblesPuntosChoque.length; i++){
			if(posiblesPuntosChoque[i] != null){
				d = Vector.distancia(posiblesPuntosChoque[i], posPlayer);
				if(d <= radioPlayer){
					puntosChoque[i] = posiblesPuntosChoque[i];
					System.out.println((posiblesPuntosChoque[i]==null)?"null":puntosChoque[i].toString());
				}else{
					puntosChoque[i] = null;
				}
			}
		}

		
		
	}
	
}
