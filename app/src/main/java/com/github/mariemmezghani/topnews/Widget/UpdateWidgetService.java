package com.github.mariemmezghani.topnews.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.github.mariemmezghani.topnews.Model.Article;
import com.github.mariemmezghani.topnews.Model.News;
import com.github.mariemmezghani.topnews.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UpdateWidgetService extends IntentService {
    ArrayList<Article> articles;

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public void startService(Context context, ArrayList<Article> articles){
        Intent intent= new Intent(context, UpdateWidgetService.class);
        intent.putExtra("newsList", articles);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String newsString=prefs.getString("newsString","");
            //source8
            Type type = new TypeToken<ArrayList<News>>(){}.getType();
            articles=new Gson().fromJson(newsString,type);
            handleActionUpdateWidgets(articles);
        }

    }
    private void handleActionUpdateWidgets(ArrayList<Article> arrayList){
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), NewsWidgetProvider.class));
        //Trigger data update to handle the widgets and force a data refresh
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.news_list);
        //update all widgets
        NewsWidgetProvider.updateWidgets(this,widgetManager, ids);


    }

}
