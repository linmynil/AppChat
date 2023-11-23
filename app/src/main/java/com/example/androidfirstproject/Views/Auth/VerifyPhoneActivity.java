package com.example.androidfirstproject.Views.Auth;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfirstproject.Adapters.PhoneBookAdapter;
import com.example.androidfirstproject.Models.Message;
import com.example.androidfirstproject.Models.User;
import com.example.androidfirstproject.R;
import com.example.androidfirstproject.Views.HomeActivity;
import com.example.androidfirstproject.Views.NavigationViews.MessageActivity;
import com.example.androidfirstproject.Views.NavigationViews.PhoneBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText edtOTP;
    private Button btnResend, btnLogIn;
    private String verificationID, phone;
    private FirebaseAuth rAuth;
    private DatabaseReference mDatabase;
    private Boolean loggedIn;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        edtOTP = findViewById(R.id.edtOTP);
        btnResend = findViewById(R.id.btnResend);
        btnLogIn = findViewById(R.id.btnLogIn);

        rAuth = FirebaseAuth.getInstance();
        loggedIn = false;

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("phonePackage");
        phone = bundle.getString("phone");
        Log.d(ContentValues.TAG, "onCreate: " + phone);


        sendVerificationCode(phone);



        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked");
                String otp = edtOTP.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    edtOTP.setError("Please enter your OTP.");
                    return;
                }

                mDatabase = FirebaseDatabase.getInstance().getReference("test");
                mDatabase.setValue("test");

                verifyCode(otp);
                checkUser(phone);
            }
        });
    }

    // Hàm gửi OTP qua số điện thoại
    private void sendVerificationCode(String phoneNumber) {
        try {
            Log.d(TAG, "sendVerificationCode: " + phoneNumber);
//            FirebaseAuth.getInstance().getFirebaseAuthSettings()
//                    .setAppVerificationDisabledForTesting(true);
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(rAuth)
                            .setPhoneNumber(phoneNumber)       // Set số điện thoại
                            .setTimeout(60L, TimeUnit.SECONDS) // Set thời gian chờ
                            .setActivity(this)
                            .setCallbacks(mCallbacks)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: please try again", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "sendVerificationCode: " + e.getMessage());
        }

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            Toast.makeText(VerifyPhoneActivity.this, "Verification Failed.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(VerifyPhoneActivity.this, "OTP code has sent to your phone number", Toast.LENGTH_SHORT);
        }
    };

    private void verifyCode (String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInByCredentials(credential);
    }

    private void signInByCredentials (PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyPhoneActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUser(String phone) {
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Intent intent = new Intent(VerifyPhoneActivity.this, UserInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);
                    intent.putExtra("phoneNumberPackage", bundle);
                    Log.d(TAG, "onComplete: " + phone);
                    for(DataSnapshot data : task.getResult().getChildren()) {
                        if (phone.equals(data.child("phoneNumber").getValue())) {
                            loggedIn = true;
                            break;
                        } else {
                            loggedIn = false;
                        }
                    }
                    if (loggedIn == true) {
                        Log.d(TAG, "onComplete: LoggedIn!");
                        rAuth = FirebaseAuth.getInstance();
                        mAuthListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String currentUserID = user.getUid();

                                    Intent intent1 = new Intent(VerifyPhoneActivity.this, MessageActivity.class);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("currentUserID", currentUserID);
                                    intent.putExtra("currentUserPackage", bundle);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        };
                        rAuth.addAuthStateListener(mAuthListener);

                    } else {
                        rAuth = FirebaseAuth.getInstance();
                        mAuthListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                startActivity(intent);
                            }
                        };
                        rAuth.addAuthStateListener(mAuthListener);

                    }
                }
            }
        });
    }
}