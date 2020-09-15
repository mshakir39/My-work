package com.example.poetrious.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Activities.Drawer;
import com.example.poetrious.Activities.Users_profile;
import com.example.poetrious.Models.User_data;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.FollowViewHolder> {
    private String [] data;
    public Context context;
    public List<User_data> mpost;
    public Search_Adapter(Context mcontext, List<User_data> mpost)
    {
        this.context = mcontext;
        this.mpost = mpost;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.search_item,parent,false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        final User_data user_data=mpost.get(position);
        Log.i("profile",user_data.getProfile_img_Download_link()+"");
        String prof=user_data.getProfile_img_Download_link();
        final String s_id=user_data.getUser_ID();

        // holder.image.setImageURI(Uri.parse(show_follow_list.getProfile_img_Download_link()));
        if(prof!=null)
        {
            Log.d("Checkingg",prof+"");
            Uri profile=Uri.parse(prof);
            Picasso.with(context).load(profile).into(holder.image);

        }
        else {
            Log.d("Checkingg",prof+"Null");
        }
        holder.User_name.setText(user_data.getUser_Name());
        //  final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info");

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context.getApplicationContext(), Users_profile.class);
//                intent.putExtra("usr_id", show_follow_list.getUser_ID());
//                Log.e("Folllllow",show_follow_list.getUser_ID());
//                context.startActivity(intent);

                if(s_id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    Intent intent = new Intent(context.getApplicationContext(), Drawer.class);


                    context.startActivity(intent);
                }
                else
                {
                Intent intent = new Intent(context.getApplicationContext(), Users_profile.class);
                intent.putExtra("usr_id", user_data.getUser_ID());

                context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mpost.size();
    }

    public static class FollowViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton back;
        ImageView image,dots;  TextView User_name, type;
        Button Follow;


        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.search_img);

            User_name=itemView.findViewById(R.id.search_name);

        }
    }




}
