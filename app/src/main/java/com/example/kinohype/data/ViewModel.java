package com.example.kinohype.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class ViewModel extends AndroidViewModel {
    //список фильмов
    private LiveData<List<Movie>> movies;
    private LiveData<List<LoveMovie>> lovemovies;
    private static KinoDataBase dataBase;
    public ViewModel(@NonNull Application application) {
        super(application);
        dataBase = KinoDataBase.getInstance(getApplication());
        movies = dataBase.movieDao().getAllMovies();
        lovemovies = dataBase.movieDao().getAllLoveMovies();
    }
    //геттер на лист
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<LoveMovie>> getLovemovies() {
        return lovemovies;
    }

    public Movie getMovieById(int id) {
        try {
            return new GetMTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoveMovie getLoveMovieById(int id) {
        try {
            return new GetLMTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //метод удаления
    public void deleteAllMovies() {
        new DeleteMTask().execute();
    }

    //метод вставки данных
    public void insertMovie(Movie m) {
        new InsertMTask().execute(m);
    }

    //метод удаления 1 элемента из базы
    public void deleteMovie(Movie m) {
        new DeleteOneMTask().execute(m);
    }

    //метод вставки данных
    public void insertLoveMovie(LoveMovie m) {
        new InsertLMTask().execute(m);
    }

    //метод удаления 1 элемента из базы
    public void deleteLoveMovie(LoveMovie m) {
        new DeleteOneLMTask().execute(m);
    }

    private static class DeleteOneLMTask extends AsyncTask<LoveMovie, Void, Void> {

        @Override
        protected Void doInBackground(LoveMovie... movies) {
            if(movies != null && movies.length > 0) dataBase.movieDao().deleteLoveMovie(movies[0]);
            return null;
        }
    }

    private static class InsertLMTask extends AsyncTask<LoveMovie, Void, Void> {

        @Override
        protected Void doInBackground(LoveMovie... movies) {
            if(movies != null && movies.length > 0) dataBase.movieDao().insertLoveInto(movies[0]);
            return null;
        }
    }


    private static class DeleteOneMTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0) dataBase.movieDao().deleteMovie(movies[0]);
            return null;
        }
    }

    private static class InsertMTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0) dataBase.movieDao().insertMovieInto(movies[0]);
            return null;
        }
    }

    private static class DeleteMTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
            dataBase.movieDao().deleteAllMovies();
            return null;
        }
    }

    private static class GetMTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0) return dataBase.movieDao().getMovieById(integers[0]);
            return null;
        }

    }

    private static class GetLMTask extends AsyncTask<Integer, Void, LoveMovie> {

        @Override
        protected LoveMovie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0) return dataBase.movieDao().getLoveMovieById(integers[0]);
            return null;
        }

    }
}
