package com.github.mariemmezghani.topnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mariemmezghani.topnews.Model.Article;
import com.github.mariemmezghani.topnews.Model.Comment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    Article article;
    private ImageView newsImage;
    private TextView articleTitle;
    private TextView articleByLine;
    private TextView articleContent;
    private Button readButton;
    private ListView mMessageListView;
    private EditText mMessageEditText;
    private Button mSendButton;
    private FloatingActionButton shareButton;
    Toolbar toolbar;
    CustomTabsIntent customTabsIntent;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private CommentAdapter mCommentAdapter;
    private List<Comment> commentList;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String ANONYMOUS = "anonymous";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //views
        newsImage=(ImageView) findViewById(R.id.news_image_detail);
        articleTitle=(TextView) findViewById(R.id.article_title);
        articleByLine=(TextView)findViewById(R.id.article_byline);
        articleContent=(TextView)findViewById(R.id.article_content);
        readButton=(Button)findViewById(R.id.read_button);
        shareButton=(FloatingActionButton)findViewById(R.id.share_fab);
        mMessageEditText=(EditText) findViewById(R.id.messageEditText);
        mMessageListView=(ListView)findViewById(R.id.messageListView);
        mSendButton=(Button)findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        commentList = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(DetailActivity.this, R.layout.item_message, commentList);
        mMessageListView.setAdapter(mCommentAdapter);
        ListViewUtils.setListViewHeightBasedOnItems(mMessageListView);

        //initialize Firebase
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mMessagesDatabaseReference=mFirebaseDatabase.getReference("messages");
        mFirebaseAuth= FirebaseAuth.getInstance();
        mUsername=ANONYMOUS;

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

                // Enable Send button when there's text to send
                mMessageEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().trim().length() > 0) {
                            mSendButton.setEnabled(true);
                        } else {
                            mSendButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

                // Send button sends a message and clears the EditText
                mSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Comment comment = new Comment(mUsername, mMessageEditText.getText().toString(), article.getTitle());
                        mMessagesDatabaseReference.push().setValue(comment);

                        // Clear input box
                        mMessageEditText.setText("");
                    }
                });
                mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user!= null){
                            //user is signed in
                            mUsername = firebaseAuth.getCurrentUser().getDisplayName();
                            attachDatabaseReadListener();
                        }else{
                            //user is signed out
                            mUsername = ANONYMOUS;
                            mCommentAdapter.clear();
                            detachDatabasReadListener();
                        }

                    }
                };
            }

        }
    }

    private void share(){
        ShareCompat.IntentBuilder.from(this)

                .setChooserTitle(R.string.action_share)

                .setType("text/plain")

                .setText(article.getUrl())

                .startChooser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabasReadListener();
        mCommentAdapter.clear();
    }
    private void attachDatabaseReadListener(){

        if(mChildEventListener==null) {
            // source 5: query firebase database
            mMessagesDatabaseReference.orderByChild("newsTitle").equalTo(article.getTitle()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    mCommentAdapter.add(comment);
                    mCommentAdapter.notifyDataSetChanged();
                    ListViewUtils.setListViewHeightBasedOnItems(mMessageListView);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void detachDatabasReadListener(){

        if (mChildEventListener != null) {

            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

    }

    }

