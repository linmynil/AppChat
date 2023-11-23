package com.example.androidfirstproject.Adapters;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidfirstproject.Models.Message;
import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT  = 0;
    public static final int MSG_TYPE_RIGHT  = 1;

    private List<Message> listChat;
    private Context context;
    private String currentUserPhone, currentPhoneUser1;
    private DatabaseReference mDatabase;

    public ChatRoomAdapter(List<Message> listChat, Context context, String currentUserPhone) {
        this.context = context;
        this.listChat = listChat;
        this.currentUserPhone = currentUserPhone;
    }


    @NonNull
    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == MSG_TYPE_RIGHT) {
           view = LayoutInflater.from(context).inflate(R.layout.chat_layout_right,parent,false);
        }
        else if (viewType == MSG_TYPE_LEFT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_layout_left,parent,false);
        }
        return new ChatRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ViewHolder holder, int position) {
        Message message = listChat.get(position);
        holder.myMessage.setText(message.getContent());
        holder.myTime.setText(message.getTime());
    }



    @Override
    public int getItemCount() {
        return listChat.size();
    }

//    public void account(){
//        mDatabase = FirebaseDatabase.getInstance().getReference("user").child(currentUserID);
//        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                } else {
//                    User user = task.getResult().getValue(User.class);
//                    currentPhoneUser1 = user.getPhoneNumber();
//                }
//            }
//        });
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public TextView myMessage, myTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myMessage = itemView.findViewById(R.id.myMessage);
            myTime = itemView.findViewById(R.id.myTime);
        }
    }
    @Override
    public int getItemViewType(int position) {
//        account();
        if(listChat.get(position).getPhoneUser1().equals(currentUserPhone)){
            Log.d(">>>>>>>>>>TAG","vitri"+listChat.get(position).getPhoneUser1()+currentPhoneUser1);
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
