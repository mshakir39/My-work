package com.example.poetrious.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.poetrious.Adapters.Pager_Adapter;
import com.example.poetrious.Fragment.Followers;
import com.example.poetrious.Fragment.Following;
import com.example.poetrious.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Follower_Following extends AppCompatActivity implements Followers.OnFragmentInteractionListener, Following.OnFragmentInteractionListener {

String sessionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower__following);
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        sessionId = Objects.requireNonNull(getIntent().getExtras()).getString("usr_id");
        Log.e("Sessio_id",sessionId+"");
        int page= Objects.requireNonNull(getIntent().getExtras()).getInt("One");

   tabLayout.addTab(tabLayout.newTab().setText("Followers"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TabLayout.Tab tab = tabLayout.getTabAt(getIntent().getIntExtra("One",0));
        if (tab != null) {
            tab.select();
        }
      Followers followers=new Followers(sessionId);
     //   followers.getuser(sessionId);
      //  Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
    //    intent.putExtra("uid", sessionId);
      //  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        final ViewPager viewPager=(ViewPager) findViewById(R.id.pager);
        Pager_Adapter pager_adapter=new Pager_Adapter(getSupportFragmentManager(),tabLayout.getTabCount());




        viewPager.setAdapter(pager_adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(page);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

public  void show()
    {

    }

    @Override
    public void onFragmentInteractionHome(Uri uri) {

    }
}