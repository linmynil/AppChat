package com.example.androidfirstproject.Views.NavigationViews;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfirstproject.Adapters.ChatRoomAdapter;
import com.example.androidfirstproject.Models.ChatRoom;
import com.example.androidfirstproject.Models.Message;
import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RoomChatActivity extends AppCompatActivity {
    private DatabaseReference mDatabase,nDatabase;
    ImageView back;
    TextView tvNameUser2;
    EditText input_message;
    ImageButton sendMessage;
    private String currentUserID;
    private String phoneUser2, idChatRoom;
    private ArrayList<String> listMessId;
    private ArrayList<Message> listMessagesChatRoom ;
    RecyclerView lvListChatRoom;
    String messageID, currentUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        tvNameUser2 = findViewById(R.id.tvNameUser2);
        input_message = findViewById(R.id.input_message);
        sendMessage = findViewById(R.id.sendMessage);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //recyclerView
        lvListChatRoom = findViewById(R.id.lvRoomChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        lvListChatRoom.setLayoutManager(linearLayoutManager);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = user.getUid();
        listMessId = new ArrayList<String>();
        listMessagesChatRoom = new ArrayList<Message>();

        // Lay ChatRoom truyen tu PhoneActivity
        ChatRoom chatRoom;
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            chatRoom = intent.getSerializableExtra("chatRoom", ChatRoom.class);
        } else {
            chatRoom = (ChatRoom) intent.getSerializableExtra("chatRoom");
        }
         idChatRoom = chatRoom.getId();
        Log.d(">>>>>>>TAG","chatroomid"+idChatRoom);

        getCurrentUser(chatRoom, new OnCompleteCallbackUserPhone() {
            @Override
            public void onComplete(User phoneUser) {
                loadData();
            }
        });
//        scrollToBottom(lvListChatRoom);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String inputMessage = String.valueOf(input_message.getText());
              String currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
              Message message = createMessage(phoneUser2,inputMessage,currentTime,idChatRoom);
              input_message.setText("");

              loadData();

              Toast.makeText(RoomChatActivity.this, "Tin nhắn đã gửi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        listMessagesChatRoom.clear();
//        loadData();
    }

    private User checkPhoneUser2(String phoneUser2) {
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot data : task.getResult().getChildren()) {
                    try {
                        User user = data.getValue(User.class);
                        if (phoneUser2.equals(user.getPhoneNumber())) {
                            tvNameUser2.setText(user.getFullName());
                            break;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error: " + e.getMessage());
                    }
                }
            }
        });
        return null;
    }

    public void loadData() {
        listMessagesChatRoom.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot data : task.getResult().getChildren()) {
                    try {
                        Message message = data.getValue(Message.class);
                        message.setId(data.getKey());
                        if (idChatRoom.equals(message.getChatRoomId())) {
                            messageID = message.getId();
                            message.setPhoneUser2(phoneUser2);
                            if (!listMessId.contains(messageID)) {
                                listMessId.add(messageID);
                            }
                            DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference("chatRoom");
                            nDatabase.child(idChatRoom + "/messageList").setValue(listMessId);

                        }
                    } catch (Exception e) {
                        Log.d(">TAG", ""+ e.getMessage());
                        Toast.makeText(getApplicationContext(), "Error send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
//                reload();
                nDatabase = FirebaseDatabase.getInstance().getReference("chatRoom").child(idChatRoom);
                if (!listMessId.isEmpty()) {
                    String idLastMessage = listMessId.get(listMessId.size() - 1);
                    nDatabase.child("lastMessageId").setValue(idLastMessage);
                }

            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(getApplicationContext(), "New message", Toast.LENGTH_SHORT).show();
                listMessagesChatRoom.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    message.setId(data.getKey());
                    if (idChatRoom.equals(message.getChatRoomId())) {
                        messageID = message.getId();
                        listMessagesChatRoom.add(message);
                        ChatRoomAdapter adapter = new ChatRoomAdapter(listMessagesChatRoom, RoomChatActivity.this, currentUserPhone);
                        lvListChatRoom.setAdapter(adapter);
                        scrollToBottom(lvListChatRoom);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Message createMessage(String phoneUser2, String inputMessage, String currentTime, String idChatRoom ){
        mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        String messageId = mDatabase.push().getKey();
        Message message = new Message(inputMessage,currentTime,idChatRoom,currentUserPhone,phoneUser2);
        mDatabase.child(messageId).setValue(message);
        return message;
    }

    public void reload(){
        getListChatRoom();

        mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String id : listMessId) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getKey().equals(id.trim())) {
                            try {
                                Message message = data.getValue(Message.class);
                                message.setId(data.getKey());
                                listMessagesChatRoom.add(message);
                                Log.d(">>>>>>>>>>>>>>>>>TAG",""+message.getPhoneUser2());
                            } catch (Exception e) {
                                Log.d(TAG, "Exception: " + e.getMessage());
                            }
                        }
                    }
                    ChatRoomAdapter adapter = new ChatRoomAdapter(listMessagesChatRoom, RoomChatActivity.this, currentUserPhone);
                    lvListChatRoom.setAdapter(adapter);
                    scrollToBottom(lvListChatRoom);
                    Log.d(TAG, "currentUserPhone: " + currentUserPhone);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void getCurrentUser(ChatRoom chatRoom, OnCompleteCallbackUserPhone callback){
        mDatabase = FirebaseDatabase.getInstance().getReference("user").child(currentUserID);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    User user = task.getResult().getValue(User.class);
                    currentUserPhone = user.getPhoneNumber();
                    callback.onComplete(user);
                    Log.d(TAG, "current: " + currentUserPhone);
                    if (chatRoom != null) {
                        for (String phone : chatRoom.getUserPhoneNumber()) {
                            if (!phone.equals(currentUserPhone)) {
                                phoneUser2 = phone;
                                Log.d(">>TAG","phoneuser2roomchat"+phoneUser2);
                                break;
                            }
                        }
                        checkPhoneUser2(phoneUser2);
                    }
                }
            }
        });
    }

    public void getListChatRoom(){
        mDatabase = FirebaseDatabase.getInstance().getReference("chatRoom").child(idChatRoom);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                listMessId = chatRoom.getMessageList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void scrollToBottom(final RecyclerView recyclerView) {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);
                }
            }
        });
    }



}