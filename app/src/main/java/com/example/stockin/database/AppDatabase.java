package com.example.stockin.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.stockin.dao.ForumDao;
import com.example.stockin.model.Forum;

@Database(entities = {Forum.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract ForumDao forumDao();
    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "forum_db")

                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }
}
