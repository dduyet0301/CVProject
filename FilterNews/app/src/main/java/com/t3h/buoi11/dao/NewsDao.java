package com.t3h.buoi11.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.t3h.buoi11.model.News;
import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM News")
    List<News> getNews();

    @Query("SELECT * FROM News WHERE isFavor = :isFavor")
    List<News> getFavor(boolean isFavor);

    @Insert
    void insert(News... news);

    @Update
    void update(News... news);

    @Delete
    void delete(News... news);
}
