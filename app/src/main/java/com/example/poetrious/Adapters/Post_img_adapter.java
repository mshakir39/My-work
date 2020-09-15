package com.example.poetrious.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.poetrious.Activities.MyPost;
import com.example.poetrious.Activities.UsersPost;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Post_img_adapter extends RecyclerView.Adapter<Post_img_adapter.viewholder> {
    String img;
    private boolean zoomOut =  false;
    public Context mcontext;
    CoordinatorLayout coordinatorLayout;
    DownloadManager downloadManager;
    public Post_img_adapter(Context mcontext, List<Post_List> mpost) {
        this.mcontext = mcontext;
        this.mpost = mpost;
    }

    public List<Post_List> mpost;

    private FirebaseUser firebaseUser;



    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mcontext).inflate(R.layout.img_item, parent, false);

        return new Post_img_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {

        final Post_List post_list=mpost.get(position);

        img = post_list.getImg_post();


        Log.e("imagee",img+"");


        if(img!= null) {


            final Uri imageUri=Uri.parse(img);
            Glide.with(mcontext).load(imageUri) .into(holder.post_pic);

            //  Picasso.with(mcontext).load(imageUri).rotate(90f).into(holder.post_pic);





        }
        else
        {
            Log.d("NULL",img+"");
        }
        holder.post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post_list.getUsr_id()== FirebaseAuth.getInstance().getCurrentUser().getUid())
                {
                    Intent intent=new Intent(mcontext.getApplicationContext(), MyPost.class);
                    intent.putExtra("pos", position);
                    Log.e("positionAd",""+position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(mcontext.getApplicationContext(), UsersPost.class);
                    intent.putExtra("pos", position);
                    intent.putExtra("usr_id", post_list.getUsr_id());
                    Log.e("positionAd",""+position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);
                }

            }
        });













    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }


    static public class viewholder extends RecyclerView.ViewHolder {
        private ImageView profile, liker, comment, favorite, post_pic,dote;



        public viewholder(@NonNull View itemView) {
            super(itemView);

            post_pic = itemView.findViewById(R.id.img_post);


        }
    }






    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
