package com.george.econtactdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseAdd extends AppCompatActivity {
    EditText edtName, edtMail, edtPhone, edtAddress, edtLat, edtLong, edtProfession, edtProfile;
    Button btnAdd;
    Spinner spinner1;
    TextView txtNull, txt0;

    FirebaseAuth auth;
    Spinner[] spinners = new Spinner[8];


    public static String idUser;

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;

    DatabaseReference databaseProfessionals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_add);


        databaseProfessionals = FirebaseDatabase.getInstance().getReference("Professionals");
        auth = FirebaseAuth.getInstance();

        edtName = (EditText) findViewById(R.id.edtName);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtLat = (EditText) findViewById(R.id.edtLat);
        edtLong = (EditText) findViewById(R.id.edtLong);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        edtProfession = (EditText) findViewById(R.id.edtProfession);
        edtProfile = (EditText) findViewById(R.id.edtProfile);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinners[0] = (Spinner) findViewById(R.id.sp0);
        spinners[1] = (Spinner) findViewById(R.id.sp1);
        spinners[2] = (Spinner) findViewById(R.id.sp2);
        spinners[3] = (Spinner) findViewById(R.id.sp3);
        spinners[4] = (Spinner) findViewById(R.id.sp4);
        spinners[5] = (Spinner) findViewById(R.id.sp5);
        spinners[6] = (Spinner) findViewById(R.id.sp6);
        spinners[7] = (Spinner) findViewById(R.id.sp7);

        txtNull = (TextView) findViewById(R.id.txtNull);
        txt0 = (TextView) findViewById(R.id.txt0);

        hideAll();

        final ArrayList<String> categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.categories)));

        ArrayAdapter<String> categoriesxa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner1.setAdapter(categoriesxa);

        initSpinners();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setEnabledByPos(position);
            }

            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add user's data to firebase
                professionalsAdd();


                Intent intent = new Intent(FirebaseAdd.this, LaunchActivity.class);
                startActivity(intent);
                Toast.makeText(FirebaseAdd.this, "Restart the app", Toast.LENGTH_LONG).show();

            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // adding user's lat and lng
        getLocation();

    }

    /**
     * Pairnoume to pos pou ksekinaei apo 0. To pos 0 antistoixei
     * sto spinner1, opou kanoume hide ola. To pos 1 kai meta
     * antistoixoun sto sp0 mexri sp7 opou kanoume hide
     * ola ektos apo to epilegmeno.
     *
     * @param pos Einai to epilegmeno spinner (0 gia to spinner1).
     */
    public void setEnabledByPos(int pos) {
        if (pos == 0) {
            hideAll();
        } else {
            for (int i = 0; i < spinners.length; i++) {
                int position = pos - 1;
                int visibility = i == position ? View.VISIBLE : View.INVISIBLE;
                boolean enabled = i == position;
                spinners[i].setVisibility(visibility);
                spinners[i].setEnabled(enabled);
            }
        }

        txt0.setVisibility(View.VISIBLE);
        txtNull.setVisibility(View.INVISIBLE);
    }

    public void initSpinners() {
        for (int i = 0; i < spinners.length; i++) {
            int id = getResources().getIdentifier("sp" + i, "array", getPackageName());
            String[] array = getResources().getStringArray(id);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array);
            spinners[i].setAdapter(adapter);
        }
    }

    private void hideAll() {
        for (Spinner sp : spinners) {
            sp.setVisibility(View.INVISIBLE);
            sp.setEnabled(false);
        }

        txt0.setVisibility(View.VISIBLE);
        txtNull.setVisibility(View.INVISIBLE);
    }


    private void professionalsAdd() {


        String name = edtName.getText().toString().trim();
        String mail = edtMail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String profession = edtProfession.getText().toString().trim();
        String profile = edtProfile.getText().toString().trim();
        double lat = Double.parseDouble(edtLat.getText().toString().trim());
        double lng = Double.parseDouble(edtLong.getText().toString().trim());

        final String categories = spinner1.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(String.valueOf(lat)) && !TextUtils.isEmpty(String.valueOf(lng)) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(profession)) {

            // creating a new id for each user
            idUser = auth.getCurrentUser().getUid();


            //calling professionls class for entry data
            Professionals professionals = new Professionals(name, mail, phone, address, lat, lng, profession, profile);


            //creating many children if sp0,1,2,3,... is enabled ... add this spinner to firebase as a sub-child
            boolean success = false;
            for (Spinner sp : spinners) {
                if (sp.isEnabled()) {
                    String subCategory = sp.getSelectedItem().toString();
                    databaseProfessionals.child(categories).child(subCategory).push().child(idUser).setValue(professionals);
                    success = true;
                    break;
                }
            }
            if (!success) {
                Toast.makeText(this, "Κάτι πήγε λάθος, προσπάθησε ξανά", Toast.LENGTH_LONG).show();
            }


            ClearTxt();

            Toast.makeText(this, "Η εισαγωγή μόλις ολοκληρώθηκε", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(this, "Συμπληρώστε όλα τα πεδία", Toast.LENGTH_LONG).show();
        }
    }


    private void ClearTxt() {

        edtName.setText("");
        edtMail.setText("");
        edtPhone.setText("");
        edtAddress.setText("");
        edtLong.setText("");
        edtLat.setText("");
    }

    public void browser(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.latlong.net/convert-address-to-lat-long.html"));
        startActivity(browserIntent);

    }


    public void infoExplain(View view) {

        Intent intent = new Intent(FirebaseAdd.this, PopupInfo.class);
        startActivity(intent);

    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                    REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {

                double lati = location.getLatitude();
                double lngi = location.getLongitude();
                ((EditText) findViewById(R.id.edtLat)).setText("" + lati);
                ((EditText) findViewById(R.id.edtLong)).setText("" + lngi);
            } else {
                ((EditText) findViewById(R.id.edtLat)).setText(null);
                ((EditText) findViewById(R.id.edtLong)).setText(null);

            }
        }


    }
}
