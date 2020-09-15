package com.example.poetrious.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.poetrious.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    EditText number, code;
    TextView count;
    Button Send_Code, Verify_Btn, Resend;
    private String verificatiID;
    private FirebaseAuth mauth;
    Dialog dialog;
     String userid;
    private PhoneAuthProvider.ForceResendingToken ResendToken;
   private CountDownTimer cTimer = null;

    @Override
    public void onBackPressed() {
        dialog.setContentView(R.layout.exit);
        TextView YES=dialog.findViewById(R.id.Yes);
        TextView NO=dialog.findViewById(R.id.No);
           Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        number = findViewById(R.id.phonenumber);
        dialog = new Dialog(this);
        code = findViewById(R.id.code);
        Send_Code = findViewById(R.id.send_code);
        Verify_Btn = findViewById(R.id.verify_code);

        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_EXTERNAL_STORAGE  )+ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)+ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_CONTACTS)+ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA  )
                != PackageManager.PERMISSION_GRANTED) {

            final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(Login.this, permissions, 1);
        }



        count=findViewById(R.id.counter);
        mauth = FirebaseAuth.getInstance();
        final String PhoneNumber = "+92" + number.getText().toString().trim();
        final String User_Code = code.getText().toString().trim();
        Verify_Btn.setVisibility(View.GONE);

        count.setVisibility(View.GONE);
        code.setVisibility(View.GONE);

        Send_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (number.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Your Phone Number ", Toast.LENGTH_SHORT).show();
                    } else {
                        String Code="+92";
                        String num=number.getText().toString();
                        StringBuilder sb = new StringBuilder(num);
                        char first = num.charAt(0);
                        if(num.length()!=11&&num.length()!=10&&num.length()<11)
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter Valid Number ", Toast.LENGTH_SHORT).show();

                        }
                        else if(first=='0')
                        {
                            String str= sb.deleteCharAt(0).toString();
                            SendVerificationCode(Code+str.trim());
                        }
                        else {
                            SendVerificationCode(Code+num.trim());
                        }




                        Send_Code.setEnabled(false);


                    }







            }
        });




     cTimer=   new CountDownTimer(30000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                count.setText("Timeout: " + millisUntilFinished / 1000);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                count.setText("Click to resend");
                count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (number.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Enter Your Phone Number ", Toast.LENGTH_SHORT).show();
                        } else {
                            String Code = "+92";
                            String num = number.getText().toString();
                            StringBuilder sb = new StringBuilder(num);
                            char first = num.charAt(0);
                            if (num.length() != 11 && num.length() != 10 && num.length() < 11) {
                                Toast.makeText(getApplicationContext(), "Please Enter Valid Number ", Toast.LENGTH_SHORT).show();

                            } else if (first == '0') {
                                String str = sb.deleteCharAt(0).toString();
                                resendVerificationCode(Code + str.trim(),ResendToken);
                                cTimer.start();
                            } else {
                                resendVerificationCode(Code + num.trim(),ResendToken);
                                cTimer.start();
                            }


                        }



                    }
                });

            }
        };

        Verify_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (User_Code.isEmpty() || User_Code.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Code ", Toast.LENGTH_SHORT).show();

                }
                VerifyCode(User_Code);
            }
        });

    }

    private void VerifyCode(String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificatiID, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
            userid    = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    SharedPreferences id =getSharedPreferences("UID", MODE_PRIVATE);
                    SharedPreferences.Editor ed = id.edit();
                    ed.putString("User_id", userid);
                    ed.apply();
                    SharedPreferences sp =getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor et = sp.edit();
                    et.putBoolean("isLogin", true);
                    et.apply();
                    is_user_exist();

                } else {

                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
public void is_user_exist()
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reff;
    DatabaseReference myRef = database.getReference("Users_info");
    myRef.child(userid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.hasChild("User_Name")) {
                // run some code
                SharedPreferences sp =getSharedPreferences("Exist", MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.putBoolean("user", true);
                et.apply();
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else
            {

                Intent intent = new Intent(Login.this, Add_Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }



        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

    private void SendVerificationCode(String num) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback

        );

    }


    private void resendVerificationCode(String num,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(getApplicationContext(), "Code Has Sent To Your Phone Number", Toast.LENGTH_SHORT).show();
            verificatiID = s;
            count.setVisibility(View.VISIBLE);
            cTimer.start();
            ResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String Code = phoneAuthCredential.getSmsCode();
            if (Code != null) {
                VerifyCode(Code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };


}
