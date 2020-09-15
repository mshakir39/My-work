package com.example.poetrious.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.poetrious.Adapters.Post_img_adapter;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Users_profile extends AppCompatActivity {
    ImageView user_img;
    TextView Name, postss, likess, Followers, Following, ranks,Follow_check,Following_check;
    String sessionId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button Follow, unFollow;
    DatabaseReference myRef = database.getReference("Users_info");
    RecyclerView recyclerView;
    List<Post_List> lists;
    String user_id;
    RecyclerView.Adapter adapter;
    Button MSG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        user_img = findViewById(R.id.profile_image);
        Name = findViewById(R.id.main_prof_name);
        postss = findViewById(R.id.posts);
        Following_check=findViewById(R.id.textView14);
        Follow = findViewById(R.id.edit);
        Follow_check=findViewById(R.id.textView4);
        unFollow = findViewById(R.id.unfollow);
        MSG = findViewById(R.id.msg);
        recyclerView = findViewById(R.id.list2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        likess = findViewById(R.id.likes);
        Followers = findViewById(R.id.textView12);
        Following = findViewById(R.id.textView13);
        ranks = findViewById(R.id.textView8);
        sessionId = getIntent().getStringExtra("usr_id");
        Log.e("iddddddddddd", sessionId + "");
        assert sessionId != null;
        lists = new ArrayList<>();

        Users_profile users_profile = new Users_profile();
        count_followers();
        count_following();
        isFollowing();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_Post").child(sessionId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post_List p = dataSnapshot1.getValue(Post_List.class);
                    //    Log.e("postid",p.getPost_id());


                    Log.i("postt", String.valueOf(p));
                    lists.add(p);
                }

                adapter = new Post_img_adapter(getApplicationContext(), lists);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Following_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();


                int page = 1;
                Intent intent = new Intent(Users_profile.this,Follower_Following.class);
             //   intent.putExtra("One", page);
                bundle.putInt("One", page);
                bundle.putString("usr_id",sessionId);
                intent.putExtras(bundle);
              //  intent.putExtra("usr_id", sessionId);
                //  startActivity(new Intent(Users_profile.this,Follower_Following.class));
                startActivity(intent);
            }
        });
Follow_check.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int page = 0;
        Bundle bundle=new Bundle();
        Intent intent = new Intent(Users_profile.this,Follower_Following.class);
    //    intent.putExtra("One", page);
        bundle.putInt("One", page);
        bundle.putString("usr_id",sessionId);
        intent.putExtras(bundle);
      //  startActivity(new Intent(Users_profile.this,Follower_Following.class));
        startActivity(intent);

    }
});

        Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference()
                        .child("Followers")
                        .child(sessionId)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("user_id")
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                FirebaseDatabase.getInstance().getReference()
                        .child("Following")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(sessionId)
                        .child("user_id")
                        .setValue(sessionId);
                setFollowing();
            }


        });

        unFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Followers")
                        .child(sessionId)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();

                FirebaseDatabase.getInstance().getReference()
                        .child("Following")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(sessionId)
                        .removeValue();
                setUnfollowing();
            }


        });


        final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info").child(sessionId);
        final DatabaseReference user_prof = FirebaseDatabase.getInstance().getReference().child("Users_profile").child(sessionId);
        usernamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("Profile_img_Download_link").getValue(String.class);
                String name = snapshot.child("User_Name").getValue(String.class);
                Log.e("URI", image + "");
                Uri profile = Uri.parse(image);
                Picasso.with(Users_profile.this).load(profile).rotate(90f).into(user_img);
                //    user_img.setImageURI(Uri.parse(image));

                Name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        MSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("usr_id", sessionId);
                // Log.e("Folllllow",show_follow_list.getUser_ID());
                startActivity(intent);
                //   startActivity(new Intent(getApplicationContext(),ChatActivity.class));
            }
        });
        user_prof.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    String like = snapshot.child("total_likes").getValue(String.class);
                    String posts = snapshot.child("total_post").getValue(String.class);
                    String follows = snapshot.child("follows").getValue(String.class);
                    String followers = snapshot.child("followers").getValue(String.class);
                    String rank = snapshot.child("rank").getValue(String.class);

                    likess.setText(like);
                    postss.setText(posts);

                    ranks.setText(rank);
                } else {
                    likess.setText("0");
                    postss.setText("0");

                    ranks.setText("0");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isFollowing() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(sessionId).exists()) {
                    Log.e("USER", snapshot.toString() + "");
                    setFollowing();
                } else {

                    setUnfollowing();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class background extends Thread {
        @Override
        public void run() {
            super.run();


        }

    }

    private void count_followers() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Followers")
                .child(sessionId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("COUNt_Child_f", snapshot.getChildrenCount() + "");
                    String size = String.valueOf(snapshot.getChildrenCount());
                    Followers.setText(size);
                } else {
                    Followers.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setFollowing() {

        Follow.setVisibility(View.INVISIBLE);
        unFollow.setVisibility(View.VISIBLE);

    }

    private void setUnfollowing() {
        Follow.setVisibility(View.VISIBLE);
        unFollow.setVisibility(View.INVISIBLE);


    }

    private void count_following() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Following")
                .child(sessionId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("COUNt_Child", snapshot.getChildrenCount() + "");
                    String size = String.valueOf(snapshot.getChildrenCount());
                    Following.setText(size);
                } else {
                    Following.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}