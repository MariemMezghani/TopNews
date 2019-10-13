package com.github.mariemmezghani.topnews;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mariemmezghani.topnews.Model.Comment;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);

        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);


        Comment comment = getItem(position);

        messageTextView.setText(comment.getText());

        authorTextView.setText(comment.getName());

        return convertView;
    }
}