package com.homebit.androidtest.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homebit.androidtest.R;
import com.homebit.androidtest.model.User.User;
import com.homebit.androidtest.utils.Constants;
import com.homebit.androidtest.view.UserPostActivity;

import java.util.ArrayList;

/**
 * Created by pardypanda05 on 12/10/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    ArrayList<User> arrayListUsers;
    Activity mActivity;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUserName, textViewFullName, textViewUserAddress, textViewPhoneNumber;

        public LinearLayout linearLayoutUserDetail;
        public MyViewHolder(View view) {
            super(view);
            textViewFullName = (TextView) view.findViewById(R.id.text_full_name);
            textViewUserName = (TextView) view.findViewById(R.id.text_user_name);
            textViewUserAddress = (TextView) view.findViewById(R.id.text_user_address);
            textViewPhoneNumber = (TextView) view.findViewById(R.id.text_user_phone_number);
            linearLayoutUserDetail = view.findViewById(R.id.layout_user_detail);
        }
    }


    public UserListAdapter(ArrayList<User> arrayListUsers, Activity activity) {

        this.arrayListUsers = arrayListUsers;
        this.mActivity = activity;

    }

    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);

        return new UserListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final UserListAdapter.MyViewHolder holder, final int position) {

        final User userDetails = arrayListUsers.get(position);
        holder.textViewFullName.setText("Full : Name " + userDetails.getName());
        holder.textViewUserName.setText("Username : " + userDetails.getUsername());
        holder.textViewUserAddress.setText("Address : " +userDetails.getAddress().getSuite() + ", " + userDetails.getAddress().getStreet() + ", " + userDetails.getAddress().getCity()+ "-"+userDetails.getAddress().getZipcode());
        holder.textViewPhoneNumber.setText("Phone Number : " + userDetails.getPhone());
        holder.linearLayoutUserDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UserPostActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_USER_ID, userDetails.getId());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListUsers.size();
    }


}
