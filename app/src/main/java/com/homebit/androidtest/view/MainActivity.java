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
import com.homebit.androidtest.model.User.User;
import com.homebit.androidtest.utils.APIHandler;
import com.homebit.androidtest.utils.Constants;
import com.homebit.androidtest.utils.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerViewUserList;
    RecyclerView.LayoutManager mLayoutManager;

    UserListAdapter userAdapter;
    ProgressDialog progressDialog;
    Gson mGson;
    ArrayList<User> arrayListUserList = new ArrayList<>();
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGson = new GsonBuilder().create();
        databaseHandler = new DatabaseHandler(MainActivity.this);
        setUI();

        getUserList();
    }

    private void setUI(){

        recyclerViewUserList = findViewById(R.id.recycler_user_list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUserList.setLayoutManager(mLayoutManager);
        recyclerViewUserList.setItemAnimator(new DefaultItemAnimator());
    }


    private void getUserList()
    {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE_PLEASE_WAIT);
        progressDialog.setCancelable(false);
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        try {
            new APIHandler().getsharedInstance(MainActivity.this).executeJsonArray(Request.Method.GET, APIHandler.restAPI.USER_LIST, null, new Response.Listener<JSONArray>() {


                @Override
                public void onResponse(JSONArray response) {


                    try {
                        JSONArray jsonArrayUser = new JSONArray(response.toString());

                        for (int i = 0 ; i < jsonArrayUser.length() ; i ++) {
                            User transaction = mGson.fromJson(jsonArrayUser.getJSONObject(i).toString().toString(), User.class);
                            arrayListUserList.add(transaction);
                        }

                        databaseHandler.addUserDetails(arrayListUserList);
                        userAdapter = new UserListAdapter(arrayListUserList, MainActivity.this);
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
