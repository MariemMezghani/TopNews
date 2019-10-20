package com.github.mariemmezghani.topnews.NetworkUtils;

import com.github.mariemmezghani.topnews.BuildConfig;

public class Request {

    private static final String BASE_URL ="https://newsapi.org/";

    public static GetDataService getDataService(){
        return RetrofitClient.getClient(BASE_URL).create(GetDataService.class);
    }

    //https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=1f47b3b163be465e8cbd9da77cc158b1
    public static String getNewsUrl(String source){
        StringBuilder newsUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return newsUrl.append(source)
                .append("&apiKey=")
                .append(BuildConfig.ApiKey)
                .toString();
    }
}
