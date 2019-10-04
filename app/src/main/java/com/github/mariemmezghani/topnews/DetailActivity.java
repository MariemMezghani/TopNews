package com.github.mariemmezghani.topnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.mariemmezghani.topnews.Model.Article;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Article article;
    private ImageView newsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //views
        newsImage=(ImageView) findViewById(R.id.news_image_detail);

        final Intent intentThatStartedThisActivity = this.getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("article")) {
                article = (Article) intentThatStartedThisActivity.getParcelableExtra("article");
                Picasso.with(this).load(article.getUrlToImage()).into(newsImage);


            }

        }
    }
}
