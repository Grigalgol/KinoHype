package com.example.kinohype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kinohype.data.Movie;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonPop;
    private Button buttonTop;
    private Button buttonBes;
    private AdapterForMovie adapterForMovie;
    private RecyclerView recyclerViewImages;

    int chooseTypeOfMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //кнопки для переключения подборки кино
        buttonTop = findViewById(R.id.buttonTop);
        buttonBes = findViewById(R.id.buttonBes);
        buttonPop = findViewById(R.id.buttonPop);
        //по умолчанию
        chooseTypeOfMovies = Internet.POP;
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
                JSONObject jsonObject = Internet.getJSONObjectfromInternet(Internet.POP, 1);
                //получаем список фильмов
                ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
                adapterForMovie.setM(movies);
            }
        });
        buttonBes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = Internet.getJSONObjectfromInternet(Internet.BES, 1);
                //получаем список фильмов
                ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
                adapterForMovie.setM(movies);
            }
        });
        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = Internet.getJSONObjectfromInternet(Internet.TOP, 1);
                //получаем список фильмов
                ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
                adapterForMovie.setM(movies);
            }
        });
    }
}