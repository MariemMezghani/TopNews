package com.github.mariemmezghani.topnews.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.mariemmezghani.topnews.Model.Article;
import com.github.mariemmezghani.topnews.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new RemoteViewsFactory(this.getApplicationContext(),intent);
    }

    public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        public  ArrayList<Article> newsArrayList = new ArrayList<>();
        public RemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String newsString=prefs.getString("newsString","");
            Type type = new TypeToken<ArrayList<Article>>(){}.getType();
            newsArrayList=new Gson().fromJson(newsString,type);


        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return newsArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews item = new RemoteViews(mContext.getPackageName(), R.layout.news_widget_list_item);
            item.setTextViewText(R.id.widget_list_item, newsArrayList.get(i).getTitle());
            return item;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
