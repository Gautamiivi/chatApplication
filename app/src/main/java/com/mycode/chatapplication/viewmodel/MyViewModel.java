package com.mycode.chatapplication.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mycode.chatapplication.model.ChatGroup;
import com.mycode.chatapplication.model.ChatMessage;
import com.mycode.chatapplication.repository.Repository;

import java.util.List;

public class MyViewModel extends AndroidViewModel {
    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }
    //auth
    public void signUpAnonymousUser(){
        Context c= this.getApplication();
        repository.fireBaseAnonymousAuth(c);
    }

    public void sendMessage(String msg , String chatGroup){
        repository.sendMessage(msg, chatGroup);
    }
    //user id
    public String getCurrentUserId(){
        return repository.getCurrentUserId();
    }
    //sign out
    public void singOut(){
        repository.signOut();
    }

    //getting chat group
    public MutableLiveData<List<ChatGroup>> getGroupList(){
        return repository.getMutableLiveData();
    }
    public void createNewGroup(String groupName){
        repository.createNewChatGroup(groupName);
     }

     //message
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return repository.getMessageLiveData(groupName);
    }
}

