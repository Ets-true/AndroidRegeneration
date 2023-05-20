package com.example.regenerationoil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {JobEntity.class, User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JobDao jobDao();

    public abstract UserDao userDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "regeneration_database")
                    .build();
        }
        return instance;
    }
}