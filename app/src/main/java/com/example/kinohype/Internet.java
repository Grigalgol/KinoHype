package com.example.kinohype;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Internet {
    private static String site = "https://www.themoviedb.org/"; //сайт для получения фильмов

    private static String baseURLreviews = "https://api.themoviedb.org/3/movie/%s/reviews";
    private static String baseURLvideo = "https://api.themoviedb.org/3/movie/%s/videos";
    private static String apikey = "3471f739f0d9ecfeace2ffbbc4432052"; //api ключ
    private static String baseurl = "https://api.themoviedb.org/3/discover/movie"; //базовый url
    //параметры для запроса
    private static String params_page = "page";
    private static String params_apikey = "api_key";
    private static String params_language = "language";
    private static String params_sortby = "sort_by";
    private static String params_votecounte = "vote_count.gte";
    //значение для языка
    private static String LANGUAGE = "ru-RU";
    //ключи для подборки фильмов
    public static int POP = 0;
    public static int TOP = 1;
    public static int BES = 2;

    private static URL buildURLTiTrailer(int id) {
        //геним ссылку
        Uri uri = Uri.parse(String.format(baseURLvideo, id)).buildUpon().appendQueryParameter(params_apikey, apikey).appendQueryParameter(params_language, LANGUAGE).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL buildURLToReviews(int id) {
        //геним ссылку
        Uri uri = Uri.parse(String.format(baseURLreviews, id)).buildUpon().appendQueryParameter(params_apikey, apikey).appendQueryParameter(params_language, LANGUAGE).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static URL buildURL(int s, int page) {
        String sort = null;
        switch (s) {
            case 0:
                sort = "popularity.desc";
                break;
            case 1:
                sort = "vote_average.desc";
                break;
            case 2:
                sort = "revenue.desc";
                break;
        }
        Uri uri = Uri.parse(baseurl).buildUpon().appendQueryParameter(params_language, LANGUAGE).appendQueryParameter(params_apikey, apikey).appendQueryParameter(params_sortby, sort).appendQueryParameter(params_votecounte, "500").appendQueryParameter(params_page, Integer.toString(page)).build();
        //результат
        URL result = null;
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JsonTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject res = null;
            //~Если пусто, возврат 0
            if (urls == null) return null;
            HttpURLConnection connection = null;
            //открываем соединение
            try {
                StringBuilder builder = new StringBuilder();
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream(); //поток ввода
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //для чтения строками используем <-
                //читаем данные
                String s = bufferedReader.readLine();
                while (s != null) {
                    builder.append(s);
                    s = bufferedReader.readLine();
                }
                try {
                    res = new JSONObject(builder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //закрываем соединение
                if (connection != null) connection.disconnect();
            }
            return res;
        }
    }

    public static JSONObject getJSONReviewsfromInternet(int id) {
        JSONObject res = null;
        URL url = buildURLToReviews(id);
        try {
            res = new JsonTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static JSONObject getJSONTrailerfromInternet(int id) {
        JSONObject res = null;
        URL url = buildURLTiTrailer(id);
        try {
            res = new JsonTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static JSONObject getJSONObjectfromInternet(int s, int page) {
        JSONObject res = null;
        URL url = buildURL(s, page);
        try {
            res = new JsonTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }
}
