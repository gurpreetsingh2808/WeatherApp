package com.spacelabs.weatherapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spacelabs.weatherapp.BuildConfig;
import com.spacelabs.weatherapp.domain.WeatherData;

/**
 * Created by Gurpreet on 18-12-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = BuildConfig.DATABASE_NAME;

    // Contacts table name
    private static final String TABLE_WEATHER = "weather";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WEATHER_DESCRIPTION = "weatherDescription";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LOCALITY = "locality";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_WEATHER_ID = "weatherId";
    private static final String KEY_WEATHER_ICON = "weatherIcon";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WEATHER_DESCRIPTION + " TEXT," + KEY_LATITUDE + " TEXT,"
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

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new weather data
    public void insertWeatherData(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_WEATHER_DESCRIPTION, weatherData.getDescription());
        values.put(KEY_LATITUDE, weatherData.getLatitude());
        values.put(KEY_LONGITUDE, weatherData.getLongitude());
        values.put(KEY_LOCALITY, weatherData.getLocality());
        values.put(KEY_TEMPERATURE, weatherData.getTemperature());
        values.put(KEY_WEATHER_ID, weatherData.getWeatherId());
        values.put(KEY_WEATHER_ICON, weatherData.getWeatherIcon());

        // Inserting Row
        db.insert(TABLE_WEATHER, null, values);
        db.close(); // Closing database connection
    }

    // Getting single weather data
    public WeatherData getWeatherData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEATHER, new String[]{KEY_ID, KEY_WEATHER_DESCRIPTION,
                        KEY_LATITUDE, KEY_LONGITUDE, KEY_LOCALITY, KEY_TEMPERATURE, KEY_WEATHER_ID,
                        KEY_WEATHER_ICON}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        WeatherData weatherData = null;
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            weatherData = new WeatherData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), Integer.parseInt(cursor.getString(6)), cursor.getString(7));
            cursor.close();
        }

        return weatherData;
    }

    // Updating single weather data
    public int updateWeatherData(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_WEATHER_DESCRIPTION, weatherData.getDescription());
        values.put(KEY_LATITUDE, weatherData.getLatitude());
        values.put(KEY_LONGITUDE, weatherData.getLongitude());
        values.put(KEY_LOCALITY, weatherData.getLocality());
        values.put(KEY_TEMPERATURE, weatherData.getTemperature());
        values.put(KEY_WEATHER_ID, weatherData.getWeatherId());
        values.put(KEY_WEATHER_ICON, weatherData.getWeatherIcon());

        // updating row
        return db.update(TABLE_WEATHER, values, KEY_ID + " = ?", new String[]{String.valueOf(weatherData.getId())});

    }

    // Deleting single weather data
    public void deleteWeatherData(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER, KEY_ID + " = ?",
                new String[]{String.valueOf(weatherData.getId())});
        db.close();
    }

}