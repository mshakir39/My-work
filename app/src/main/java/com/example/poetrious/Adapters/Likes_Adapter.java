package com.example.poetrious.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Activities.Users_profile;
import com.example.poetrious.Models.Like_list;
import com.example.poetrious.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Likes_Adapter extends RecyclerView.Adapter<Likes_Adapter.Viewholder> {


    public List<Like_list> mpost;

    public Likes_Adapter(List<Like_list> mpost, Context mcontext) {
        this.mpost = mpost;
        this.mcontext = mcontext;
    }

    public Context mcontext;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.like_model, parent, false);

        return new Likes_Adapter.Viewholder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final Like_list comment_list=mpost.get(position);
//if(comment_list.getProfile_img_Download_link()!=null)
//{

    Uri profile=Uri.parse(comment_list.getProfile_img_Download_link());
    Picasso.with(mcontext).load(profile).rotate(90f).into(holder.profile);
    holder.Name.setText(comment_list.getUser_Name());
//}

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext.getApplicationContext(), Users_profile.class);
                intent.putExtra("usr_id", comment_list.getUser_ID());
                Log.e("uid",comment_list.getUser_ID()+"");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Log.e("Folllllow",show_follow_list.getUser_ID());
                mcontext.startActivity(intent);
            }
        });

        // getuserInfo(holder.profile,holder.comments,comment_list.getPublisher());

    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class Viewholder extends RecyclerView.ViewHolder {

        ImageView profile, liker, comment, favorite, post_pic;
        TextView Name, category, time, likes;
        Viewholder(@NonNull View itemView) {

            super(itemView);


            profile = itemView.findViewById(R.id.C_Profile_image);
            Name=itemView.findViewById(R.id.C_user_name);

        }
    }
    private void getuserInfo(final ImageView imageView, final TextView textView, final String publisherid)
    {


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users_info").child(publisherid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    //   Post_List p=dataSnapshot1.getValue(Post_List.class);
                    String image=dataSnapshot1.child("Profile_img_Download_link").getValue(String.class);
                    String Name=dataSnapshot1.child("User_Name").getValue(String.class);


                    //   Picasso.with(mcontext).load(image).rotate(90f).into(imageView);
                    // textView.setText(Name);
                    Log.e("imgggg",image+"");
                    Log.e("imgggg",Name+"");
                    Log.e("imgggg",publisherid+"");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
