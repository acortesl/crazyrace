package com.crazyrace.util;

import com.crazyrace.objetos.Player;
import com.crazyrace.objetos.Roca;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by alex on 30/04/18.
 */
public class VectorTest extends TestCase {



    @Test
    public void testVectorUnitario() {
        Vector v1 = new Vector(5,0);
        v1.unitario();
        Assert.assertTrue(v1.equals(new Vector(1.0,0.0)));

        v1 = new Vector(-5,0);
        v1.unitario();
        Assert.assertTrue(v1.equals(new Vector(-1.0,0.0)));

        v1 = new Vector(0,5);
        v1.unitario();
        Assert.assertTrue(v1.equals(new Vector(0.0,1.0)));

        v1 = new Vector(0,-5);
        v1.unitario();
        Assert.assertTrue(v1.equals(new Vector(0.0,-1.0)));
    }


    @Test
    public void testModu1o() {
        Vector v = new Vector(5,0);
        Assert.assertTrue(Vector.modulo(v) == 5);
    }

    @Test
    public void testDistancia(){
        Vector v1 = new Vector(5,0);
        Vector v2 = new Vector(5,5);
        Assert.assertTrue(Vector.distancia(v1,v1) == 0);
        Assert.assertTrue(Vector.distancia(v1,v2) == 5);
    }

    @Test
    public void testSuma(){
        Vector v1 = new Vector(5,0);
        Vector v2 = new Vector(7,3);
        Vector v3 = new Vector(3,-5);
        Vector v4 = new Vector(-5,-9);
        ArrayList<Vector> vectores1 = new ArrayList<>();
        vectores1.add(v1);
        vectores1.add(v2);
        vectores1.add(v3);
        vectores1.add(v4);
        Vector[] vectores2 = new Vector[]{v1,v2,v3,v4};
        System.out.println(Vector.sumarVectores(vectores1));
        System.out.println(Vector.sumarVectores(vectores2));
        Assert.assertTrue(Vector.sumarVectores(vectores1).equals(Vector.sumarVectores(vectores2)));
    }





}