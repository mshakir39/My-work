package com.example.poetrious.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.poetrious.Activities.Comment_Activity;
import com.example.poetrious.Activities.Drawer;
import com.example.poetrious.Activities.Image_viewer;
import com.example.poetrious.Activities.LikesActivity;
import com.example.poetrious.Activities.Users_profile;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewholder> {
    String img;
    private boolean zoomOut = false;
    public Context mcontext;
    CoordinatorLayout coordinatorLayout;
    DownloadManager downloadManager;

    public PostAdapter(Context mcontext, List<Post_List> mpost) {
        this.mcontext = mcontext;
        this.mpost = mpost;
    }

    public List<Post_List> mpost;

    private FirebaseUser firebaseUser;


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mcontext).inflate(R.layout.post_item, parent, false);

        return new PostAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {

        final Post_List post_list = mpost.get(position);
        final Post_List post_list2 = mpost.get(position);
        holder.description.setText(post_list.getDescription());
        holder.Name.setText(post_list.getName_usr());
        Log.e("errortime", post_list.getPost_time() + "");
     //  String timer = getdate(post_list.getPost_time());
//        String timeAgo = TimeAgo.getTimeAgo(Long.parseLong(post_list.getPost_time()));
//
//        holder.time.setText(timeAgo);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            String timeAgo = TimeAgo.getTimeAgo(Long.parseLong(post_list.getPost_time()));

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



        holder.post_pic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        holder.post_pic.setAdjustViewBounds(true);

        Log.e("imagee", img + "                ii" + post_list.getDescription() + "   " + post_list.getPost_id());
        String prof = post_list.getPub_pic();
        holder.progressBar.setVisibility(View.GONE);
        if (post_list.getImg_post() != null) {
            img = post_list.getImg_post();
            Log.e("IMGG", img + "                ii" + post_list.getDescription() + "");
            final Uri imageUri = Uri.parse(img);
            Log.e("URI_POST",""+imageUri);
            Glide.with(mcontext).load(imageUri)
//                    .listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.progressBar.setVisibility(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.  progressBar.setVisibility(View.GONE);
//                    return false;
//                }
//            })
                    .into(holder.post_pic);

            //  Picasso.with(mcontext).load(imageUri).rotate(90f).into(holder.post_pic);
      holder.post_pic.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              holder.    post_pic.buildDrawingCache();

              Bundle bundle = new Bundle();

              Intent intent = new Intent(mcontext, Image_viewer.class);
//Add your data to bundle
              bundle.putString("path", String.valueOf(imageUri));



//Add the bundle to the intent
              intent.putExtras(bundle);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Fire that second activity
              mcontext.startActivity(intent);    //Create the bundle
              Animatoo.animateZoom(mcontext);




          }
      });


        } else {
            Log.d("NULL", img + "");
        }

        if (prof != null) {
            Log.d("Checking", prof + "");
            Uri profile = Uri.parse(prof);
            Picasso.with(mcontext).load(profile).into(holder.profile);
            // Glide.with(mcontext).load(profile).into(  holder.profile);

        } else {
            Log.d("NULL", prof + "");
        }

        //    Picasso.with(mcontext).load(post_list.getProfile_img_Download_link()).rotate(90f).into(holder.profile);


        islike(post_list.getPost_id(), holder.liker);
        count_like(holder.likes, post_list.getPost_id());
        comments(post_list.getPost_id(), holder.comments);
        //favotite_posts(post_list.getPost_id(),holder.favorite);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        mcontext, R.style.BottomSheetDialogTheme
                );
                View bottomsheetview = LayoutInflater.from(mcontext)
                        .inflate(
                                R.layout.bootom_sheel_layout,
                                (ViewGroup) bottomSheetDialog.findViewById(R.id.bottom_sheet_container)
                        );
                bottomSheetDialog.setContentView(bottomsheetview);
                bottomSheetDialog.getBehavior().setPeekHeight(1000);
                bottomSheetDialog.show();
            }
        });
        holder.dote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_list.getUsr_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    PopupMenu Meenu = new PopupMenu(mcontext, holder.dote);

                    Meenu.inflate(R.menu.auth_menu);
                    Meenu.show();

                    Meenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.Delete) {
                                final String[] key = new String[1];
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String time = dataSnapshot.child("post_time").getValue(String.class);
                                            if (Objects.equals(time, post_list.getPost_time())) {

                                                key[0] = dataSnapshot.getKey();
                                                DatabaseReference U_Reference = FirebaseDatabase.getInstance().getReference("User_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key[0]);
                                                DatabaseReference P_Reference = FirebaseDatabase.getInstance().getReference("Post").child(post_list.getPost_id());
                                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(post_list.getImg_post());
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // File deleted successfully
                                                        Log.d("TAG", "onSuccess: deleted file");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Uh-oh, an error occurred!
                                                        Log.d("TAG", "onFailure: did not delete file");
                                                    }
                                                });
                                                U_Reference.removeValue();
                                                P_Reference.removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                mpost.remove(position);

                            }
                            return true;
                        }
                    });
                } else {
                    PopupMenu Meenu = new PopupMenu(mcontext, holder.dote);

                    Meenu.inflate(R.menu.dot_menu);
                    Meenu.show();

                    Meenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            return false;
                        }
                    });
                }

            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Comment_Activity.class);
              Post_List post_list1=mpost.get(position);

                Intent intentt = new Intent("message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intentt.putExtra("Post_check",post_list.getPost_id());

                LocalBroadcastManager.getInstance(mcontext).sendBroadcast(intentt);
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("post_id", post_list.getPost_id());
                bundle.putString("publisher_id", post_list.getUsr_id());

//Add the bundle to the intent
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Fire that second activity
                mcontext.startActivity(intent);    //Create the bundle

            }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_list.getUsr_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Intent intent = new Intent(mcontext, Drawer.class);
                    mcontext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mcontext, Users_profile.class);
                    intent.putExtra("usr_id", post_list.getUsr_id());
                    mcontext.startActivity(intent);
                }
            }
        });
        holder.liker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.liker.getTag().equals("Like")) {

                    MediaPlayer mp = MediaPlayer.create(mcontext, R.raw.swiftly);
                    mp.start();


                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_list.getPost_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_list.getPost_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();


                }
            }
        });
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, LikesActivity.class);


                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("postid", post_list.getPost_id());
                bundle.putString("publisher_id", post_list.getUsr_id());
                Log.d("GGG", post_list.getPost_id() +"   ");
//Add the bundle to the intent
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Fire that second activity
                mcontext.startActivity(intent);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Comment_Activity.class);


                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("post_id", post_list.getPost_id());
                bundle.putString("publisher_id", post_list.getUsr_id());
                Log.d("IDDD", post_list.getPost_id() + "");
//Add the bundle to the intent
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Fire that second activity
                mcontext.startActivity(intent);    //Create the bundle


            }
        });


    }

    public String getdate(String timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp) * 1000);
        return DateFormat.format("dd-MM-yyyy hh:mm", calendar).toString();
    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }


    static public class viewholder extends RecyclerView.ViewHolder {
        private ImageView profile, liker, comment, share, post_pic, dote;
        TextView Name, category, time, likes, comments, description;
        ProgressBar progressBar;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.Profile_image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            liker = itemView.findViewById(R.id.Liker);
            comment = itemView.findViewById(R.id.Comment);
            share = itemView.findViewById(R.id.Share);
            Name = itemView.findViewById(R.id.user_name);
            dote = itemView.findViewById(R.id.dot);
            category = itemView.findViewById(R.id.category);
            time = itemView.findViewById(R.id.Time);
            likes = itemView.findViewById(R.id.total_likes);
            comments = itemView.findViewById(R.id.comments);
            post_pic = itemView.findViewById(R.id.post_image);
            description = itemView.findViewById(R.id.img_description);

        }

    }

    private void comments(String postid, final TextView textView) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    textView.setText("");
                } else {
                    textView.setText(dataSnapshot.getChildrenCount() + " Comments");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void islike(String post_id, final ImageView imageView) {


        if (post_id != null) {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Likes")
                    .child(post_id);
            Log.d("PATH", post_id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).exists()) {

                        imageView.setImageResource(R.drawable.like_g);
                        imageView.setTag("Liked");

                    } else {

                        imageView.setImageResource(R.drawable.like_b);
                        imageView.setTag("Like");
                        Log.d("elsetag", "no");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

        }

    }


    public void count_like(final TextView like, String postid) {
        if (postid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Likes")
                    .child(postid);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int s = (int) dataSnapshot.getChildrenCount();
                    if (s == 0) {
                        like.setText("");
                    } else if (s == 1) {
                        like.setText(dataSnapshot.getChildrenCount() + " Like");
                    } else {
                        like.setText(dataSnapshot.getChildrenCount() + " Likes");
                    }


                    Log.d("Likess", dataSnapshot.getChildrenCount() + " Likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


//}
//public void favotite_posts(final String post_id, final ImageView imageView)
//{
//    final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
//    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
//            .child("Favorite_posts")
//            .child(uid);
//
//final Post_List post_list=new Post_List();
//    databaseReference.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//            {
//                String check=dataSnapshot1.child("Fav_post").getValue(String.class);
//                if(check==null)
//                {
//                    imageView.setImageResource(R.drawable.fav_done);
//                    imageView.setTag("Favorite");
//
//Log.d("SetTag","Work");
//
//                }
//                else if(check.equals(post_id))
//                {
//                    imageView.setImageResource(R.drawable.favotite);
//                    imageView.setTag("Fav");
//                    Log.d("elsetag","no");
//                    Log.d("SetTag","nowork");
//                }
//
//            }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
//

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
