package com.crazyrace.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 2/11/17.
 */

public class DBOpener extends SQLiteOpenHelper {


    public static final String DB_NAME = "CrazyRace.db";
    public static final String T_RANKING= "ranking";
    public static final String T_R_STAGE= "stage";
    public static final String T_R_LEVEL= "level";
    public static final String T_R_COINS= "coins";
    public static final String T_R_TIME= "time";
    public static final String T_R_TIMESTR= "timestr";

    private static final String CREATE_T_RANKING = "CREATE TABLE " + T_RANKING +
            " (" + T_R_STAGE + " INTEGER, " + T_R_LEVEL + " INTEGER, " + T_R_COINS + " INTEGER, " +
            T_R_TIME + " INTEGER, " + T_R_TIMESTR + " TEXT, PRIMARY KEY("+T_R_STAGE+","+T_R_LEVEL+"));";


    public DBOpener(Context context, int version){
        super(context, DB_NAME, null, version);
    }

    public DBOpener(Context context, String name, int version){
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys = 'ON'");
        database.execSQL(CREATE_T_RANKING);
    }

    private void doReset(SQLiteDatabase database){
        database.execSQL("DROP TABLE IF EXISTS " + T_RANKING);
        onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        doReset(db);
    }

}
