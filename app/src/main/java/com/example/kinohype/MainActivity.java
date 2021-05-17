package com.example.kinohype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kinohype.data.Movie;
import com.example.kinohype.data.ViewModel;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    //язык приложения

    private static String lang;


    private Button buttonPop;
    private Button buttonTop;
    private Button buttonBes;
    private AdapterForMovie adapterForMovie;
    private RecyclerView recyclerViewImages;
    //номер страницы
    private static int page = 1;
    //метод сортировки
    private static int mSort;
    private static boolean isLoading = false;

    private ViewModel viewModel;
    //идентификатор загрузчика
    private static final int loaderid = 932;
    private LoaderManager loaderManager;

    //переопределяем метод с меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //чтобы реагировать на нажатию в менб переопределяем метод
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.love:
                Intent intent1 = new Intent(this, LoveFilmActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    int chooseTypeOfMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lang = Locale.getDefault().getLanguage();
        //отвечает за загрузки в приложении
        loaderManager = LoaderManager.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        //кнопки для переключения подборки кино
        buttonTop = findViewById(R.id.buttonTop);
        buttonBes = findViewById(R.id.buttonBes);
        buttonPop = findViewById(R.id.buttonPop);
        //по умолчанию
        chooseTypeOfMovies = Internet.POP;
        buttonPop.setTextColor(getResources().getColor(R.color.chooseMovieType));

        recyclerViewImages = findViewById(R.id.receclerViewImages);
        adapterForMovie = new AdapterForMovie();
        //для показа фильмов сетками
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 2));
        //устанавливаем адаптер для recyclerview
        recyclerViewImages.setAdapter(adapterForMovie);
        JSONObject jsonObject = Internet.getJSONObjectfromInternet(chooseTypeOfMovies, 1, lang);
        //получаем список фильмов
        ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
        adapterForMovie.setM(movies);
        buttonPop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                page = 1;
                mSort = Internet.POP;
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.chooseMovieType));
                buttonTop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonBes.setTextColor(getResources().getColor(R.color.defaultColor));
                downloadData(Internet.POP, page);
            }
        });

        buttonBes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                mSort = Internet.BES;
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonTop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonBes.setTextColor(getResources().getColor(R.color.chooseMovieType));
                downloadData(Internet.BES, page);
            }
        });

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                mSort = Internet.TOP;
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonTop.setTextColor(getResources().getColor(R.color.chooseMovieType));
                buttonBes.setTextColor(getResources().getColor(R.color.defaultColor));
                downloadData(Internet.TOP, page);
            }
        });
        adapterForMovie.setOnImageMovieClickListener(new AdapterForMovie.OnImageMovieClickListener() {
            @Override
            public void omPosterClick(int position) {
                //переопределяем метод
                Movie movie = adapterForMovie.getMovies().get(position);
                //создаем интент и отправляем в новую активность наш фильмец
                Intent intentGoToFilm = new Intent(MainActivity.this, AboutFilmActivity.class);
                intentGoToFilm.putExtra("id", movie.getId());
                startActivity(intentGoToFilm);
            }
        });
        adapterForMovie.setOnSetInTheEnd(new AdapterForMovie.OnSetInTheEnd() {

            @Override
            public void onSetEnd() {
                //переопределяем метод
                if(!isLoading) {
                    downloadData(mSort, page);
                }

            }
        });

        LiveData<List<Movie>> moviesLiveData = viewModel.getMovies();
        moviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(page == 1) adapterForMovie.setM(movies);

            }
        });
    }
    private void downloadData(int method, int page) {
       URL url = Internet.buildURL(method, page, lang);
       Bundle bundle = new Bundle();
       bundle.putString("url", url.toString());
       loaderManager.restartLoader(loaderid, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        Internet.JSONloader jsoNloader = new Internet.JSONloader(this, args);
        jsoNloader.setOnStartLoadingListener(new Internet.JSONloader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
            }
        });
        return jsoNloader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONformat.getMovieJSON(data);
        if(movies != null && !movies.isEmpty()) {
            //очищаем старые данные
            if(page == 1) {
                viewModel.deleteAllMovies();
                adapterForMovie.clearM();
            }
            //вставляем новые данные в цикле
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            adapterForMovie.addM(movies);
            page++;
        }
        isLoading = false;
        loaderManager.destroyLoader(loaderid);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}