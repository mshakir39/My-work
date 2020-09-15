package com.example.poetrious.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.poetrious.Adapters.Message_Adapter;
import com.example.poetrious.Models.Chat;
import com.example.poetrious.Notification.Client;
import com.example.poetrious.Notification.Token;
import com.example.poetrious.R;
import com.example.poetrious.Services.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ChatActivity extends AppCompatActivity {
    ImageView Image, send;
    TextView Name, text;
    private APIService apiService;

    String namme;
    Message_Adapter message_adapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    Intent intent;

    ValueEventListener seenListener;
boolean notify=false;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Image = findViewById(R.id.profile_image);
        send = findViewById(R.id.send_C);
        Name = findViewById(R.id.user_name);
        text = findViewById(R.id.Message);
        intent = getIntent();
            userid = intent.getStringExtra("userid");
apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.C_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        String currentusr_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final String sessionId = getIntent().getStringExtra("usr_id");
        //  Log.e("IDDDDDDDDDD",sessionId+"");
        readmsg(currentusr_id, sessionId);
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users_info");
        final DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("Users_info").child(sessionId);
        usernamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("Profile_img_Download_link").getValue(String.class);
                String name = snapshot.child("User_Name").getValue(String.class);
                Log.e("URI", image + "");
                Uri profile = Uri.parse(image);
                Glide.with(getApplicationContext()).load(profile).into(Image);
                //    user_img.setImageURI(Uri.parse(image));
                namme = name;
                Name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                if (text.getText().toString().trim().equals("") && text.getText().toString().trim().length() < 1) {
                    Toast.makeText(ChatActivity.this, "Cant Send Empty Message !", Toast.LENGTH_SHORT).show();
                } else {
//                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(sessionId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            String usertoken=dataSnapshot.getValue(String.class);
//                            sendNotifications(FirebaseAuth.getInstance().getCurrentUser().getUid(), "Poetrious",text.getText().toString().trim());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });


//                    TOPIC = "/topics/"+sessionId; //topic must match with what the receiver subscribed to
//                    NOTIFICATION_TITLE = namme;
//                    NOTIFICATION_MESSAGE = text.getText().toString().trim();
//
//                    JSONObject notification = new JSONObject();
//                    JSONObject notifcationBody = new JSONObject();
//                    try {
//                        notifcationBody.put("title", NOTIFICATION_TITLE);
//                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
//
//                        notification.put("to", TOPIC);
//                        notification.put("data", notifcationBody);
//                    } catch (JSONException e) {
//                        Log.e(TAG, "onCreate: " + e.getMessage() );
//                    }
//                    sendNotification(notification);
//

                    sendmsg(text.getText().toString().trim(), FirebaseAuth.getInstance().getCurrentUser().getUid(), sessionId);
                    text.setText("");

//                   final String msg=text.getText().toString();
//ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_Data").addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//String name=snapshot.child("User_Name").getValue(String.class);
//if(notify)
//{
//    sendNotification(sessionId,name,msg);
//}
//
//notify=false;
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//});
                }

            }
        });
        //   UpdateToken();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    //    private void UpdateToken(){
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        String refreshToken= FirebaseInstanceId.getInstance().getToken();
//        Token token= new Token(refreshToken);
//        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
//    }
//    public void sendNotifications(String usertoken, String title, String message) {
//        Data data = new Data(title, message);
//        NotificationSender sender = new NotificationSender(data, usertoken);
//        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
//            @Override
//            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                if (response.code() == 200) {
//                    if (response.body().success != 1) {
//                        Toast.makeText(ChatActivity.this, "Failed ", Toast.LENGTH_LONG);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse> call, Throwable t) {
//
//            }
//        });
//    }
//private void sendNotification(JSONObject notification) {
//    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.i(TAG, "onResponse: " + response.toString());
////                    edtTitle.setText("");
////                    edtMessage.setText("");
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(ChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
//                    Log.i(TAG, "onErrorResponse: Didn't work");
//                }
//            }){
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//            params.put("Authorization", serverKey);
//            params.put("Content-Type", contentType);
//            return params;
//        }
//    };
//    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//}
    private void updateToken(String token)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);

    }

//    private void sendNotification(String receiver, final String username, final String message){
//        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = tokens.orderByKey().equalTo(receiver);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Token token = snapshot.getValue(Token.class);
//                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",
//                          userid  );
//
//                    Sender sender = new Sender(data, token.getToken());
//
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<MyResponse>() {
//                                @Override
//                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    if (response.code() == 200){
//                                        if (response.body().success != 1){
//                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyResponse> call, Throwable t) {
//
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
    private void sendmsg(String msg, String sender, String receiver) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", msg);
        databaseReference.child("Chats").push().setValue(hashMap);

    }

    private void readmsg(final String myid, final String userid) {
        mchat = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Chat chat = dataSnapshot.getValue(Chat.class);
                    chat.setMsg(String.valueOf(dataSnapshot.child("message").getValue()));
                    chat.setSender(String.valueOf(dataSnapshot.child("sender").getValue()));
                    chat.setReceiver(String.valueOf(dataSnapshot.child("receiver").getValue()));
                    Log.e("Chaaaat", Objects.requireNonNull(dataSnapshot.child("message").getValue()).toString());
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {

                        mchat.add(chat);
                    }
                    message_adapter = new Message_Adapter(ChatActivity.this, mchat);
                    recyclerView.setAdapter(message_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}