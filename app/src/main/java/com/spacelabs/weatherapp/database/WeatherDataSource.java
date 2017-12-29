package com.spacelabs.weatherapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.spacelabs.weatherapp.domain.WeatherData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gurpreet on 19-12-2017.
 */

public class WeatherDataSource {
    // Database fields
    private SQLiteDatabase mDatabase;
    private WeatherDataHelper mDbHelper;

    public WeatherDataSource(Context context) {
        mDbHelper = new WeatherDataHelper(context);
    }

    private void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    private void read() throws SQLException {
        mDatabase = mDbHelper.getReadableDatabase();
    }

    private void close() {
        mDbHelper.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new weather data
    public void insertWeatherData(WeatherData weatherData) {
        open();
        ContentValues values = new ContentValues();
        values.put(mDbHelper.getKEY_DAY(), weatherData.getDay());
        values.put(mDbHelper.getKEY_WEATHER_DESCRIPTION(), weatherData.getDescription());
        values.put(mDbHelper.getKEY_LATITUDE(), weatherData.getLatitude());
        values.put(mDbHelper.getKEY_LONGITUDE(), weatherData.getLongitude());
        values.put(mDbHelper.getKEY_LOCALITY(), weatherData.getLocality());
        values.put(mDbHelper.getKEY_TEMPERATURE(), weatherData.getTemperature());
        values.put(mDbHelper.getKEY_WEATHER_ID(), weatherData.getWeatherId());
        values.put(mDbHelper.getKEY_WEATHER_ICON(), weatherData.getWeatherIcon());

        // Inserting Row
        mDatabase.insert(mDbHelper.getTABLE_WEATHER(), null, values);
        close(); // Closing database connection
    }

    // Getting single weather data
    public WeatherData getWeatherData(int id) {
        read();
        Cursor cursor = mDatabase.query(mDbHelper.getTABLE_WEATHER(), mDbHelper.getMAllColumns(), mDbHelper.getKEY_ID() + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        WeatherData weatherData = null;
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            weatherData = new WeatherData(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), Integer.parseInt(cursor.getString(7)), cursor.getString(8));
            cursor.close();
        }

        return weatherData;
    }

    // Getting latest weather data
    public WeatherData getLatestWeatherData() {
        read();
        Cursor cursor = mDatabase.query(mDbHelper.getTABLE_WEATHER(), mDbHelper.getMAllColumns(), null,
                null, null, null, mDbHelper.getKEY_ID() + " desc", "1");
        WeatherData weatherData = null;
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            weatherData = new WeatherData(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), Integer.parseInt(cursor.getString(7)), cursor.getString(8));
            cursor.close();
        }

        return weatherData;
    }


    // Getting all weather data records
    public List<WeatherData> getAllWeatherData() {
        List<WeatherData> listWeatherData = new ArrayList<WeatherData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + mDbHelper.getTABLE_WEATHER() + " ORDER BY " + mDbHelper.getKEY_ID() + " desc";
        open();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                WeatherData weatherData = new WeatherData(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), Integer.parseInt(cursor.getString(7)), cursor.getString(8));
                // Adding weather to list
                listWeatherData.add(weatherData);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        // return weather list
        return listWeatherData;


    }

    // Updating single weather data
    public int updateWeatherData(WeatherData weatherData, int id) {
        open();
        ContentValues values = new ContentValues();
        values.put(mDbHelper.getKEY_DAY(), weatherData.getDay());
        values.put(mDbHelper.getKEY_WEATHER_DESCRIPTION(), weatherData.getDescription());
        values.put(mDbHelper.getKEY_LATITUDE(), weatherData.getLatitude());
        values.put(mDbHelper.getKEY_LONGITUDE(), weatherData.getLongitude());
        values.put(mDbHelper.getKEY_LOCALITY(), weatherData.getLocality());
        values.put(mDbHelper.getKEY_TEMPERATURE(), weatherData.getTemperature());
        values.put(mDbHelper.getKEY_WEATHER_ID(), weatherData.getWeatherId());
        values.put(mDbHelper.getKEY_WEATHER_ICON(), weatherData.getWeatherIcon());

        // updating row
        return mDatabase.update(mDbHelper.getTABLE_WEATHER(), values, mDbHelper.getKEY_ID() + " = ?", new String[]{String.valueOf(id)});

    }

    // Deleting single weather data
    public void deleteWeatherData(int id) {
        open();
        mDatabase.delete(mDbHelper.getTABLE_WEATHER(), mDbHelper.getKEY_ID() + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }


}
