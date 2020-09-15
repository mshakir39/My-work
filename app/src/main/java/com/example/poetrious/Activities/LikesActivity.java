package com.example.poetrious.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.Likes_Adapter;
import com.example.poetrious.Models.Like_list;
import com.example.poetrious.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LikesActivity extends AppCompatActivity {
    ImageView C_image,C_send;
    EditText add_comment;
    private RecyclerView recyclerView;
    public List<Like_list> lists;
    Context context;
    private Likes_Adapter adapter;
    String postid;
    String pub_id;
    Like_list post_list=new Like_list();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        postid = Objects.requireNonNull(bundle).getString("postid");
        pub_id = bundle.getString("publisher_id");
        Log.d("IDDD",postid+"");



        recyclerView = findViewById(R.id.C_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(LikesActivity.this));
        lists=new ArrayList<Like_list>();
        adapter= new Likes_Adapter(lists,LikesActivity.this);
        recyclerView.setAdapter(adapter);
        show_likes();
    }

    private void show_likes()
    {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Likes").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users_info");
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    final Boolean key = snapshot.getValue(Boolean.class);
                    String id= String.valueOf(snapshot.getRef().getKey());
                    Log.e("keyyyyyyyyyy",key+id+"");
               //     String pubid=snapshot.child("publisher").getValue(String.class);
                    myRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String img=snapshot.child("Profile_img_Download_link").getValue(String.class);
                            String name=snapshot.child("User_ID").getValue(String.class);
                    //        Log.e("IIIIIIIIMMMMMM",img+"                   "+name+"");
//                            databaseReference.child(key).child("profile").setValue(img);
//                            databaseReference.child(key).child("Name").setValue(name);

                         post_list  =snapshot.getValue(Like_list.class);
                            assert post_list != null;
                            Log.e("IDDDDDDDDDD",post_list.getUser_ID()+"   "+post_list.getProfile_img_Download_link());
                            lists.add(post_list);
                            adapter = new Likes_Adapter(lists, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });


                }

              //    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}