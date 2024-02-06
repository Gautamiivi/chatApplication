package com.mycode.chatapplication.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mycode.chatapplication.R;
import com.mycode.chatapplication.adapter.GroupsAdapter;
import com.mycode.chatapplication.databinding.ActivityGroupsBinding;
import com.mycode.chatapplication.model.ChatGroup;
import com.mycode.chatapplication.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {
    private ArrayList<ChatGroup> chatGroupArrayList;
    private GroupsAdapter groupsAdapter;
    private RecyclerView recyclerView;
    private ActivityGroupsBinding activityGroupsBinding;
    private MyViewModel myViewModel;
    private Dialog chatGroupDialog;
    private FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        activityGroupsBinding= DataBindingUtil.setContentView(this,R.layout.activity_groups);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = activityGroupsBinding.recyclerView;
        fabButton = findViewById(R.id.fabButton);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myViewModel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);
                groupsAdapter = new GroupsAdapter(chatGroupArrayList);
                recyclerView.setAdapter(groupsAdapter);
                groupsAdapter.notifyDataSetChanged();
            }
        });
    }
    public void showDialog(){
        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dailog_layout,null);
        chatGroupDialog.setContentView(view);
        chatGroupDialog.show();
        EditText editText = view.findViewById(R.id.editText);
        Button createBtn = view.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(v -> {
            String groupName = editText.getText().toString();
            myViewModel.createNewGroup(groupName);
            chatGroupDialog.dismiss();
        });
    }
}