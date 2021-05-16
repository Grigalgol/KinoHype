package com.example.kinohype;

import com.example.kinohype.data.Movie;
import com.example.kinohype.data.Review;
import com.example.kinohype.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONformat {
    //базовый url для картинки фильма, размеры картинок
    private static String baseImageUrl = "https://image.tmdb.org/t/p/";
    private static String small_poster_size = "w185";
    private static String big_poster_size = "w780";

    private static String keyname = "name";

    public static ArrayList<Review> getReviewsJSON (JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null) return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            //в цикле получаем отзывы
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObjectReview = jsonArray.getJSONObject(i);
                String author = jsonObjectReview.getString("author");
                String content = jsonObjectReview.getString("content");
                Review review = new Review(author, content);
                result.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Trailer> getTrailersJSON (JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null) return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            //в цикле получаем отзывы
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObjectTrailers= jsonArray.getJSONObject(i);
                String key = "https://www.youtube.com/watch?v=" + jsonObjectTrailers.getString("key");
                String name = jsonObjectTrailers.getString("name");
                Trailer trailer = new Trailer(key, name);
                result.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static ArrayList<Movie> getMovieJSON(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if(jsonObject == null) return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            //в цикле получаем фильмы
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject objKino = jsonArray.getJSONObject(i);
                //по тегам выкачиваем инфу про фильм
                int id = objKino.getInt("id");
                String title = objKino.getString("title");
                String original_title = objKino.getString("original_title");
                int vote_count = objKino.getInt("vote_count");
                String overview = objKino.getString("overview");
                String big_poster_path = baseImageUrl + big_poster_size + objKino.getString("poster_path");
                String poster_path = baseImageUrl + small_poster_size + objKino.getString("poster_path");
                String backdrop_path = objKino.getString("backdrop_path");
                double vote_average = objKino.getDouble("vote_average");
                String data_of_release = objKino.getString("release_date");
                Movie m = new Movie(id, title, original_title, vote_count, overview, backdrop_path, big_poster_path, poster_path, vote_average, data_of_release);
                result.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}