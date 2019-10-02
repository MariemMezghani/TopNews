package com.github.mariemmezghani.topnews;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mariemmezghani.topnews.Model.Article;
import com.github.mariemmezghani.topnews.Model.News;
import com.github.mariemmezghani.topnews.NetworkUtils.GetDataService;
import com.github.mariemmezghani.topnews.NetworkUtils.Request;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private GetDataService mService;
    private SwipeRefreshLayout mSwipe;
    private ImageView articleImage;
    private TextView articleTitle;
    RecyclerView mArticlesList;
    ArticleAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //source2; setup drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,0,0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        //service
        mService = Request.getDataService();

        //View
        mSwipe=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        mArticlesList=(RecyclerView)findViewById(R.id.recyclerView);

        //RecyclerView
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mArticlesList.setLayoutManager(manager);
        mArticlesList.setHasFixedSize(true);
        loadNews();




    }
    private void loadNews(){
        mService.getNewsArticles(Request.getNewsUrl("bbc-news"))
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {

                        //load articles
                        List<Article> articles= response.body().getArticles();
                        mArticleAdapter=new ArticleAdapter(getBaseContext());
                        mArticleAdapter.setArticles(articles);
                        mArticlesList.setAdapter(mArticleAdapter);
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
