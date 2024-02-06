package com.mycode.chatapplication.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mycode.chatapplication.R;
import com.mycode.chatapplication.adapter.ChatAdapter;
import com.mycode.chatapplication.databinding.ActivityChatBinding;
import com.mycode.chatapplication.model.ChatMessage;
import com.mycode.chatapplication.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding chatBinding;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private MyViewModel myViewModel;
    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatBinding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = chatBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //getting the group name from the click item in the group activity
        String groupName =getIntent().getStringExtra("GroupName");
        myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messageList = new ArrayList<>();
                messageList.addAll(chatMessages);
                chatAdapter = new ChatAdapter(messageList,getApplicationContext());
                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();

                //scroll to  the latest message added
                int latestPos = chatAdapter.getItemCount()-1;
                if(latestPos>0) {
                    recyclerView.smoothScrollToPosition(latestPos);
                }
            }
        });
        chatBinding.setVModel(myViewModel);
        chatBinding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = chatBinding.editTextChat.getText().toString();
                myViewModel.sendMessage(msg,groupName);
                chatBinding.editTextChat.getText().clear();
            }
        });
    }
}