package com.example.androidfirstproject.Adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.androidfirstproject.Models.ChatRoom;
import com.example.androidfirstproject.Models.Message;
import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends BaseAdapter {
    private DatabaseReference mDatabase;

    private List<ChatRoom> chatRoomList;
    Context context;
    private User currentUser;
    private Boolean found = false;

    public MessageAdapter(List<ChatRoom> chatRoomList, Context context, User currentUser) {

        this.chatRoomList = chatRoomList;
        this.context = context;
        this.currentUser = currentUser;
    }


    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int _i, View _view, ViewGroup _viewGroup) {
        View view = _view;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.item_message, null);
            TextView nameUser2 = view.findViewById(R.id.nameUser2);
            TextView lastMessage = view.findViewById(R.id.lastMessage);
            TextView time = view.findViewById(R.id.time);
            MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(nameUser2,lastMessage,time);
            view.setTag(holder);
        }
        ChatRoom chatRoom = (ChatRoom)getItem(_i);
        MessageAdapter.ViewHolder holder = (MessageAdapter.ViewHolder) view.getTag();

        getNameUser2(chatRoom,holder);

        getLastMessage(chatRoom,holder);

        return view;
    }

    private static class ViewHolder {
        TextView nameUser2,lastMessage, time ;

        public ViewHolder(TextView nameUser2, TextView lastMessage, TextView time) {
            this.nameUser2 = nameUser2;
            this.lastMessage = lastMessage;
            this.time = time;
        }
    }

    public void getNameUser2(ChatRoom chatRoom, MessageAdapter.ViewHolder holder){
        for (String phone : chatRoom.getUserPhoneNumber()) {
            if (!currentUser.getPhoneNumber().equals(phone)) {
                mDatabase = FirebaseDatabase.getInstance().getReference("user");
                mDatabase.get().addOnCompleteListener(task -> {
                    for (DataSnapshot data : task.getResult().getChildren()) {
                        User user = data.getValue(User.class);
                        if (user.getPhoneNumber().equals(phone)) {
                            holder.nameUser2.setText(user.getFullName());
                            break;
                        }
                    }
                });
            }
        }
    }

    public void getLastMessage(ChatRoom chatRoom, MessageAdapter.ViewHolder holder) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        mDatabase.get().addOnCompleteListener(task -> {
            for (DataSnapshot data : task.getResult().getChildren()) {
                if (data.getKey().equals(chatRoom.getLastMessageId())) {
                    Message message = data.getValue(Message.class);
                    if (message.getContent().length() > 15) {
                        holder.lastMessage.setText(message.getContent().substring(0, 15) + "...");
                    } else {
                        holder.lastMessage.setText(message.getContent());
                    }

                    SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy hh:mm");
                    try {
                        Date date = formatter1.parse(message.getTime());
                        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
                        String ago = prettyTime.format(date);
                        holder.time.setText(ago);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}

