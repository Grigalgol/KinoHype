package com.example.kinohype;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinohype.data.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.ArrayList;

public class AdapterForMovie extends RecyclerView.Adapter<AdapterForMovie.MovieViewHolder> {

    //массив фильмов
    private ArrayList<Movie> m;

    //пустой конструктор, в котором присваиваем значение
    public AdapterForMovie() {
        m = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        //метод, который устанавливает в imageView картинку фильма из posterPath, спасибо, что объяснили пикассо
        Movie movie = m.get(i);
        Picasso.get().load(movie.getPosterPath()).into(movieViewHolder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return m.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.iamgeViewMovie);
        }
    }
    //сеттер для установки нового массива
    public void setM(ArrayList<Movie> movies) {
        m = movies;
        //оповещаем адаптер
        notifyDataSetChanged();
    }
    //метод для добавления новых фильмов, не удаляя старые
    public void addM(ArrayList<Movie> movies) {
        this.m.addAll(movies);
        //оповещаем адаптер
        notifyDataSetChanged();
    }
    //геттер, может понадобится в будущем
    public ArrayList<Movie> getMovies() {
        return m;
    }
}