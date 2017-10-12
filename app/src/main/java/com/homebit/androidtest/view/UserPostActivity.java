package com.homebit.androidtest.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homebit.androidtest.R;
import com.homebit.androidtest.controller.UserListAdapter;
import com.homebit.androidtest.controller.UserPostAdapter;
import com.homebit.androidtest.model.User.User;
import com.homebit.androidtest.model.UserPost.Post;
import com.homebit.androidtest.utils.APIHandler;
import com.homebit.androidtest.utils.Constants;
import com.homebit.androidtest.utils.DatabaseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

public class UserPostActivity extends AppCompatActivity {

    RecyclerView recyclerViewUserPost;
    RecyclerView recyclerViewUserList;
    RecyclerView.LayoutManager mLayoutManager;

    UserPostAdapter userAdapter;
    ProgressDialog progressDialog;
    Gson mGson;
    ArrayList<Post> arrayListUserPost = new ArrayList<>();
    int selectedUserId;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        mGson = new GsonBuilder().create();
        databaseHandler = new DatabaseHandler(UserPostActivity.this);
        selectedUserId = getIntent().getIntExtra(Constants.EXTRA_KEY_USER_ID,1);

        setUI();
        getUserPost();
    }

    private void setUI(){

        recyclerViewUserList = findViewById(R.id.recycler_user_post);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUserList.setLayoutManager(mLayoutManager);
        recyclerViewUserList.setItemAnimator(new DefaultItemAnimator());
    }

    private void getUserPost()
    {

        progressDialog = new ProgressDialog(UserPostActivity.this);
        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE_PLEASE_WAIT);
        progressDialog.setCancelable(false);
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        try {
            new APIHandler().getsharedInstance(UserPostActivity.this).executeJsonArray(Request.Method.GET, APIHandler.restAPI.USER_POST+selectedUserId, null, new Response.Listener<JSONArray>() {


                @Override
                public void onResponse(JSONArray response) {


                    try {
                        JSONArray jsonArrayUser = new JSONArray(response.toString());

                        for (int i = 0 ; i < jsonArrayUser.length() ; i ++) {
                            Post userPost = mGson.fromJson(jsonArrayUser.getJSONObject(i).toString().toString(), Post.class);
                            arrayListUserPost.add(userPost);
                        }


                        userAdapter = new UserPostAdapter(arrayListUserPost, UserPostActivity.this);
                        recyclerViewUserList.setAdapter(userAdapter);


                    }catch (Exception e)
                    {
                        System.out.println("Json Exception " + e.getMessage());
                    }
                    finally {
                        progressDialog.dismiss();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }


            }, null);

        }
        catch (Exception e)
        {

        }
    }
}
