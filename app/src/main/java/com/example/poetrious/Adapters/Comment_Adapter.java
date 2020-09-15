package com.example.poetrious.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Activities.Users_profile;
import com.example.poetrious.Models.Comment_list;
import com.example.poetrious.R;
import com.example.poetrious.RecyclerViewClickInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.C_Viewholder>  {
int POSITION;

private RecyclerViewClickInterface recyclerViewClickInterface;



    String Postid;
Context contextt;

    public List<Comment_list> mpost;




    public Comment_Adapter(List<Comment_list> mpost, Context mcontext,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.mpost = mpost;
        this.mcontext = mcontext;
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }

    public Context mcontext;



    @NonNull
    @Override
    public C_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.comment_model, parent, false);

        return new Comment_Adapter.C_Viewholder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final C_Viewholder holder, int position) {
        final Comment_list comment_list = mpost.get(position);
        holder.profile.setTag(comment_list.getPublisher());
        holder.Name.setTag(comment_list.getPostId());

        if (comment_list.getComment() != null) {
            Log.d("comments", Objects.requireNonNull(comment_list.getComment()));


        } else {

            Log.d("comments", Objects.requireNonNull(comment_list.getComment()));
        }


        Uri profile = Uri.parse(comment_list.getProfile());
        Picasso.with(mcontext).load(profile).into(holder.profile);
        holder.Name.setText(comment_list.getName());
        holder.comments.setText(comment_list.getComment());
  //  holder.comment.setTag(comment_list.getCommentId());


        String id = comment_list.getCommentId();

        Intent intent = new Intent("message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));

        intent.putExtra("id",id);
        LocalBroadcastManager.getInstance(mcontext).sendBroadcast(intent);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            String timeAgo = Comment_Adapter.TimeAgo.getTimeAgo(Long.parseLong(comment_list.getTime()));

                            holder.time.setText(timeAgo);
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 500,60000);
      //  holder.time.setText(getdate(comment_list.getTime()));

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
//
//        databaseReference.child("comment_activity.id").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.child("usr_id").getValue()==FirebaseAuth.getInstance().getCurrentUser().getUid())
//                {
//
//                    Log.e("qwerty", snapshot.getKey() + "");
//                    exist = true;
//
//
//
//                }
//                else {
//                    exist = false;
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext.getApplicationContext(), Users_profile.class);
                intent.putExtra("usr_id", comment_list.getPublisher());
                Log.e("uid", comment_list.getPublisher() + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Log.e("Folllllow",show_follow_list.getUser_ID());
                mcontext.startActivity(intent);
            }
        });
        // getuserInfo(holder.profile,holder.comments,comment_list.getPublisher());

    }
//
//    public String getdate(String timestamp) {
//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(Long.parseLong(timestamp) * 1000);
//        String date = DateFormat.format("dd-MM-yyyy hh:mm", calendar).toString();
//        return date;
//    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class C_Viewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView profile, liker, comment, favorite, post_pic;
        TextView Name, category, time, likes, comments, description;

        C_Viewholder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            time = itemView.findViewById(R.id.timestamp);
            profile = itemView.findViewById(R.id.C_Profile_image);
            Name = itemView.findViewById(R.id.C_user_name);
            comments = itemView.findViewById(R.id.C_text);

        }


        public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final String[] exist = {"0"};
            POSITION=getAdapterPosition();
            recyclerViewClickInterface.onItemClick(getAdapterPosition());
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Post");
            databaseReference.child(Name.getTag().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("Outer"," "+POSITION);
                    String uid=snapshot.child("usr_id").getValue(String.class);
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(uid))
                    {
                        exist[0] ="1";
                        Toast.makeText(mcontext, "Exist"+exist[0]+" User", Toast.LENGTH_SHORT).show();
                        Log.e("IDD", exist[0]+"Exist User   |||");


                    }
                    else
                    {
                        exist[0]="0";
                        Log.e("ID", exist[0]+"Not Exist    |||");

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.e("PostCheck", exist[0] + ""+Name.getTag());
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(profile.getTag())) {
                menu.add(0, 0, 0, "Delete");//groupId, itemId, order, title

            }
            else  {


if(exist[0].equals("1"))
{
    menu.add(0, 0, 0, "Delete");//groupId, itemId, order, title
    Toast.makeText(mcontext, "Exist"+exist[0]+" True", Toast.LENGTH_SHORT).show();

}
else

{
    Toast.makeText(mcontext, "Exist"+exist[0]+" False", Toast.LENGTH_SHORT).show();
}

            }


            //  menu.add(0, v.getId(), 0, "SMS");
        }



    }

    //    private void getuserInfo(final ImageView imageView, final TextView textView, final String publisherid)
//    {
//
//
//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users_info").child(publisherid);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//                {
//                 //   Post_List p=dataSnapshot1.getValue(Post_List.class);
//String image=dataSnapshot1.child("Profile_img_Download_link").getValue(String.class);
//String Name=dataSnapshot1.child("User_Name").getValue(String.class);
//
//
//                 //   Picasso.with(mcontext).load(image).rotate(90f).into(imageView);
//                   // textView.setText(Name);
//                    Log.e("imgggg",image+"");
//                    Log.e("imgggg",Name+"");
//                    Log.e("imgggg",publisherid+"");
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//   }
    public void remove_comment() {
        Comment_list comment_list=mpost.get(POSITION);
Log.e("inner_"," "+POSITION);
        Intent intent = new Intent("custom-message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("position",POSITION);

        LocalBroadcastManager.getInstance(mcontext).sendBroadcast(intent);
//        mpost.remove(POSITION);
//        notifyItemRemoved(POSITION);



//        intent.putExtra("size",getItemCount());
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Comments").child(comment_list.getPostId());
        databaseReference.child(comment_list.getCommentId()).removeValue();
        //  notifyItemRangeChanged( position, mpost.size()-1 );
    }
    public void remove_from_database()
    {

      //  final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Comments").child(postid);
    }

    public static class TimeAgo {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public static String getTimeAgo(long time) {
            if (time < 1000000000000L) {
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }


            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        }

    }


}
