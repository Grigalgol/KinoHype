package com.example.kinohype.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "loveMoviesTable")
public class LoveMovie extends Movie{
    public LoveMovie(int uniqId,  int id, String title, String original_title, int voteCount, String overview, String backdropPath, String bigPosterPath, String posterPath, double voteAverage, String dataOfRelease) {
        super(uniqId, id, title, original_title, voteCount, overview, backdropPath, bigPosterPath, posterPath, voteAverage, dataOfRelease);
    }

    @Ignore
    public LoveMovie(Movie movie) {
        super(movie.getUniqId(), movie.getId(), movie.getTitle(), movie.getOriginal_title(), movie.getVoteCount(), movie.getOverview(), movie.getBackdropPath(), movie.getBigPosterPath(), movie.getPosterPath(), movie.getVoteAverage(), movie.getDataOfRelease());
    }
}
