package com.example.kinohype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonPop;
    private Button buttonTop;
    private Button buttonBes;
    private AdapterForMovie adapterForMovie;
    private RecyclerView recyclerViewImages;

    private ViewModel viewModel;

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
        JSONObject jsonObject = Internet.getJSONObjectfromInternet(chooseTypeOfMovies, 1);
        //получаем список фильмов
        ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
        adapterForMovie.setM(movies);
        buttonPop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.chooseMovieType));
                buttonTop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonBes.setTextColor(getResources().getColor(R.color.defaultColor));
                downloadData(Internet.POP, 1);
            }
        });

        buttonBes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonTop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonBes.setTextColor(getResources().getColor(R.color.chooseMovieType));
                downloadData(Internet.BES, 1);
            }
        });

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //меняем цвет текста на кнопках
                buttonPop.setTextColor(getResources().getColor(R.color.defaultColor));
                buttonTop.setTextColor(getResources().getColor(R.color.chooseMovieType));
                buttonBes.setTextColor(getResources().getColor(R.color.defaultColor));
                downloadData(Internet.TOP, 1);
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

            }
        });

        LiveData<List<Movie>> moviesLiveData = viewModel.getMovies();
        moviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapterForMovie.setM(movies);
            }
        });
    }
    private void downloadData(int method, int page) {
        JSONObject jsonObject = Internet.getJSONObjectfromInternet(method, page);
        //получаем список фильмов
        ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
        if(movies != null && !movies.isEmpty()) {
            //очищаем старые данные
            viewModel.deleteAllMovies();
            //вставляем новые данные в цикле
            for(Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
        }
    }
}