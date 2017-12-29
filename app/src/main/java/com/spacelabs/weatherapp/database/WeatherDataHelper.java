package com.spacelabs.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spacelabs.weatherapp.BuildConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gurpreet on 18-12-2017.
 */

@Getter
@Setter
public class WeatherDataHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = BuildConfig.DATABASE_NAME;

    // Contacts table name
    private final String TABLE_WEATHER = "weather";

    // Contacts Table Columns names
    private final String KEY_ID = "id";
    private final String KEY_DAY = "day";
    private final String KEY_TIMESTAMP = "timestamp";
    private final String KEY_WEATHER_DESCRIPTION = "weatherDescription";
    private final String KEY_LATITUDE = "latitude";
    private final String KEY_LONGITUDE = "longitude";
    private final String KEY_LOCALITY = "locality";
    private final String KEY_TEMPERATURE = "temperature";
    private final String KEY_WEATHER_ID = "weatherId";
    private final String KEY_WEATHER_ICON = "weatherIcon";
    private String[] mAllColumns = new String[]{KEY_ID, KEY_DAY, KEY_TIMESTAMP, KEY_WEATHER_DESCRIPTION,
            KEY_LATITUDE, KEY_LONGITUDE, KEY_LOCALITY, KEY_TEMPERATURE, KEY_WEATHER_ID,
            KEY_WEATHER_ICON};


    public WeatherDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_DAY + " TEXT,"
                + KEY_TIMESTAMP + " LONG," + KEY_WEATHER_DESCRIPTION + " TEXT," + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT," + KEY_LOCALITY + " TEXT," + KEY_TEMPERATURE + " TEXT,"
                + KEY_WEATHER_ID + " INTEGER," + KEY_WEATHER_ICON + " TEXT" + ")";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        // Create tables again
        onCreate(db);
    }


}