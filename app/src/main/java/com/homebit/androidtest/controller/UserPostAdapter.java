package com.homebit.androidtest.controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homebit.androidtest.R;
import com.homebit.androidtest.model.UserPost.Post;

import java.util.ArrayList;

/**
 * Created by pardypanda05 on 12/10/17.
 */

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.MyViewHolder>{

    ArrayList<Post> arrayListUsers;
    Activity mActivity;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPostTitle, textViewPostBody;

        public MyViewHolder(View view) {
            super(view);
            textViewPostBody = (TextView) view.findViewById(R.id.text_post_title);
            textViewPostTitle = (TextView) view.findViewById(R.id.text_post_body);

        }
    }


    public UserPostAdapter(ArrayList<Post> arrayListUsers, Activity activity) {

        this.arrayListUsers = arrayListUsers;
        this.mActivity = activity;

    }

    @Override
    public UserPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_post, parent, false);

        return new UserPostAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final UserPostAdapter.MyViewHolder holder, final int position) {

        Post userPost = arrayListUsers.get(position);
        holder.textViewPostBody.setText("Title "+ userPost.getTitle());
        holder.textViewPostTitle.setText("Body "+userPost.getBody());
    }

    @Override
    public int getItemCount() {
        return arrayListUsers.size();
    }


}

