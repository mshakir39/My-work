package com.example.poetrious.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.Comment_Adapter;
import com.example.poetrious.Models.Comment_list;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.example.poetrious.RecyclerViewClickInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Comment_Activity extends AppCompatActivity implements RecyclerViewClickInterface {
    ImageView C_image, C_send;
      public  String id;
      String Curr_Post_Id;
    EditText add_comment;
    String size;
    private RecyclerView recyclerView;
    public List<Comment_list> lists;
    Context context;
    private Comment_Adapter adapter;
     Comment_Adapter adapterr;
    String postid;
    String pub_id;
    String commentId ;
    String position;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("message"));

//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("message"));
        C_image = findViewById(R.id.my_C_image);
        C_send = findViewById(R.id.send_C);
        add_comment = findViewById(R.id.C_comment);
        Intent intent = new Intent();
//        postid=intent.getStringExtra("postid");
//        pub_id=intent.getStringExtra("publisher_id");
//        Log.d("IDDD",postid+"");

        Bundle bundle = getIntent().getExtras();
        Curr_Post_Id = Objects.requireNonNull(bundle).getString("post_id");
//Extract the data…
        postid = Objects.requireNonNull(bundle).getString("post_id");
        pub_id = bundle.getString("publisher_id");
        Log.e("Curr", Curr_Post_Id + "");
id=postid;

        recyclerView = findViewById(R.id.C_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(Comment_Activity.this));
        lists = new ArrayList<>();
        adapter = new Comment_Adapter(lists, Comment_Activity.this,this);
      //  adapterr=new Comment_Adapter(postid,Comment_Activity.this);




        C_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_comment.getText().toString().trim().equals("")) {
                    Toast.makeText(Comment_Activity.this, "You can't send empty Comment", Toast.LENGTH_SHORT).show();
                } else {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users_info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Post_List post_list = dataSnapshot.getValue(Post_List.class);


                            String pushkey = databaseReference.push().getKey();
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("comment", add_comment.getText().toString().trim());
                            hashMap.put("Name", Objects.requireNonNull(post_list).getUser_Name());
                            hashMap.put("PostId", postid);
                            hashMap.put("commentId", pushkey);
                            hashMap.put("profile", post_list.getProfile_img_Download_link());
                            //      Log.d("Namee",post_list.getUser_Name()+post_list.getProfile_img_Download_link()+"");
                            hashMap.put("time", String.valueOf(savetime()));
                            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            assert pushkey != null;
                            databaseReference.child(pushkey).setValue(hashMap);
                            add_comment.setText("");
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //  Addcomment();
                }


            }


        });


        getImage();
        showcomments();

    }

    private Long savetime() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }

    private void showcomments() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users_info");

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String key = snapshot.getKey();
                  //  Log.e("keyyyyyyyyyy", key + "");

                    String pubid = snapshot.child("publisher").getValue(String.class);

                    myRef.child(pubid).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String img = snapshot.child("Profile_img_Download_link").getValue(String.class);
                            String name = snapshot.child("User_Name").getValue(String.class);
                    //        Log.e("IIIIIIIIMMMMMM", img + "");
                            databaseReference.child(key).child("profile").setValue(img);
                            databaseReference.child(key).child("Name").setValue(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Comment_list comment_list = snapshot.getValue(Comment_list.class);

                    lists.add(comment_list);

                }
recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
       // databaseReference.child(commentId).setValue(null);
        adapter.remove_comment();


     //  adapter.notifyItemRangeChanged(, adapter.getItemCount()-1);
        return super.onContextItemSelected(item);
    }

    public void getImage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("Profile_img_Download_link").getValue(String.class);
                Uri myUri = Uri.parse(value);
                Picasso.with(Comment_Activity.this).load(myUri).into(C_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent=new Intent(Comment_Activity.this,MainActivity.class);
//           startActivity(intent);
//           finish();
//        Animatoo.animateZoom(Comment_Activity.this);
    }
//    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String id = intent.getStringExtra("Post_check");
//         Log.e("MYY",id+"                     sssssss");
//            Toast.makeText(Comment_Activity.this,id +" ",Toast.LENGTH_SHORT).show();
//        }
//    };

//    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//             position= intent.getStringExtra("position");
//            size= intent.getStringExtra("size");
//         Log.e("POSITION",position+" "+size);
//        }
//    };

    @Override
    public void onItemClick(int Position) {
        Toast.makeText(Comment_Activity.this, "Comment Activity "+Position, Toast.LENGTH_SHORT).show();
//        adapter.notifyItemRemoved(Position);
//        adapter.notifyItemRangeRemoved(Position-1,Position);

    }

    @Override
    public void onLongItemClick(int Position) {

    }
}
