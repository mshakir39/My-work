package com.example.poetrious.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.poetrious.Fragment.Profile_Frag;
import com.example.poetrious.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Drawer extends AppCompatActivity {
    DrawerLayout dLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_info");
ImageView menu,image;View hView;
TextView nav_namee,nav_numm;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        menu=findViewById(R.id.img_menu);
        setNavigationDrawer(); // call method
image=hView.findViewById(R.id.nav_img);
nav_namee=hView.findViewById(R.id.nav_name);
nav_numm=hView.findViewById(R.id.nav_num);

        getSupportFragmentManager().beginTransaction().add(R.id.frame, new Profile_Frag()).commit();
menu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dLayout.openDrawer(GravityCompat.START);
    }
});
        String user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        myRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String value = dataSnapshot.child("Profile_img_Download_link").getValue(String.class);
                String name = dataSnapshot.child("User_Name").getValue(String.class);
                String num = dataSnapshot.child("User_Number").getValue(String.class);

                if(value!=null)

                {
                    Uri myUri = Uri.parse(value);
                    nav_namee.setText(name);
                    nav_numm.setText(num);
                    Picasso.with(Drawer.this).load(myUri).rotate(90f).into(image);
                }





                //   Log.d("myUri", myUri.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = findViewById(R.id.nav);
      hView   =  navView.getHeaderView(0);// initiate a Navigation View
        navView.setItemIconTintList(null);
        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id

                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.profile) {
                    frag = new Profile_Frag();
                }
                else if(itemId==R.id.logout)
                {
                    progressDialog = new ProgressDialog(Drawer.this);
                    progressDialog.setTitle("Logging Out .....");
                    progressDialog.show();
                    SharedPreferences login =getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    login.edit().clear().apply();

                    SharedPreferences prof =getSharedPreferences("check", Context.MODE_PRIVATE);
                    prof.edit().clear().apply();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Drawer.this, Login.class));
                            startActivity(new Intent(Drawer.this, Login.class));
                            finish();

                        }
                    }, 3000);
                }
                // display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Drawer.this,MainActivity.class));
        Animatoo.animateSlideRight(Drawer.this);
        finish();
    }
}
