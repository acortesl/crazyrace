package com.crazyrace.objetos;

import android.database.Cursor;

/**
 * Created by alex on 2/11/17.
 */

public class Ranking {

    private int stage;
    private int level;
    private int coins;
    private int time;
    private String timeStr;

    public Ranking(int stage, int level, int coins, int time, String timeStr){
        this.stage = stage;
        this.level = level;
        this.coins = coins;
        this.time = time;
        this.timeStr = timeStr;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public static Ranking createRanking(Cursor cursor){
        int s = cursor.getInt(0);
        int l = cursor.getInt(1);
        int c = cursor.getInt(2);
        int t = cursor.getInt(3);
        String ts = cursor.getString(4);
        Ranking r = new Ranking(s,l,c,t,ts);
        return r;
    }

    @Override
    public String toString(){
        return "["+stage+","+level+"]: " + coins + " " + time + " " + timeStr;
    }
}
