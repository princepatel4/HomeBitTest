package com.homebit.androidtest.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homebit.androidtest.R;
import com.homebit.androidtest.controller.UserPostAdapter;
import com.homebit.androidtest.model.UserPost.Post;
import com.homebit.androidtest.utils.APIHandler;
import com.homebit.androidtest.utils.Constants;
import com.homebit.androidtest.utils.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

public class UserPostActivity extends AppCompatActivity {

    RecyclerView recyclerViewUserPost;
    RecyclerView recyclerViewUserList;
    RecyclerView.LayoutManager mLayoutManager;

    TextView textViewErrorMessage;
    UserPostAdapter userAdapter;
    ProgressDialog progressDialog;
    Gson mGson;
    ArrayList<Post> arrayListUserPost = new ArrayList<>();
    int selectedUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        mGson = new GsonBuilder().create();

        selectedUserId = getIntent().getIntExtra(Constants.EXTRA_KEY_USER_ID,1);

        setUI();

        if(Utils.isNetworkAvailable(UserPostActivity.this)) {
            textViewErrorMessage.setVisibility(View.GONE);
            recyclerViewUserList.setVisibility(View.VISIBLE);
            getUserPost();
        }else {
            textViewErrorMessage.setText("No internet connection, please check connection.");
            textViewErrorMessage.setVisibility(View.VISIBLE);
            recyclerViewUserList.setVisibility(View.GONE);
        }
    }

    private void setUI(){

        recyclerViewUserList = findViewById(R.id.recycler_user_post);
        textViewErrorMessage = findViewById(R.id.text_error_message);
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
