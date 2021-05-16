package com.example.kinohype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinohype.data.LoveMovie;
import com.example.kinohype.data.Movie;
import com.example.kinohype.data.ViewModel;
import com.squareup.picasso.Picasso;

public class AboutFilmActivity extends AppCompatActivity {

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

    private int filmId;

    private ImageView imageViewLove;
    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitleplusDataOfRelease;
    private TextView textViewOwerview;
    private TextView textViewRaitingValue;
    private ViewModel viewModel;
    private Movie movie;
    private LoveMovie loveMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_film);
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewLove = findViewById(R.id.imageView3);
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
        movie = viewModel.getMovieById(filmId);
        //с помощью пикассо устанавливаем картинку фильма (постер)
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewPoster);
        //устанавливаем данные из класса Movie
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitleplusDataOfRelease.setText(movie.getOriginal_title() + " " + movie.getDataOfRelease());
        textViewOwerview.setText(movie.getOverview());
        double raiting = movie.getVoteAverage();
        textViewRaitingValue.setText(Double.toString(raiting));
        //в зависимости от рейтинга меняем его цвет
        if(raiting<2.5) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color0_2_5));
        if(raiting>=2.5 && raiting<6) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color2_5_6));
        if(raiting>=6 && raiting<8) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color6_8));
        if(raiting>=8 && raiting<9) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color8_9));
        if(raiting>=9) textViewRaitingValue.setTextColor(getResources().getColor(R.color.color9_10));
        setLove();
        //меняем заголовок экшн бара на название кино
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(movie.getTitle());
    }
    //метод добавления в любимые фильмы путем нажатия на сердечко
    public void onClickFavorite(View view) {
        //проверка на наличие фильма
        if(loveMovie == null) {
            //пришлось создать конструктор, так как родительский класс нельзя передавать
            viewModel.insertLoveMovie(new LoveMovie(movie));
            Toast.makeText(this, "Фильм успешно добавлен в раздел Избранное", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteLoveMovie(loveMovie);
            Toast.makeText(this, "Фильм успешно исключен из раздела Избранное", Toast.LENGTH_SHORT).show();
        }
        setLove();
    }
    private void setLove() {
        loveMovie = viewModel.getLoveMovieById(filmId);
        if (loveMovie == null) imageViewLove.setImageResource(R.drawable.hearttouch);
        else imageViewLove.setImageResource(R.drawable.heartnotouch);
    }
}