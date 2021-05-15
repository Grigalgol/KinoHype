package com.example.kinohype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.kinohype.data.Movie;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AdapterForMovie adapterForMovie;
    private RecyclerView recyclerViewImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewImages = findViewById(R.id.receclerViewImages);
        adapterForMovie = new AdapterForMovie();
        //для показа фильмов сетками
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 2));
        JSONObject jsonObject = Internet.getJSONObjectfromInternet(Internet.POP, 1);
        //получаем список фильмов
        ArrayList<Movie> movies = JSONformat.getMovieJSON(jsonObject);
        adapterForMovie.setM(movies);
        //устанавливаем адаптер для recyclerview
        recyclerViewImages.setAdapter(adapterForMovie);
    }
}