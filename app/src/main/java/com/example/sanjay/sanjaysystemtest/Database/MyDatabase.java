package com.example.sanjay.sanjaysystemtest.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sanjay.sanjaysystemtest.Database.Dao.UserDao;
import com.example.sanjay.sanjaysystemtest.Database.Model.User;


@Database(entities = {User.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static final String DB_NAME = "BacancyDatabase.db";
    private static volatile MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MyDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                MyDatabase.class,
                DB_NAME).build();
    }

    public abstract UserDao getUserDao();

}
