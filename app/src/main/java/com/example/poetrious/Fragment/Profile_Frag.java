package com.example.poetrious.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.poetrious.Activities.EditProfile;
import com.example.poetrious.Activities.Follower_Following;
import com.example.poetrious.Adapters.Post_img_adapter;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.OnBackPressed;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Profile_Frag extends Fragment implements OnBackPressed {
    ImageView imageView, upload_Img;

    TextView count_likes, count_posts, count_followers, count_following,Followers_check,Following_check;

    private static final int PICK_IMAGE = 100;
    TextView prof_name;
    FirebaseStorage storage;

    StorageReference storageReference;


    Button Logout, Edit;
    ImageView imageVieww;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("Users_info");

    RecyclerView recyclerView;
    List<Post_List> lists;
    String user_id;
    RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //      imageView = findViewById(R.id.add_img);
        imageVieww = view.findViewById(R.id.profile_image);
        prof_name = view.findViewById(R.id.main_prof_name);
        count_likes = view.findViewById(R.id.likes);
        Followers_check=view.findViewById(R.id.textView4);
        Following_check=view.findViewById(R.id.textView14);
        count_posts = view.findViewById(R.id.posts);
        count_followers = view.findViewById(R.id.textView12);
        count_following = view.findViewById(R.id.textView13);
        recyclerView = view.findViewById(R.id.list2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Edit = view.findViewById(R.id.edit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        lists = new ArrayList<>();


        count_following();
        count_followers();


        background background = new background();
        background.start();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        databaseReference.keepSynced(true);

        // rank();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("User_Name").getValue(String.class);
                String value = dataSnapshot.child("Profile_img_Download_link").getValue(String.class);
                Uri myUri = Uri.parse(value);
                //         Uri name_uri=Uri.parse(name);


                Picasso.with(getContext()).load(myUri).into(imageVieww);
                prof_name.setText(name);
                Log.d("myUri", myUri.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Followers_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Followers fragment = new Followers();
                Bundle bundle = new Bundle();
                bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                fragment.setArguments(bundle);
                int page = 0;
                Intent intent = new Intent(getContext(),Follower_Following.class);
                intent.putExtra("One", page);
                //  startActivity(new Intent(Users_profile.this,Follower_Following.class));
                startActivity(intent);

            }
        });
        Following_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = 1;
                Intent intent = new Intent(getContext(),Follower_Following.class);
                intent.putExtra("One", page);
                //  startActivity(new Intent(Users_profile.this,Follower_Following.class));
                startActivity(intent);
               // startActivity(new Intent(getContext(), Follower_Following.class));

            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), EditProfile.class));
//                Animatoo.animateSlideLeft(getActivity().getApplicationContext());
                getActivity().finish();

            }
        });

        return view;
    }

    private void count_following() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("COUNt_Child", snapshot.getChildrenCount() + "");
                    String size = String.valueOf(snapshot.getChildrenCount());
                    count_following.setText(size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void count_followers() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Followers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("COUNt_Child_f", snapshot.getChildrenCount() + "");
                    String size = String.valueOf(snapshot.getChildrenCount());
                    count_followers.setText(size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goToAttract(View v) {

    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    class background extends Thread {
        @Override
        public void run() {
            super.run();
            SharedPreferences sp1 = getActivity().getSharedPreferences("UID", 0);
            final String cb1 = sp1.getString("User_id", "false");

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final HashMap<String, String> insert_profile_data = new HashMap<>();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        final int size = (int) dataSnapshot.getChildrenCount();

                        Log.e("Size", size + "");
                        final int[] likes_count = {0};
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Post_List p = dataSnapshot1.getValue(Post_List.class);
                            //     p.setPost_time(Objects.requireNonNull(dataSnapshot1.child("post_time").getValue()).toString());
                            Log.e("Timer123", p.getPost_time() + "" + size);
                            assert p != null;
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(p.getPost_id()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    likes_count[0] = (int) snapshot.getChildrenCount() + likes_count[0];
                                    Log.e("LIkes", likes_count[0] + "");


                                    DatabaseReference profile_ref = database.getReference("Users_profile").child(user_id);


                                    insert_profile_data.put("total_post", String.valueOf(size));
                                    insert_profile_data.put("total_likes", String.valueOf(likes_count[0]));


                                    insert_profile_data.put("follows", "0");
                                    insert_profile_data.put("followers", "0");
                                    insert_profile_data.put("rank", "0");
                                    profile_ref.setValue(insert_profile_data);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                            //  Log.i("postt", String.valueOf(p));
                            lists.add(p);
                        }

                        adapter = new Post_img_adapter(getContext(), lists);
                        recyclerView.setAdapter(adapter);
                    } else {
                        DatabaseReference profile_ref = database.getReference("Users_profile").child(user_id);
                        profile_ref.removeValue();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference profile_ref = database.getReference("Users_profile").child(user_id);
            profile_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        String count_likess = snapshot.child("total_likes").getValue(String.class);
                        String count_postss = snapshot.child("total_post").getValue(String.class);
                        count_likes.setText(count_likess);
                        count_posts.setText(count_postss);
                    } else {
                        count_likes.setText("0");
                        count_posts.setText("0");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }


}


