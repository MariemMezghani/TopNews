package com.github.mariemmezghani.topnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mariemmezghani.topnews.Model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{

    private Context mContext;
    private List<Article> mArticles;
    private final ArticleAdapterOnClickHandler mClickHandler;

    public ArticleAdapter( Context mContext, ArticleAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler=clickHandler;
    }
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        //set articleImage
        Picasso.with(mContext).load(mArticles.get(position).getUrlToImage()).into(holder.articleImage);
        // set article title
        holder.articleTitle.setText(mArticles.get(position).getTitle());
        //set article date
        holder.articleDate.setText(mArticles.get(position).getPublishedAt());

    }

    @Override
    public int getItemCount() {
        if (mArticles==null){
            return 0;
        }
        return mArticles.size();
    }

    public void setArticles(List<Article> articles){
        mArticles=articles;
        notifyDataSetChanged();
    }

    //the interface that recieves onClickmessages
    public interface ArticleAdapterOnClickHandler{
        void onClick(Article clickedArticle);
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView articleImage;
        TextView articleTitle;
        TextView articleDate;



        public ArticleViewHolder(View itemView) {
            super(itemView);
            articleImage = (ImageView) itemView.findViewById(R.id.news_image);
            articleTitle= (TextView) itemView.findViewById(R.id.news_title);
            articleDate=(TextView)itemView.findViewById(R.id.article_date);

            itemView.setOnClickListener(this);
        }

        // This gets called by the child views during a click.
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Article article = mArticles.get(adapterPosition);
            mClickHandler.onClick(article);
        }

    }
}
