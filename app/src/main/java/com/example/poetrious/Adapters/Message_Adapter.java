package com.example.poetrious.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetrious.Models.Chat;
import com.example.poetrious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MsgViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private String [] data;
    public Context context;
    public List<Chat> mchat;
    FirebaseUser firebaseUser;
    public Message_Adapter(Context mcontext, List<Chat> mpost)
    {
        this.context = mcontext;
        this.mchat = mpost;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT)
        {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_item_right,parent,false);
            return new MsgViewHolder(view);
        }
        else
        {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_item_left,parent,false);
            return new MsgViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        final Chat chat=mchat.get(position);

        holder.MSG.setText(chat.getMsg());





    }
    @Override
    public int getItemViewType(int position) {

     firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
     if(mchat.get(position).getSender().equals(firebaseUser.getUid()))
     {
         return MSG_TYPE_RIGHT;
     }else
     {
         return MSG_TYPE_LEFT;
     }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder
    {
   TextView MSG;


        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            MSG=itemView.findViewById(R.id.show_message);




        }
    }




}
