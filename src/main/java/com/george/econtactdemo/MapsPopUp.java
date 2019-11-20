package com.george.econtactdemo;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsPopUp extends AppCompatActivity {

    ArrayList<String> mlist = new ArrayList<>();
    ListView mListView;
    Button btnProfile;
    TextView txtUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_pop_up);

        mListView = (ListView) findViewById(R.id.list);


        btnProfile = (Button) findViewById(R.id.btnProfile);

        txtUser = (TextView) findViewById(R.id.txtUser);

        // creating a pop up window

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        txtUser.setText("Στοιχεία επικοινωνίας με τον Επαγγελματία");
        final Professionals prof = (Professionals) getIntent().getSerializableExtra("prof");
        if (prof == null) return;

        //pairneis ta stoixeia toy epaggelmatia apo thn db
        String name = prof.getName();
        final String mail = prof.getMail();
        final String phone = prof.getPhone();
        String profession = prof.getProfession();
        final String profile = prof.getProfile();

        //bazeis ta stoixeia tou ekastote epaggelmatia se lista
        mlist.add("Όνομα: " + name);
        mlist.add("e-mail:" + mail);
        mlist.add("Τηλέφωνο: " + phone);
        mlist.add("Επάγγελμα: " + profession);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, mlist);
        mListView.setAdapter(arrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


//                dhmiourgia email
                if (position == 1) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Σας βρήκα στο e-contact.");
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    try {
                        startActivity(Intent.createChooser(i, "Πως θα θέλατε να στείλετε το e-mail;"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MapsPopUp.this, "Δεν μπορείτε να στείλετε e-mail από το κινητό σας", 
                                Toast.LENGTH_SHORT).show();
                    }
                    // dhmiourgia klhshs thlefwnikhs
                } else if (position == 2) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(MapsPopUp.this, Manifest.permission.CALL_PHONE) 
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile != "") {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + profile));
                    startActivity(browserIntent);
                    Toast.makeText(MapsPopUp.this, "Ο Επαγγελματίας έχει προφίλ", Toast.LENGTH_SHORT).show();
                } else {
                    hideBtn();
                }
            }
        });
    }


    private void hideBtn() {

        btnProfile.setEnabled(false);
        btnProfile.setVisibility(View.INVISIBLE);
        Toast.makeText(MapsPopUp.this, "Ο επαγγελματίας δεν έχει προφίλ", Toast.LENGTH_SHORT).show();
    }
}
