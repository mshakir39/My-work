package com.example.poetrious.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.Followers_Adapter;
import com.example.poetrious.Models.Show_follow_list;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Followers extends Fragment {
    private OnFragmentInteractionListener mListener;
    ArrayList<String> arrayList = new ArrayList<String>();
    String user_id;
    private RecyclerView recyclerView;
    private List<Show_follow_list> lists;
    int check = 0;
    String copy = "";
    private RecyclerView.Adapter adapter;
public Followers (String id)
{
this.user_id=id;
Log.e("IDD",user_id+"");
}
public Followers()
{

}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.onFragmentInteractionHome(Uri.parse("doWhatYouWant"));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_followers, container, false);
        recyclerView = view.findViewById(R.id.list_followers);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        lists = new ArrayList<>();
 // user_id    = getArguments().getString("uid");



        back back = new back();
        back.start();
        return  view;
    }
    public interface OnFragmentInteractionListener {
         void onFragmentInteractionHome(Uri uri);

    }

    class back extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void run() {
            super.run();
            final String[] mynum = new String[1];
            Log.e("receiver", "Got message: " + user_id);
            final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info");

            final DatabaseReference usr_num = FirebaseDatabase.getInstance().getReference().child("Followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            usr_num.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for (DataSnapshot dataSnapshot:snapshot.getChildren())
                  {


                      usernamesRef.child(dataSnapshot.child("user_id").getValue().toString()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              Show_follow_list show_follow_list1 = snapshot.getValue(Show_follow_list.class);
                              Log.e("Followers",snapshot.getValue()+"");
                              lists.add(show_follow_list1);
                              adapter = new Followers_Adapter(getContext(), lists);
                              recyclerView.setAdapter(adapter);
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }

                      });

                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
    public void getuser(String id)
    {
     this.   user_id=id;
        Log.e("ID",user_id+"");
    }
}