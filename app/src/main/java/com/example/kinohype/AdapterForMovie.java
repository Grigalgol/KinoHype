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
import java.util.List;

public class AdapterForMovie extends RecyclerView.Adapter<AdapterForMovie.MovieViewHolder> {

    //массив фильмов
    private List<Movie> m;
    private OnImageMovieClickListener onImageMovieClickListener;
    private OnSetInTheEnd onSetInTheEnd;

    //пустой конструктор, в котором присваиваем значение
    public AdapterForMovie() {
        m = new ArrayList<>();
    }

    //слушатель на клик картинки
    interface OnImageMovieClickListener {
        void omPosterClick(int position);
    }

    //когда мы достигаем конца, мы подгружаем новые данные
    interface OnSetInTheEnd {
        void onSetEnd();
    }

    public void setOnImageMovieClickListener(OnImageMovieClickListener onImageMovieClickListener) {
        this.onImageMovieClickListener = onImageMovieClickListener;
    }

    public void setOnSetInTheEnd(OnSetInTheEnd onSetInTheEnd) {
        this.onSetInTheEnd = onSetInTheEnd;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        //если достигли конца списка и слушатель onSetInTheEnd не равен 0
        if (m.size()>=20 &&  i > m.size()-6 && onSetInTheEnd != null) {
            onSetInTheEnd.onSetEnd();
        }
        //метод, который устанавливает в imageView картинку фильма из posterPath, спасибо, что объяснили пикассо
        Movie movie = m.get(i);
        //изменить качество картинки можно тут (себе на будущее)
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImageMovieClickListener != null) {
                        //передаем позицию адаптера
                        onImageMovieClickListener.omPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    public void clearM() {
        this.m.clear();
        notifyDataSetChanged();
    }

    //сеттер для установки нового массива
    public void setM(List<Movie> movies) {
        m = movies;
        //оповещаем адаптер
        notifyDataSetChanged();
    }
    //метод для добавления новых фильмов, не удаляя старые
    public void addM(List<Movie> movies) {
        this.m.addAll(movies);
        //оповещаем адаптер
        notifyDataSetChanged();
    }
    //геттер, может понадобится в будущем
    public List<Movie> getMovies() {
        return m;
    }
}