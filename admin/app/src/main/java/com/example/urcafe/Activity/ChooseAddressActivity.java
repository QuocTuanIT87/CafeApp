package com.example.urcafe.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.urcafe.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class ChooseAddressActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView mapSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        mapSearch=findViewById(R.id.mapSearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAddress);


        mapSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearch.getQuery().toString();
                List<Address> addressList=null;
                if(location != null){
                    Geocoder geocoder = new Geocoder(ChooseAddressActivity.this);
                    try{
                        addressList=geocoder.getFromLocationName(location, 1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                double latitude = marker.getPosition().latitude;
                double longitude = marker.getPosition().longitude;
                // Get the address from the marker's title
                String address = getAddressFromLatLng(latitude, longitude);

                // Return the address to the previous activity
                Intent intent = new Intent();
                intent.putExtra("address", address);
                setResult(Activity.RESULT_OK, intent);
                finish();

                return true; // Consume the event
            }
        });
        // Add map click listener to add marker on map click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Remove any existing markers
                mMap.clear();

                // Get the address from the clicked position
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                String address = getAddressFromLatLng(latitude, longitude);

                // Add marker
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(address);
                mMap.addMarker(markerOptions);
            }
        });
    }



    private String getAddressFromLatLng(double latitude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    builder.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                // Remove the trailing comma and space
                address = builder.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


}