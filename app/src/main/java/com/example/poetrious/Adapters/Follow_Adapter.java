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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Follow_Adapter extends RecyclerView.Adapter<Follow_Adapter.FollowViewHolder> {
private String [] data;
String uid;
public Context context;
    public List<Show_follow_list> mpost;
    public Follow_Adapter(String id)
    {
this.uid=id;
    }
   public Follow_Adapter(Context mcontext, List<Show_follow_list> mpost)
    {
        this.context = mcontext;
        this.mpost = mpost;
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
        isFollowing(holder.Follow,holder.unFollow,show_follow_list.getUser_ID());
        holder.Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference()
                        .child("Followers")
                        .child(show_follow_list.getUser_ID())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("user_id")
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                FirebaseDatabase.getInstance().getReference()
                        .child("Following")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(show_follow_list.getUser_ID())
                        .child("user_id")
                        .setValue(show_follow_list.getUser_ID());
setFollowing(holder.Follow,holder.unFollow);
            }



        });


holder.unFollow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseDatabase.getInstance().getReference()
                .child("Followers")
                .child(show_follow_list.getUser_ID())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
               .removeValue();

        FirebaseDatabase.getInstance().getReference()
                .child("Following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(show_follow_list.getUser_ID())
                .removeValue();
        setUnfollowing(holder.Follow,holder.unFollow);
    }


});

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
    private void setFollowing(Button follow,Button unfollow )
    {

       follow.setVisibility(View.GONE);
        unfollow.setVisibility(View.VISIBLE)     ;

    }
    private void setUnfollowing(Button follow,Button unfollow) {
       follow.setVisibility(View.VISIBLE);
        unfollow.setVisibility(View.GONE)     ;


    }
    private void isFollowing(final Button follow, final Button unfollow, String user_id)
    {

        Log.e("USER_IDDD",user_id+""+follow+unfollow);
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());



  databaseReference    .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Log.e("USER",snapshot.toString()+"");
                    setFollowing(follow,unfollow);
                }
                else {
                    setUnfollowing(follow,unfollow);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
