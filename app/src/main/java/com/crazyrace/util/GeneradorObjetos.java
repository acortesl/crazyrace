package com.crazyrace.util;


import android.graphics.Bitmap;

import com.crazyrace.objetos.Aura;
import com.crazyrace.objetos.Bomba;
import com.crazyrace.objetos.Coin;
import com.crazyrace.objetos.Rebotable;
import com.crazyrace.objetos.Roca;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorObjetos {


    private static float getRaceLengt(int stage){
        return stage * Constants.STAGE_INCREMENT + Constants.STAGE_LENGTH;
    }

    public static int calcStageLenght(int stage, float density){
        return (int) (getRaceLengt(stage) * density);
    }
    public static int calcNAuras(int stage, int level){
        return (int)(Constants.BOMBAS_PER_STAGE+(stage*1.0/4)+level-1);
    }
    public static int calcNBombas(int stage, int level){
        return (int)(Constants.BOMBAS_PER_STAGE+(stage*1.0/4)+level);
    }
    public static int calcNObs(int stage, int level){
        return (int)(Constants.OBS_PER_STAGE+(stage*1.0/2)*2*(level+1));
    }

    public static List<Aura> generateAuras(Bitmap bitmap, int stage, int level, int screenX, int screenY){
        ArrayList<Aura> auras = new ArrayList<>();
        Random r = new Random(stage * Constants.K_AURAS);
        int virtualX = (int) getRaceLengt(stage);
        int nAura = calcNAuras(stage,level);
        while(auras.size() < nAura){
            Vector posVirtual = Vector.getRandomVector(virtualX,Constants.VIRTUAL_STAGE_HEIGH,r); // posicion aleatoria en pantalla virtual
            Vector objpos = translateToRealScreen(posVirtual, screenX, screenY, virtualX, Constants.VIRTUAL_STAGE_HEIGH); // posicion en pantalla real

            auras.add(new Aura(bitmap, objpos));
        }
        return auras;
    }

    public static List<Bomba> generateBombas(Bitmap bitmap, int stage, int level, int screenX, int screenY){
        ArrayList<Bomba> bombas = new ArrayList<>();
        Random r = new Random(stage * Constants.K_BOMBAS);
        int virtualX = (int) getRaceLengt(stage);
        int nBomb = calcNBombas(stage,level);
        while(bombas.size() < nBomb){
            Vector posVirtual = Vector.getRandomVector(virtualX,Constants.VIRTUAL_STAGE_HEIGH,r); // posicion aleatoria en pantalla virtual
            Vector objpos = translateToRealScreen(posVirtual, screenX, screenY, virtualX, Constants.VIRTUAL_STAGE_HEIGH); // posicion en pantalla real
            bombas.add(new Bomba(bitmap, objpos));
        }
        return bombas;
    }
    public static List<Coin> generateCoins(Bitmap bitmap, int stage, int screenX, int screenY){
        ArrayList<Coin> coins = new ArrayList<>();
        Random r = new Random(stage * Constants.K_MONEDAS);
        int virtualX = (int) getRaceLengt(stage);
        while(coins.size() < Constants.COINS_PER_STAGE){
            Vector posVirtual = Vector.getRandomVector(virtualX,Constants.VIRTUAL_STAGE_HEIGH,r); // posicion aleatoria en pantalla virtual
            Vector coinpos = translateToRealScreen(posVirtual, screenX, screenY, virtualX, Constants.VIRTUAL_STAGE_HEIGH); // posicion en pantalla real
            coins.add(new Coin(bitmap,coinpos));
        }
        return coins;
    }

    public static List<Roca> generateObstacle(Bitmap[] bitmaps, int stage, int level, int screenX, int screenY, float density){
        ArrayList<Roca> obs = new ArrayList<>();
        int nObs = calcNObs(stage,level);
        Random r = new Random(stage * Constants.K_OBSTACULOS);
        // Math.floor(Math.random()*(N-M+1)+M);
        int virtualX = (int) getRaceLengt(stage);

        while(obs.size() < nObs){
            int nRocaBmp = (int) Math.floor(Math.random()*bitmaps.length); // eleccion de imagen de roca aleatoria
            Vector posVirtual = Vector.getRandomVector(virtualX,Constants.VIRTUAL_STAGE_HEIGH,r); // posicion aleatoria en pantalla virtual
            Vector posicion = translateToRealScreen(posVirtual, screenX, screenY, virtualX, Constants.VIRTUAL_STAGE_HEIGH); // posicion en pantalla real
            obs.add(new Roca(bitmaps[nRocaBmp], posicion.getX(),posicion.getY(),density));
        }
        return obs;
    }

    public static Vector translateToRealScreen(Vector posVirtual, int screenX, int screenY, int virtualX, int virtualY){
        double x = posVirtual.getX()*screenX/virtualX;
        double y = posVirtual.getY()*screenY/virtualY;
        posVirtual.setX(x);
        posVirtual.setY(y);
        return posVirtual;
    }


}
