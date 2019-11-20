package com.george.econtactdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText edtUser, edtPass, edtRePass;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String user_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRePass = (EditText)findViewById(R.id.edt_Re_Pass);
        edtPass=(EditText) findViewById(R.id.edtPass);
        edtUser=(EditText) findViewById(R.id.edtUserName);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("emailUsers");



    }


    public void SignUpNow(View view) {
        final String email = edtUser.getText().toString().trim();
        final String pass= edtPass.getText().toString().trim();
        String repass= edtRePass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(repass)){
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener
                    (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                user_id = auth.getCurrentUser().getUid();

                                databaseReference.child(user_id).child("email").setValue(email);
                                Toast.makeText(Register.this, "Καλωσόρισες!!", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(Register.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    });


        }


    }
}
