package com.george.econtactdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private DatabaseReference mProf1;
    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String category = getIntent().getStringExtra("category");
        String subcategory = getIntent().getStringExtra("subcategory");

        //gia offline xrhsh apo edw mexri....
        //ousiastika apothikeyei ta data sto kinhto san local db.

        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        mProf1 = FirebaseDatabase.getInstance().getReference("Professionals").child(category).child(subcategory);


        mProf1.keepSynced(true);

        mProf1.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });        //mexri edw gia offline teleiws!
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mProf1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                            final Professionals professionalsUser = ds.getValue(Professionals.class);
                        if (professionalsUser == null) return;

                        float color = professionalsUser.profile.equals("") ?
                                BitmapDescriptorFactory.HUE_BLUE :
                                BitmapDescriptorFactory.HUE_YELLOW;

                        LatLng location = new LatLng(professionalsUser.latitude, professionalsUser.longitude);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(location)
                                .icon(BitmapDescriptorFactory.defaultMarker(color)));
                        marker.setTag(professionalsUser);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 9));

                    } else {
                        Toast.makeText(getApplicationContext(), "Database is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent intent = new Intent(MapsActivity.this, MapsPopUp.class);
                intent.putExtra("prof", (Professionals) marker.getTag());
                startActivity(intent);


                return false;
            }
        });
    }
}

