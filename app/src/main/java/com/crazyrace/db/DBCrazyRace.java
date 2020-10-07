package com.crazyrace.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.crazyrace.objetos.Ranking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alex on 2/11/17.
 */

public class DBCrazyRace {

    private DBOpener db;
    private SQLiteDatabase database;
    private int version;
    private Context c;
    public enum TipoDB{ACTUALIZAR,JUGAR};

    public DBCrazyRace(Context c, TipoDB tipo){
        this.c = c;
        version = leerVersion();
        if(tipo == TipoDB.ACTUALIZAR) {
            actualizarBD();
        }
        db = new DBOpener(c, version);

    }

    private int leerVersion(){
        int ver;
        try {
            FileInputStream in = c.openFileInput("version");
            DataInputStream dis = new DataInputStream(in);
            ver = dis.readInt();
            dis.close();
        } catch (IOException e) {
            ver = 1;
        }
        return ver;
    }

    private void actualizarBD(){
        try{
            version++;
            FileOutputStream out = c.openFileOutput("version",Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(version);
            dos.close();
        } catch (IOException e) {

        }
    }

    private void insertR(int stage,int level, int coins, long time, String timeStr){
        database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBOpener.T_R_STAGE, stage);
        values.put(DBOpener.T_R_LEVEL, level);
        values.put(DBOpener.T_R_COINS, coins);
        values.put(DBOpener.T_R_TIME, time);
        values.put(DBOpener.T_R_TIMESTR, timeStr);
        try{
            database.insertOrThrow(DBOpener.T_RANKING, null, values);
        }catch(Exception e){

        }
        database.close();
    }

    private void updateR(int stage,int level, int coins, long time, String timeStr){
        database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBOpener.T_R_COINS, coins);
        values.put(DBOpener.T_R_TIME, time);
        values.put(DBOpener.T_R_TIMESTR, timeStr);

        String[] where = {stage+"",level+""};

        database.update(DBOpener.T_RANKING, values, DBOpener.T_R_STAGE+"=? and "+DBOpener.T_R_LEVEL+"=?",where);
        database.close();
    }

    private Ranking getR(int stage, int level){
        String query = "SELECT * FROM " + DBOpener.T_RANKING + " where " + DBOpener.T_R_STAGE + " = ? and " +DBOpener.T_R_LEVEL + " = ?";
        String[] where = {stage+"",level+""};
        database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, where);
        if(cursor.getCount() <= 0){
            cursor.close();
            database.close();
            return null;
        }
        cursor.moveToFirst();
        Ranking r = Ranking.createRanking(cursor);
        cursor.close();
        database.close();
        return r;
    }

    public void updateRanking(int stage,int level, int coins, long time, String timeStr){
        Ranking r = getR(stage,level);
        if(r == null){
            insertR(stage, level, coins, time, timeStr);
        }else{
            if(time < r.getTime() || (time == r.getTime() && coins > r.getCoins())){
                updateR(stage, level, coins, time, timeStr);
            }
        }
    }

    public Ranking[] getRanking(int level){
        String query = "SELECT * FROM " + DBOpener.T_RANKING + " where " + DBOpener.T_R_LEVEL + "=? order by " + DBOpener.T_R_STAGE + " ASC";
        String[] where = {level+""};
        database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, where);
        if(cursor.getCount() <= 0){
            cursor.close();
            database.close();
            return null;
        }
        Ranking[] r = new Ranking[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        do{
            r[i] = Ranking.createRanking(cursor);
            i++;
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return r;
    }

    public int getMaxStage(){
        String query = "SELECT max(stage) FROM " + DBOpener.T_RANKING;
        database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            database.close();
            return 0;
        }
        cursor.moveToFirst();
        int max = cursor.getInt(0);
        cursor.close();
        database.close();
        return max;
    }


    private void deleteRanking(){
        database = db.getWritableDatabase();
        database.delete(DBOpener.T_RANKING, "1=1",null);
        database.close();
    }

}
