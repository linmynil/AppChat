package com.example.androidfirstproject.Views.Auth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfirstproject.Views.HomeActivity;
import com.example.androidfirstproject.Views.NavigationViews.MessageActivity;
import com.example.androidfirstproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneInputActivity extends AppCompatActivity {
    TextInputEditText edtPhone;
    Button btnVerify;

    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_input);

        // Ánh xạ
        edtPhone = findViewById(R.id.edtPhone);
        btnVerify = findViewById(R.id.btnVerify);

        // Gửi OTP
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+84" + edtPhone.getText().toString().trim();

                if (TextUtils.isEmpty(edtPhone.getText().toString().trim())) {
                    edtPhone.setError("Phone number is required.");
                    return;
                }

                Intent intent = new Intent(PhoneInputActivity.this, VerifyPhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                intent.putExtra("phonePackage", bundle);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

//         Kiểm tra xem đã có đăng nhập hay chưa, nếu có thì chuyển sang HomeActivity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(PhoneInputActivity.this, MessageActivity.class));
            finish();
        }
    }
}