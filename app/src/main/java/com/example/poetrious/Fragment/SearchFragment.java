package com.example.poetrious.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Adapters.Search_Adapter;
import com.example.poetrious.Models.User_data;
import com.example.poetrious.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment {
     String check ;
    private EditText mSearchField;
    private ImageView mSearchBtn;
    String uid;
    private List<User_data> lists;
    private RecyclerView mResultList;
ValueEventListener valueEventListener;
String name;
    private DatabaseReference mUserDatabase;
    private RecyclerView.Adapter adapter;
Search_Adapter search_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users_info");
        mResultList = (RecyclerView) view.findViewById(R.id.search_list);
      getActivity().  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mResultList.setHasFixedSize(true);
        mResultList.getRecycledViewPool().setMaxRecycledViews(0, 10);
        mResultList.setItemViewCacheSize(10);
      //  mSearchBtn=view.findViewById(R.id.imageView);
        mResultList.setDrawingCacheEnabled(true);
        mResultList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mResultList.setLayoutManager(mLayoutManager);
        mSearchField = (EditText)view. findViewById(R.id.actv);
mResultList.setAdapter(search_adapter);
        lists = new ArrayList<>();



mSearchField.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count==0)
        {
           lists.clear();
check=null;
            mResultList.setAdapter(null);
        }
        else {
            check=null;
            String message = mSearchField.getText().toString();
            message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
            // Will output: My message
            firebaseUserSearch(message);
            Log.e("TEXT",s+"     ");
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});



return  view;
    }

    private void firebaseUserSearch(final String searchText) {






           //         DatabaseReference databaseReference=     mUserDatabase.child(uid);
               //     Toast.makeText(getContext(), "Started Search", Toast.LENGTH_SHORT).show();
Log.e("IDDDD",uid+"");
valueEventListener =new ValueEventListener() {

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        lists.clear();
        if(snapshot.exists()) {

            Log.e("name", Objects.requireNonNull(snapshot.getValue()).toString()+"");
            for(DataSnapshot dataSnapshot:snapshot.getChildren())
            {
                User_data user_data=dataSnapshot.getValue(User_data.class);
                assert user_data != null;
                Log.e("DATA",snapshot.child("User_Name")+"     eegdfg      ");
                lists.add(user_data);
            }

            adapter = new Search_Adapter(getContext(), lists);
            mResultList.setAdapter(adapter);
        }
        else {
            if(check==null)
            {
                Log.e("checkb",check+"");
                firebaseUserSearch(searchText.toLowerCase());
                check="t";
            }
            else
            {
                Toast.makeText(getContext(), "No Result !", Toast.LENGTH_SHORT).show();

            }

        }

   //     search_adapter.notifyDataSetChanged();


    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
};
//                    firebaseRecyclerAdapter.startListening();
//                    mResultList.setAdapter(firebaseRecyclerAdapter);
                  //  DatabaseReference Reference=mUserDatabase.child(uid);
                  Query firebaseSearchQuery = FirebaseDatabase.getInstance().getReference("Users_info").orderByChild("User_Name").startAt(searchText).endAt(searchText+"\uf8ff");
                     //       .endAt(searchText.toLowerCase() + "\uf8ff" );
                    //    Reference.addValueEventListener(valueEventListener);
                    firebaseSearchQuery.addListenerForSingleValueEvent(valueEventListener);

                }

      //

            }










       // startAt(searchText).endAt(searchText + "\uf8ff")






















