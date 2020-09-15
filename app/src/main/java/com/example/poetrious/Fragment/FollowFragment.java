package com.example.poetrious.Fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.Follow_Adapter;
import com.example.poetrious.Models.Show_follow_list;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FollowFragment extends Fragment {
    ArrayList<String> arrayList = new ArrayList<String>();

    private RecyclerView recyclerView;
    private List<Show_follow_list> lists;
    int check = 0;
    String copy = "";
    private RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.list_follow);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        lists = new ArrayList<>();


//        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Post");
//        databaseReference.keepSynced(true);
        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (!arrayList.contains(number)) {
                arrayList.add(number);
            }

        }
        //    Objects.requireNonNull(getActivity()).onBackPressed();

        //     Log.i("contacts", String.valueOf(arrayList));
        back back = new back();
        back.start();

        return view;
    }

    class back extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void run() {
            super.run();
            final String[] mynum = new String[1];
            final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info");

            final DatabaseReference usr_num = FirebaseDatabase.getInstance().getReference().child("Users_info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
         usr_num.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 mynum[0] =snapshot.child("User_Number").getValue(String.class);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
            usernamesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String uid = childSnapshot.getKey();
                        // String name = childSnapshot.getValue(String.class);

                        usernamesRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String number = snapshot.child("User_Number").getValue(String.class);
                                if (mynum[0]!=number)
                                {

                                    HashSet hs = new HashSet();
                                    hs.addAll(arrayList);
                                    arrayList.clear();
                                    arrayList.addAll(hs);
                                    int size = arrayList.size();
                                    Log.i("size", String.valueOf(size));
                                    assert number != null;
                                    //  Log.i("numbers", number);
                                    //   lists.clear();
                                    for (int i = 0; i < size; i++) {
                                        String Code = "+92";
                                        String num;
                                        String value = arrayList.get(i);
                                        value = value.replace(" ", "");
                                        Log.i("value", value);
                                        Log.i("value", value);
                                        Log.i("numbersvalue", "   " + value);
                                        char first = value.charAt(0);
                                        StringBuilder sb = new StringBuilder(value);
                                        if (first == '0') {
                                            String str = sb.deleteCharAt(0).toString();
                                            value = Code + str;
                                            //        Log.e("num", value);
                                        }

                                        if (value.equals(number)) {


                                            check++;
                                            if (check == 1) {
                                                // show_follow_list.setProfile_img_Download_link(snapshot.child("Profile_img_Download_link").getValue(String.class));
//                                        show_follow_list.setProfile_img_Download_link(snapshot.child("User_Name").getValue(String.class));
                                                String name = snapshot.child("User_Name").getValue(String.class);
                                                //     Log.e("numbers", number + "           " + name);
                                                //     Log.i("numbers",name+"");
                                                Show_follow_list show_follow_list1 = snapshot.getValue(Show_follow_list.class);
                                                assert show_follow_list1 != null;
                                                Log.i("profimg", show_follow_list1.getProfile_img_Download_link() + "");
                                                lists.add(show_follow_list1);
                                            }

                                            // arrayList.remove(s);
                                        } else {
                                            //   Log.i("numbers","not found");

                                        }
                                    }
                                    check = 0;
                                    Log.e("qwerty", check + "         " + "dfsl;fdssdfsf");

                                    adapter = new Follow_Adapter(getContext(), lists);
                                    recyclerView.setAdapter(adapter);

                                }

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

}

