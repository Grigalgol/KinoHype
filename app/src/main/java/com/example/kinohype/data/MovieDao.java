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

    @Query("SELECT * FROM loveMoviesTable")
    LiveData<List<LoveMovie>> getAllLoveMovies();

    @Query("SELECT * FROM laterMoviesTable")
    LiveData<List<LaterMovie>> getAllLaterMovies();

    //метод получения фильма
    @Query("SELECT * FROM moviesTable WHERE id== :idMovie")
    Movie getMovieById(int idMovie);

    //метод получения фильма
    @Query("SELECT * FROM loveMoviesTable WHERE id== :idMovie")
    LoveMovie getLoveMovieById(int idMovie);

    //метод получения фильма
    @Query("SELECT * FROM laterMoviesTable WHERE id== :idMovie")
    LaterMovie getLaterMovieById(int idMovie);


    //метод вставки
    @Insert
    void insertMovieInto(Movie m);

    @Insert
    void insertLoveInto(LoveMovie m);

    @Insert
    void insertLaterInto(LaterMovie m);

    //метод удаления данных
    @Query("DELETE from moviesTable")
    void deleteAllMovies();

    //метод удаления 1 фильма
    @Delete
    void deleteMovie(Movie movie);

    @Delete
    void deleteLoveMovie(LoveMovie movie);

    @Delete
    void deleteLaterMovie(LaterMovie movie);
}
