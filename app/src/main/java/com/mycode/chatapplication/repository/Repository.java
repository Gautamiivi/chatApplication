package com.mycode.chatapplication.repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycode.chatapplication.model.ChatGroup;
import com.mycode.chatapplication.model.ChatMessage;
import com.mycode.chatapplication.views.GroupsActivity;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    //its acts like a bridge b/w viewModel and the dataSource
    MutableLiveData<List<ChatGroup>> mutableLiveData ;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference groupReference;
    MutableLiveData<List<ChatMessage>> messageLiveData;
    public MutableLiveData<List<ChatGroup>> getMutableLiveData() {
        List<ChatGroup> groupList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatGroup group =  new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);
                }
                mutableLiveData.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mutableLiveData;
    }

    public Repository() {
        this.mutableLiveData = new MutableLiveData<List<ChatGroup>>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        messageLiveData = new MutableLiveData<>();
    }

    public void fireBaseAnonymousAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i =new Intent(context,GroupsActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                });
    }

    //getting the current userid;
    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
   //signOut functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
    //creating new group
    public void createNewChatGroup(String groupName){
        databaseReference.
                child(groupName).setValue(groupName);
    }

    //getting message (live data)
    public MutableLiveData<List<ChatMessage>> getMessageLiveData(String groupName) {
        groupReference = database.getReference().child(groupName);

        List<ChatMessage> messageList = new ArrayList<>();

        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                messageLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messageLiveData;
    }
    public void sendMessage(String message ,String chatGroup){
        DatabaseReference ref = database.getReference(chatGroup);

        if(!message.trim().equals("")) {
            ChatMessage msg = new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), message,
                    System.currentTimeMillis());

            String randomKey = ref.push().getKey();
            ref.child(randomKey).setValue(msg);
        }
    }
}
