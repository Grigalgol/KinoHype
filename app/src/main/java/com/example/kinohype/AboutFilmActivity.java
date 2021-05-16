package com.example.kinohype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kinohype.data.Movie;
import com.example.kinohype.data.ViewModel;
import com.squareup.picasso.Picasso;

public class AboutFilmActivity extends AppCompatActivity {

    private int filmId;

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitleplusDataOfRelease;
    private TextView textViewOwerview;
    private TextView textViewRaitingValue;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_film);
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitleplusDataOfRelease = findViewById(R.id.textViewOriginalTitle);
        textViewOwerview = findViewById(R.id.textViewOwerview);
        textViewRaitingValue = findViewById(R.id.textViewValueRaiting);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("id")) {
            filmId = intent.getIntExtra("id", -10);
        } else {
            //закрываем активность
            finish();
        }
        //получаем фильм
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        Movie movie = viewModel.getMovieById(filmId);
        //с помощью пикассо устанавливаем картинку фильма (постер)
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewPoster);
        //устанавливаем данные из класса Movie
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitleplusDataOfRelease.setText(movie.getOriginal_title() + " " + movie.getDataOfRelease());
        textViewOwerview.setText(movie.getOverview());
        double raiting = movie.getVoteAverage();
        textViewRaitingValue.setText(Double.toString(raiting));
        if(raiting<2.5) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color0_2_5));
        if(raiting>=2.5 && raiting<6) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color2_5_6));
        if(raiting>=6 && raiting<8) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color6_8));
        if(raiting>=8 && raiting<9) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color8_9));
        if(raiting>=9) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color9_10));
    }
}