package com.example.androidfirstproject.Views.Auth;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import com.example.androidfirstproject.Views.NavigationViews.MessageActivity;
import com.example.androidfirstproject.Views.NavigationViews.PhoneBookActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    private EditText fullNameInput;
    private Button updateBtn;
    private DatabaseReference mDatabase;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        fullNameInput = findViewById(R.id.fullNameInput);
        updateBtn = findViewById(R.id.updateBtn);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("phoneNumberPackage");
        phone = bundle.getString("phone");
        Log.d(TAG, "PhoneNumber: " + phone);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked");
                if (TextUtils.isEmpty(fullNameInput.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Username must not empty", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Phone Number: " + phone);
                    String userId = user.getUid();
                    String profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png";
                    String fullName = fullNameInput.getText().toString();
                    createUser(userId, phone, fullName, profileUrl);
                    startActivity(new Intent(UserInfoActivity.this, MessageActivity.class));
                }
            }
        });
    }

    private void createUser(String userId, String phoneNumber, String fullName, String profilePicture) {
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        ArrayList<String> phoneBook = new ArrayList<String>();
        phoneBook.add("");
        User user = new User(fullName, phoneBook , phoneNumber, profilePicture, userId );
        mDatabase.child(userId).setValue(user);
    }
}