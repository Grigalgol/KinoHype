package com.example.kinohype.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, LoveMovie.class, LaterMovie.class}, version = 6, exportSchema = false)
public abstract class KinoDataBase extends RoomDatabase {

    private static String nameDB = "movies.db";
    private static KinoDataBase dataBase;
    //чтобы не было проблем из-за доступа с разных потоков добавим блок синхронизации
    private static final Object L = new Object();
    public static  KinoDataBase getInstance(Context context) {
        synchronized (L) {
            if (dataBase == null) {
                //создаем базу данных
                dataBase = Room.databaseBuilder(context, KinoDataBase.class, nameDB).fallbackToDestructiveMigration().build();
            }
        }
        return dataBase;
    }

    public abstract MovieDao movieDao();
}
