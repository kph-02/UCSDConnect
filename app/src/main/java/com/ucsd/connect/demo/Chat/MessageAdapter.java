package com.ucsd.connect.demo.Chat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ucsd.connect.demo.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    ArrayList<MessageObject> messageList;
    Map<String, String> uidToName;
    FirebaseStorage firebaseStorage;
    DatabaseReference mUserDb;

    public MessageAdapter(ArrayList<MessageObject> messageList, Map<String, String> uidToName){
        this.messageList = messageList;
        this.uidToName = uidToName;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        MessageViewHolder rcv = new MessageViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        firebaseStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(messageList.get(position).getSenderId()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.mImage);
            }
        });
        holder.mMessage.setText(messageList.get(position).getMessage());


        holder.mSender.setText(uidToName.get(messageList.get(position).getSenderId()));

        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.mViewMedia.setVisibility(View.GONE);

        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }





    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView    mMessage,
                    mSender;
        Button mViewMedia;
        LinearLayout mLayout;
        ImageView mImage;
        MessageViewHolder(View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);

            mMessage = view.findViewById(R.id.message);
            mSender = view.findViewById(R.id.sender);
            mImage = view.findViewById(R.id.ProfilePic);

            mViewMedia = view.findViewById(R.id.viewMedia);

        }
    }
}
