package com.example.kinohype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinohype.data.LaterMovie;
import com.example.kinohype.data.LoveMovie;
import com.example.kinohype.data.Movie;
import com.example.kinohype.data.Review;
import com.example.kinohype.data.Trailer;
import com.example.kinohype.data.MViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AboutFilmActivity extends AppCompatActivity {

    //язык приложения
    private static String lang;

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

    private int filmId;

    private ImageView imageViewLove;
    private ImageView imageViewPoster;
    private ImageView imageViewLater;
    private TextView textViewTitle;
    private TextView textViewOriginalTitleplusDataOfRelease;
    private TextView textViewOwerview;
    private TextView textViewRaitingValue;
    private MViewModel MViewModel;
    private Movie movie;
    private LoveMovie loveMovie;
    private LaterMovie laterMovie;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_film);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lang = Locale.getDefault().getLanguage();
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewLove = findViewById(R.id.imageView3);
        imageViewLater = findViewById(R.id.imageViewlater);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitleplusDataOfRelease = findViewById(R.id.textViewOriginalTitle);
        textViewOwerview = findViewById(R.id.textViewOwerview);
        textViewRaitingValue = findViewById(R.id.textViewValueRaiting);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            filmId = intent.getIntExtra("id", -10);
        } else {
            //закрываем активность
            finish();
        }
        //получаем фильм
        MViewModel = ViewModelProviders.of(this).get(MViewModel.class);
        movie = MViewModel.getMovieById(filmId);
        //с помощью пикассо устанавливаем картинку фильма (постер)
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewPoster);
        //устанавливаем данные из класса Movie
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitleplusDataOfRelease.setText(movie.getOriginal_title() + " " + movie.getDataOfRelease());
        textViewOwerview.setText(movie.getOverview());
        double raiting = movie.getVoteAverage();
        textViewRaitingValue.setText(Double.toString(raiting));
        //в зависимости от рейтинга меняем его цвет
        if (raiting < 2.5)
            textViewRaitingValue.setTextColor(getResources().getColor(R.color.color0_2_5));
        if (raiting >= 2.5 && raiting < 6)
            textViewRaitingValue.setTextColor(getResources().getColor(R.color.color2_5_6));
        if (raiting >= 6 && raiting < 8)
            textViewRaitingValue.setTextColor(getResources().getColor(R.color.color6_8));
        if (raiting >= 8 && raiting < 9)
            textViewRaitingValue.setTextColor(getResources().getColor(R.color.color8_9));
        if (raiting >= 9)
            textViewRaitingValue.setTextColor(getResources().getColor(R.color.color9_10));
        setLove();
        setLater();
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnTraileClickListener(new TrailerAdapter.OnTraileClickListener() {
            @Override
            public void onTrailerClick(String url) {
                //неявный интент
                Intent intentTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentTrailer);
            }
        });
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        //меняем заголовок экшн бара на название кино
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(movie.getTitle());
        JSONObject jsonObjectTrailers = Internet.getJSONTrailerfromInternet(movie.getId(), lang);
        JSONObject jsonObjectReviews = Internet.getJSONReviewsfromInternet(movie.getId(), lang);
        //получаем из объектов json трейлеры и отзывы
        ArrayList<Trailer> trailers = JSONformat.getTrailersJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JSONformat.getReviewsJSON(jsonObjectReviews);
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);
    }

    //метод добавления в любимые фильмы путем нажатия на сердечко
    public void onClickFavorite(View view) {
        //проверка на наличие фильма
        if (loveMovie == null) {
            //пришлось создать конструктор, так как родительский класс нельзя передавать
            MViewModel.insertLoveMovie(new LoveMovie(movie));
            Toast.makeText(this, getResources().getString(R.string.addSuccessfully), Toast.LENGTH_SHORT).show();
        } else {
            MViewModel.deleteLoveMovie(loveMovie);
            Toast.makeText(this, getResources().getString(R.string.addUnsuccessfully), Toast.LENGTH_SHORT).show();
        }
        setLove();
    }

    private void setLove() {
        loveMovie = MViewModel.getLoveMovieById(filmId);
        if (loveMovie == null) imageViewLove.setImageResource(R.drawable.hearttouch);
        else imageViewLove.setImageResource(R.drawable.heartnotouch);
    }

    public void onClickLater(View view) {
        if (laterMovie == null) {
            //пришлось создать конструктор, так как родительский класс нельзя передавать
            MViewModel.insertLaterMovie(new LaterMovie(movie));
            Toast.makeText(this, getResources().getString(R.string.addLater), Toast.LENGTH_SHORT).show();
        } else {
            MViewModel.deleteLaterMovie(laterMovie);
            Toast.makeText(this, getResources().getString(R.string.deleteLater), Toast.LENGTH_SHORT).show();
        }
        setLater();
    }

    private void setLater() {
        laterMovie = MViewModel.getLaterMovieById(filmId);
        if (laterMovie == null) imageViewLater.setImageResource(R.drawable.addlater);
        else imageViewLater.setImageResource(R.drawable.deletelater);
    }

    public void onClickShare(View view) {
        String msg = getString(R.string.Hi_i) + movie.getTitle() + getString(R.string.advice_watching);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        Intent chosenIntent = Intent.createChooser(intent, getString(R.string.How_do_you_want));
        startActivity(chosenIntent);
    }
}