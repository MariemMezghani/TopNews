package com.github.mariemmezghani.topnews.NetworkUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import com.github.mariemmezghani.topnews.BuildConfig;
import com.github.mariemmezghani.topnews.Model.News;
import com.github.mariemmezghani.topnews.Model.Source;

import java.util.List;

public interface GetDataService {

    @GET("v2/sources?language=en&apiKey="+ BuildConfig.ApiKey)
    Call<List<Source>> getSources();

    @GET
    Call<News> getNewsArticles(@Url String url);

}
