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
    private static KinoDataBase dataBase;
    public ViewModel(@NonNull Application application) {
        super(application);
        dataBase = KinoDataBase.getInstance(getApplication());
        movies = dataBase.movieDao().getAllMovies();
    }
    //геттер на лист
    public LiveData<List<Movie>> getMovies() {
        return movies;
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

    //метод удаления
    public void deleteAllMovies() {
        new DeleteMTask().execute();
    }

    //метод вставки данных
    public void insertMovie(Movie m) {
        new InsertMTask().execute(m);
    }

    //метод удаления 1 элемента из базы
    //метод вставки данных
    public void deleteMovie(Movie m) {
        new DeleteOneMTask().execute(m);
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
}
