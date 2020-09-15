package com.example.poetrious.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.poetrious.Fragment.FollowFragment;
import com.example.poetrious.Fragment.HomeFragment;
import com.example.poetrious.Fragment.NotificationFragment;
import com.example.poetrious.Fragment.SearchFragment;
import com.example.poetrious.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Dialog dialog;
    Toolbar toolbarr;
    Fragment selectedfragment = null;

    ImageView imageView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_info");

    @Override
    public void onBackPressed() {

        dialog.setContentView(R.layout.exit);

        TextView YES = dialog.findViewById(R.id.Yes);
        TextView NO = dialog.findViewById(R.id.No);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        imageView = findViewById(R.id.user_img);
        toolbarr = findViewById(R.id.toolbar);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.White));
//        }
//Add_Profile.progress.dismiss();
//Add_Profile.progress.cancel();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        selectedfragment = new HomeFragment();

                        break;
                    case R.id.search:
                        selectedfragment = new SearchFragment();

                        break;
                    case R.id.post:
                        selectedfragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        finish();

                        break;
                    case R.id.notification:

                        selectedfragment = new NotificationFragment();
                        break;
                    case R.id.favorite:
                        selectedfragment = new FollowFragment();


                        break;


                }
                if (selectedfragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
                }

                return true;
            }
        });
        String user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        myRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String value = dataSnapshot.child("Profile_img_Download_link").getValue(String.class);
                Log.e("Prof", value + "");
                if (value != null) {
                    Uri myUri = Uri.parse(value);
                    Picasso.with(MainActivity.this).load(myUri).into(imageView);
                }


                //   Log.d("myUri", myUri.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainActivity.this, Drawer.class));
                Animatoo.animateSlideLeft(MainActivity.this);
                finish();


            }
        });


    }
}
