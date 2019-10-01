package com.github.mariemmezghani.topnews.NetworkUtils;

public class Request {

    private static final String BASE_URL ="https://newsapi.org/";
    public static final String API_KEY = "1f47b3b163be465e8cbd9da77cc158b1";

    public static GetDataService getDataService(){
        return RetrofitClient.getClient(BASE_URL).create(GetDataService.class);
    }
}
