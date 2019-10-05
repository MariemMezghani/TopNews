package com.github.mariemmezghani.topnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ShareCompat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mariemmezghani.topnews.Model.Article;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Article article;
    private ImageView newsImage;
    private TextView articleTitle;
    private TextView articleByLine;
    private TextView articleContent;
    private Button readButton;
    private FloatingActionButton shareButton;
    Toolbar toolbar;
    CustomTabsIntent customTabsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        //views
        newsImage=(ImageView) findViewById(R.id.news_image_detail);
        articleTitle=(TextView) findViewById(R.id.article_title);
        articleByLine=(TextView)findViewById(R.id.article_byline);
        articleContent=(TextView)findViewById(R.id.article_content);
        readButton=(Button)findViewById(R.id.read_button);
        shareButton=(FloatingActionButton)findViewById(R.id.share_fab);

        final Intent intentThatStartedThisActivity = this.getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("article")) {
                article = (Article) intentThatStartedThisActivity.getParcelableExtra("article");
                Picasso.with(this).load(article.getUrlToImage()).into(newsImage);
                articleTitle.setText(article.getTitle());
                articleByLine.setText(article.getAuthor());
                articleContent.setText(article.getDescription());

                // button click handling

                readButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (MainActivity.isOnline(DetailActivity.this)) {

                            // Launch Chrome Custom Tabs on click
                            //Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                            //startActivity(webIntent);
                            //source4
                            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = intentBuilder.build();
                            customTabsIntent.launchUrl(DetailActivity.this, Uri.parse(article.getUrl()));

                        } else {

                            Toast.makeText(DetailActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();

                        }
                    }

                });
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        share();
                    }
                });



            }

        }
    }
    private void share(){
        ShareCompat.IntentBuilder.from(this)

                .setChooserTitle(R.string.action_share)

                .setType("text/plain")

                .setText(article.getTitle())

                .startChooser();

    }
}
