package com.github.mariemmezghani.topnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.mariemmezghani.topnews.Model.Article;
import com.github.mariemmezghani.topnews.Model.News;
import com.github.mariemmezghani.topnews.NetworkUtils.GetDataService;
import com.github.mariemmezghani.topnews.NetworkUtils.Request;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.ArticleAdapterOnClickHandler {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private GetDataService mService;
    private SwipeRefreshLayout mSwipe;
    private TextView sourceToolbar;
    private TextView mErrorMessage;
    private TextView mNoDataMessage;
    RecyclerView mArticlesList;
    ArticleAdapter mArticleAdapter;
    private NavigationView navigationView;
    String urlSourceName;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //view
        sourceToolbar=(TextView)findViewById(R.id.source);
        mSwipe=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        mErrorMessage=(TextView) findViewById(R.id.error_message);
        mNoDataMessage=(TextView) findViewById(R.id.nodata_message);

        //initialize
        sourceToolbar.setText(getString(R.string.bbc));
        urlSourceName=getString(R.string.bbc_source);
        mFirebaseAuth= FirebaseAuth.getInstance();
        mUsername=ANONYMOUS;

        //source2; setup drawer toggle
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,0,0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        //RecyclerView
        mArticlesList=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mArticlesList.setLayoutManager(manager);
        mArticlesList.setHasFixedSize(true);
        mArticleAdapter = new ArticleAdapter(getBaseContext(),this);

        //setup drawer
        navigationView=(NavigationView)findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id==R.id.bbc){
                            navigationItemClick(getString(R.string.bbc),getString(R.string.bbc_source));
                        }else if (id==R.id.aljazeera){
                            navigationItemClick(getString(R.string.aljazeera),getString(R.string.aljazeera_source));

                        }else if (id==R.id.cnn){
                            navigationItemClick(getString(R.string.cnn),getString(R.string.cnn_source));

                        }else if (id==R.id.sign_out_menu){
                            AuthUI.getInstance().signOut(MainActivity.this);


                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                }
        );

        loadFromSavedSource();

        //implement swipe refresh; source3
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromSavedSource();

            }
        });
        //authentificatiom
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null) {
                    //user signed in
                    onSignedInInitialize(user.getDisplayName());
                    Toast.makeText(MainActivity.this, "You are welcome to friendlyChat",Toast.LENGTH_SHORT).show();

                }else{
                    //user signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Signed-In", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Sign-In Cancelled", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }
    private void onSignedOutCleanUp(){
        mUsername=ANONYMOUS;
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    private void loadFromSavedSource(){
        //read sharedPreferences
        SharedPreferences sharedPreferences=this.getPreferences(Context.MODE_PRIVATE);
        final String savedUrlSourceName = sharedPreferences.getString("UrlSourceName",null);
        final String savedSourceName = sharedPreferences.getString("SourceName", null);

        navigationItemClick(savedSourceName,savedUrlSourceName);

    }


    private void navigationItemClick(String source, String urlSource){

        if (source == null){
            sourceToolbar.setText(getString(R.string.bbc));
            urlSourceName=getString(R.string.bbc_source);

        }else{
            sourceToolbar.setText(source);
            SharedPreferences.Editor sharedPreferences = this.getPreferences(Context.MODE_PRIVATE).edit();
            sharedPreferences.putString("SourceName", source);
            sharedPreferences.putString("UrlSourceName",urlSource);
            sharedPreferences.commit();
            urlSourceName=urlSource;

        }
        loadNews(urlSourceName);

    }
    private void loadNews(String source){

           if (isOnline(this)) {

               //service
               mService = Request.getDataService();

               mService.getNewsArticles(Request.getNewsUrl(source))
                       .enqueue(new Callback<News>() {
                           @Override
                           public void onResponse(Call<News> call, Response<News> response) {

                               int code = response.code();
                               if (code != 200){
                                   mArticlesList.setVisibility(View.INVISIBLE);
                                   mErrorMessage.setVisibility(View.INVISIBLE);
                                   mNoDataMessage.setVisibility(View.VISIBLE);

                               }
                               mArticlesList.setVisibility(View.VISIBLE);
                               mErrorMessage.setVisibility(View.INVISIBLE);
                               mNoDataMessage.setVisibility(View.INVISIBLE);

                               //load articles
                               List<Article> articles = response.body().getArticles();
                               mArticleAdapter.setArticles(articles);
                               mArticlesList.setAdapter(mArticleAdapter);
                               mSwipe.setRefreshing(false);
                           }

                           @Override
                           public void onFailure(Call<News> call, Throwable t) {

                           }
                       });
           }else{
               showErrorMessage();


           }

    }

    @Override
    public void onClick(Article article){
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("article", article);
        startActivity(intentToStartDetailActivity);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //check network connectivity
    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager

                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }
    //show error message and hide the recyclerview
    private void showErrorMessage() {
        mArticlesList.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mNoDataMessage.setVisibility(View.INVISIBLE);
        mSwipe.setRefreshing(false);
    }
}
