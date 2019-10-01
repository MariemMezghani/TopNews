package com.github.mariemmezghani.topnews.NetworkUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import com.github.mariemmezghani.topnews.Model.Source;

import java.util.List;

public interface GetDataService {

    @GET("v2/sources?language=en&apiKey="+Request.API_KEY)
    Call<List<Source>> getSources();

}
