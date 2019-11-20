package com.george.econtactdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ProfileAccount extends AppCompatActivity {

    private TextView name, phone, mail, profession, address;
    ImageButton image;
    Button btnEdit;
    private  DatabaseReference mRef;
    public static final int GALLERY_CODE = 2;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_account);

        name = (TextView)findViewById(R.id.nameName);
        phone = (TextView)findViewById(R.id.phonePhone);
        mail = (TextView)findViewById(R.id.mailMail);
        profession = (TextView)findViewById(R.id.professionProf);
        address = (TextView)findViewById(R.id.AddressAddress);
        image = (ImageButton) findViewById(R.id.imageButton);
        btnEdit = (Button) findViewById(R.id.btnEditProfile);


        Professionals prof = new Professionals();
        name.setText(prof.getName());
        phone.setText(prof.getPhone());
        mail.setText(prof.getMail());
        profession.setText(prof.getProfession());
        address.setText(prof.getAddress());

        mRef = FirebaseDatabase.getInstance().getReference("Professionals");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            uri = data.getData();
        }

    }





}
