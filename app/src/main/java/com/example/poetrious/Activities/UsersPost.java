package com.example.poetrious.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.PostAdapter;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersPost extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Post_List> lists;
    int position;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);
        recyclerView = findViewById(R.id.post_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(UsersPost.this);
        recyclerView.setLayoutManager(mLayoutManager);

        position   = getIntent().getIntExtra("pos",0);
        uid   = getIntent().getStringExtra("usr_id");
        Log.e("Position",position+"");





        lists = new ArrayList<>();
        lists.clear();
       background background = new background();
        background.start();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
//        databaseReference.keepSynced(true);
    }


    class background extends Thread {
        @Override
        public void run() {
            super.run();

            SharedPreferences sp1 = getSharedPreferences("UID", 0);
            final String cb1 = sp1.getString("User_id", "false");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_Post").child(uid);
            final HashMap<String, String> insert_profile_data = new HashMap<>();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    lists.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Post_List p = dataSnapshot1.getValue(Post_List.class);
                        //     p.setPost_time(Objects.requireNonNull(dataSnapshot1.child("post_time").getValue()).toString());

                        //  Log.i("postt", String.valueOf(p));
                        lists.add(p);
                    }
                    recyclerView.scrollToPosition(position);
                    adapter = new PostAdapter(UsersPost.this, lists);
                    recyclerView.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }


}
