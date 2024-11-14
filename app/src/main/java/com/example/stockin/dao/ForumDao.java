package com.example.stockin.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.stockin.model.Forum;

import java.util.List;
@Dao
public interface ForumDao {
    @Insert
    void insertOne(Forum forum);
    @Update
    void updateOne(Forum forum);
    @Delete
    void delete(Forum forum);
    @Query("SELECT * FROM forum_table")
    List<Forum> getAll();
}
