package com.example.himanshuaggarwal.logsens;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by himanshuaggarwal on 17/04/18.
 */

public class Database_A3_MT17015 extends SQLiteOpenHelper {
    public static final String databaseName = "sensorData.db";
    public static final String tableName = "sensorReading";
    public static final String col1 = "ID";
    public static final String col2 = "Time_Stamp";
    public static final String col3 = "ACC_X";
    public static final String col4 = "ACC_Y";
    public static final String col5 = "ACC_Z";
    public static final String col6 = "GYR_X";
    public static final String col7 = "GYR_Y";
    public static final String col8 = "GYR_Z";
    public static final String col9 = "GPS_LATITUDE";
    public static final String col10 = "GPS_LONGITUDE";
    public static final String col11 = "CELL_ID";
    public static final String col12 = "LAC";
    public static final String col13 = "MCC";
    public static final String col14 = "MNC";
    public static final String col15 = "WIFI_ACCESS_POINTS";
    public static final String col16 = "dB_VALUES";



    public Database_A3_MT17015(Context context) {
        super(context, databaseName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + tableName + "(ID INTEGER PRIMARY KEY, TIME_STAMP TEXT, ACC_X TEXT, ACC_Y TEXT, ACC_Z TEXT, GYR_X TEXT, GYR_Y TEXT, GYR_Z TEXT, GPS_LATITUDE TEXT, GPS_LONGITUDE TEXT, CELL_ID TEXT, LAC TEXT, MCC TEXT, MNC TEXT, WIFI_ACCESS_POINTS TEXT, DB_VALUES TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+tableName);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String TIME_STAMP, String ACCX, String ACCY, String ACCZ, String GYRX, String GYRY, String GYRZ,
                             String GPSLAT, String GPSLON, String CELL_ID, String LAC, String MCC, String MNC, String WIFI_ACCESS_POINTS , String DB_VALUES) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(col2, TIME_STAMP);
        contentValues.put(col3, ACCX);
        contentValues.put(col4, ACCY);
        contentValues.put(col5, ACCZ);
        contentValues.put(col6, GYRX);
        contentValues.put(col7, GYRY);
        contentValues.put(col8, GYRZ);
        contentValues.put(col9, GPSLAT);
        contentValues.put(col10, GPSLON);
        contentValues.put(col11, CELL_ID);
        contentValues.put(col12, LAC);
        contentValues.put(col13, MCC);
        contentValues.put(col14, MNC);
        contentValues.put(col15, WIFI_ACCESS_POINTS);
        contentValues.put(col16, DB_VALUES);

        long res = database.insert(tableName, null, contentValues);

        if(res==-1)
            return false;

        return true;
    }

    public boolean saveToCSV(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from "+tableName, null);
        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(outputFile, "");
        int time = 0;

        if (!dir.exists()){
            dir.mkdir();
        }

        time++;

        File saveFile = new File(dir, "/logsensLogFile.csv");
        int row = cursor.getCount();
        time++;
        int col = cursor.getColumnCount();
        String content = "";
        if (row>0){
            cursor.moveToFirst();
            for (int i = 0; i < col-1; i++) {
                content +=cursor.getColumnName(i)+",";
            }
            content +=cursor.getColumnName(col-1);

            content+="\n";

            for (int i = 0; i < row; i++) {
                cursor.moveToPosition(i);
                for (int j = 0; j < col-1; j++) {
                    content +=cursor.getString(j)+",";
                }
                content +=cursor.getString(col-1);
                content+="\n";
            }

            try{
                FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
