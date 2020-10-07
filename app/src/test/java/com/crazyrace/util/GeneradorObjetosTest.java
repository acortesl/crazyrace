package com.crazyrace.util;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class GeneradorObjetosTest  extends TestCase {




    @Test
    public void testRaceLength(){
        int level = 1;
        Assert.assertTrue(level * Constants.STAGE_INCREMENT + Constants.STAGE_LENGTH == 5200);
         level = 10;
        Assert.assertTrue(level * Constants.STAGE_INCREMENT + Constants.STAGE_LENGTH == 7000);
    }

    @Test
    public void testObstaculos() {
        Assert.assertTrue(GeneradorObjetos.calcNObs(1,1) == 7);
        Assert.assertTrue(GeneradorObjetos.calcNObs(1,2) == 8);
        Assert.assertTrue(GeneradorObjetos.calcNObs(1,3) == 9);
        Assert.assertTrue(GeneradorObjetos.calcNObs(2,1) == 9);
        Assert.assertTrue(GeneradorObjetos.calcNObs(2,2) == 11);
        Assert.assertTrue(GeneradorObjetos.calcNObs(2,3) == 13);
    }


    @Test
    public void testBombas(){
        Assert.assertTrue(GeneradorObjetos.calcNBombas(1,1) == 2);
        Assert.assertTrue(GeneradorObjetos.calcNBombas(1,2) == 3);
        Assert.assertTrue(GeneradorObjetos.calcNBombas(1,3) == 4);
        Assert.assertTrue(GeneradorObjetos.calcNBombas(2,1) == 2);
        Assert.assertTrue(GeneradorObjetos.calcNBombas(2,2) == 3);
        Assert.assertTrue(GeneradorObjetos.calcNBombas(2,3) == 4);
    }

    @Test
    public void testEscudos(){
        Assert.assertTrue(GeneradorObjetos.calcNAuras(1,1) == 1);
        Assert.assertTrue(GeneradorObjetos.calcNAuras(1,2) == 2);
        Assert.assertTrue(GeneradorObjetos.calcNAuras(1,3) == 3);
        Assert.assertTrue(GeneradorObjetos.calcNAuras(2,1) == 1);
        Assert.assertTrue(GeneradorObjetos.calcNAuras(2,2) == 2);
        Assert.assertTrue(GeneradorObjetos.calcNAuras(2,3) == 3);
    }
    @Test
    public void testCantidadObejtos() {
        System.out.println("***********************");
        System.out.println("* Cantidad de Objetos *");
        System.out.println("***********************");
        int start = 1;
        int end =  4;

        System.out.println("obstaculos");
        for(int stage = start ; stage< end ; stage++){
            for(int level = 1 ; level < 4 ; level++){
                System.out.println(GeneradorObjetos.calcNObs(stage,level));
            }
        }


        System.out.println("bombas");
        for(int stage = start ; stage< end ; stage++){
            for(int level = 1 ; level < 4 ; level++){
                System.out.println(GeneradorObjetos.calcNBombas(stage,level));
            }
        }


        System.out.println("escudos");
        for(int stage = start ; stage< end ; stage++){
            for(int level = 1 ; level < 4 ; level++){
                System.out.println(GeneradorObjetos.calcNAuras(stage,level));
            }
        }


    }





}
