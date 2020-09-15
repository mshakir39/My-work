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

import com.example.poetrious.Activities.Users_profile;
import com.example.poetrious.Models.Show_follow_list;
import com.example.poetrious.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Followers_Adapter extends RecyclerView.Adapter<Followers_Adapter.FollowViewHolder> {
    private String [] data;
    String uid;
    public Context context;
    public List<Show_follow_list> mpost;
    public Followers_Adapter(Context mcontext, List<Show_follow_list> mpost)
    {
        this.context = mcontext;
        this.mpost = mpost;
    }

    public Followers_Adapter(String sessionId) {
        this.uid=sessionId;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.follow_design,parent,false);

        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowViewHolder holder, final int position) {
        final Show_follow_list show_follow_list=mpost.get(position);
        Log.i("profile",show_follow_list.getProfile_img_Download_link()+"");
        String prof=show_follow_list.getProfile_img_Download_link();
        holder.Follow.setVisibility(View.GONE);
        holder.unFollow.setVisibility(View.VISIBLE);
        holder.unFollow.setText("remove");
        // holder.image.setImageURI(Uri.parse(show_follow_list.getProfile_img_Download_link()));
        if(prof!=null)
        {
            Log.d("Checkingg",prof+"");
            Uri profile=Uri.parse(prof);
            Picasso.with(context).load(profile).rotate(90f).into(holder.image);

        }
        else {
            Log.d("Checkingg",prof+"Null");
        }


        holder.User_name.setText(show_follow_list.getUser_Name());
        //  final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info");

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), Users_profile.class);
                intent.putExtra("usr_id", show_follow_list.getUser_ID());
                Log.e("Folllllow",show_follow_list.getUser_ID());
                context.startActivity(intent);
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
        Button Follow,unFollow;


        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            back=itemView.findViewById(R.id.go_back);
            image=itemView.findViewById(R.id.Profile_image);
            dots=itemView.findViewById(R.id.dot);
            User_name=itemView.findViewById(R.id.user_name);
            type=itemView.findViewById(R.id.category);
            Follow=itemView.findViewById(R.id.follow);
            unFollow=itemView.findViewById(R.id.unfollow);
        }
    }




}
