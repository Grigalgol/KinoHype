package com.example.kinohype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.kinohype.data.LaterMovie;
import com.example.kinohype.data.LoveMovie;
import com.example.kinohype.data.MViewModel;
import com.example.kinohype.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class LaterFilmActivity extends AppCompatActivity {

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
            case R.id.later:
                Intent intent2 = new Intent(this, LaterFilmActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView recyclerViewLater;
    private AdapterForMovie adapter;
    private MViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_later_film);
        recyclerViewLater = findViewById(R.id.recyclerViewLater);
        recyclerViewLater.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new AdapterForMovie();
        recyclerViewLater.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(MViewModel.class);
        LiveData<List<LaterMovie>> laterMovies = viewModel.getLatermovies();
        laterMovies.observe(this, new Observer<List<LaterMovie>>() {


            @Override
            public void onChanged(List<LaterMovie> laterMovies) {
                List<Movie> movies = new ArrayList<>();
                if (laterMovies != null) {
                    movies.addAll(laterMovies);
                    //так как мы не можем в лист родительского класса вставить объект дочернего класса, делаем то, что выше
                    adapter.setM(movies);
                }
            }
        });

        adapter.setOnImageMovieClickListener(new AdapterForMovie.OnImageMovieClickListener() {
            @Override
            public void omPosterClick(int position) {
                //переопределяем метод
                Movie movie = adapter.getMovies().get(position);
                //создаем интент и отправляем в новую активность наш фильмец
                Intent intentGoToFilm = new Intent(LaterFilmActivity.this, AboutFilmActivity.class);
                intentGoToFilm.putExtra("id", movie.getId());
                startActivity(intentGoToFilm);
            }
        });
    }
}