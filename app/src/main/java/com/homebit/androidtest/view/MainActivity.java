package com.homebit.androidtest.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
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
import com.homebit.androidtest.utils.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerViewUserList;
    TextView textViewErrorMessage;
    RecyclerView.LayoutManager mLayoutManager;

    UserListAdapter userAdapter;
    ProgressDialog progressDialog;
    Gson mGson;
    ArrayList<User> arrayListUserList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGson = new GsonBuilder().create();
        setUI();
        if(Utils.isNetworkAvailable(MainActivity.this)) {
            textViewErrorMessage.setVisibility(View.GONE);
            recyclerViewUserList.setVisibility(View.VISIBLE);
            getUserList();
        }else {
            textViewErrorMessage.setText("No internet connection, please check connection.");
            textViewErrorMessage.setVisibility(View.VISIBLE);
            recyclerViewUserList.setVisibility(View.GONE);
        }
    }

    private void setUI(){

        recyclerViewUserList = findViewById(R.id.recycler_user_list);
        textViewErrorMessage = findViewById(R.id.text_error_message);

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
                    if (error instanceof NoConnectionError) {
                        textViewErrorMessage.setText("No internet connection, please check connection.");
                    }else{
                        textViewErrorMessage.setText("Something went wrong please try again.");
                    }

                }


            }, null);

        }
        catch (Exception e)
        {

        }
    }
}
