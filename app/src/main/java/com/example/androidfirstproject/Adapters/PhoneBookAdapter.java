package com.example.androidfirstproject.Adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import java.util.ArrayList;
import java.util.List;

public class PhoneBookAdapter extends BaseAdapter {

    private List<User> listUser;
    private Context context;
    private ArrayList<String> phoneBookUserId;

    public PhoneBookAdapter(List<User> listUser, Context context, ArrayList<String> phoneBookUserId) {
        this.context = context;
        this.listUser = listUser;
        this.phoneBookUserId = phoneBookUserId;
    }


    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int _i, View _view, ViewGroup _viewGroup) {
        View view = _view;

        ImageView imageUser = null;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.item_phonebook, null);
            ImageView delete = view.findViewById(R.id.deletePhoneBook);
            TextView nameUser = view.findViewById(R.id.nameUser);

            PhoneBookAdapter.ViewHolder holder = new PhoneBookAdapter.ViewHolder(nameUser,  delete, imageUser);
            view.setTag(holder);


        }
        User user = (User) getItem(_i);

        PhoneBookAdapter.ViewHolder holder = (PhoneBookAdapter.ViewHolder) view.getTag();
        holder.nameUser.setText(user.getFullName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = user.getId();
                phoneBookUserId.remove(id);
                IAdapterClickEvent iAdapterClickEvent = (IAdapterClickEvent) _viewGroup.getContext();
                iAdapterClickEvent.onDeleteClick(phoneBookUserId);
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView nameUser;
        ImageView  delete, imageUser;

        public ViewHolder(TextView nameUser, ImageView delete, ImageView imageUser) {
            this.nameUser = nameUser;
            this.delete = delete;
            this.imageUser = imageUser;
        }
    }
}
