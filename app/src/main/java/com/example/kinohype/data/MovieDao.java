package com.example.kinohype.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    //query - запрос к базе
    @Query("SELECT * FROM moviesTable")
    LiveData<List<Movie>> getAllMovies();

    //метод получения фильма
    @Query("SELECT * FROM moviesTable WHERE id== :idMovie")
    Movie getMovieById(int idMovie);

    //метод вставки
    @Insert
    void insertMovieInto(Movie m);

    //метод удаления данных
    @Query("DELETE from moviesTable")
    void deleteAllMovies();

    //метод удаления 1 фильма
    @Delete
    void deleteMovie(Movie movie);
}
