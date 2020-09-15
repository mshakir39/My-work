package com.example.poetrious.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.List;


public class HomeFragment extends Fragment {
    ImageView imageView;
Boolean IsScrolling=false;
ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<Post_List> lists;
    private RecyclerView.Adapter adapter;
    int currentItems,scrollOutItems,totalItems;
    public HomeFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final   FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users_info");

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
imageView=view.findViewById(R.id.post_image);
progressBar=view.findViewById(R.id.progress);
        recyclerView =view. findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
 final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);

        mLayoutManager.setStackFromEnd(true);
// Set the layout manager to your recyclerview
        recyclerView.setLayoutManager(mLayoutManager);

//recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//    @Override
//    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//        super.onScrollStateChanged(recyclerView, newState);
//        if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//        {
//            IsScrolling=true;
//        }
//    }
//
//    @Override
//    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//        currentItems=mLayoutManager.getChildCount();
//        totalItems=mLayoutManager.getItemCount();
//        scrollOutItems=mLayoutManager.findFirstVisibleItemPosition();
//            if(IsScrolling&&currentItems+scrollOutItems==totalItems)
//            {
//                IsScrolling=false;
//                progressBar.setVisibility(VISIBLE);
//                background background=new background();
//                background.start();
//                progressBar.setVisibility(GONE);
//            }
//    }
//});




        lists = new ArrayList<>();

background background=new background();
background.start();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Post");
databaseReference.keepSynced(true);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users_info");
        databaseReference1.keepSynced(true);
        return view;

    }


    class background extends Thread
    {

        @Override
        public void run() {
            super.run();

//
          DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Post");
//            FirebaseRecyclerOptions<Post_List> options=
//                    new FirebaseRecyclerOptions.Builder<Post_List>()
//                    .setQuery(databaseReference,Post_List.class)
//                    .build();
//            adapter=new PostAdapter(options);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lists.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {

                            Post_List p=dataSnapshot1.getValue(Post_List.class);
                        Log.e("Post",p.toString());
                            lists.add(p);




                    }

                    adapter = new PostAdapter(getContext(), lists);
                  recyclerView.setAdapter(adapter);
                    adapter.notifyItemInserted(lists.size()-1);
                    adapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });








        }
    }

}
