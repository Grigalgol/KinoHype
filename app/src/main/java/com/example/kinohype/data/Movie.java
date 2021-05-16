package com.example.kinohype.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "moviesTable")
public class Movie {
    @PrimaryKey
    //значения (теги)
    private int id;
    private String title;
    private String original_title;
    private int voteCount;
    private String overview;
    private String backdropPath;
    private String bigPosterPath;
    private String posterPath;
    private double voteAverage;
    private String dataOfRelease;

    public Movie(int id, String title, String original_title, int voteCount, String overview, String backdropPath, String bigPosterPath, String posterPath, double voteAverage, String dataOfRelease) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.voteCount = voteCount;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.bigPosterPath = bigPosterPath;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.dataOfRelease = dataOfRelease;
    }
//геттеры
    public int getId() {
        return id;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getDataOfRelease() {
        return dataOfRelease;
    }
    //сеттеры



    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setDataOfRelease(String dataOfRelease) {
        this.dataOfRelease = dataOfRelease;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }
}
