package com.example.urcafe.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.example.urcafe.Model.ShipperLocation;
import com.example.urcafe.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.urcafe.databinding.ActivityShipperTrackingBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class ShipperTrackingActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double orderLat,orderLong;
    private double shipperLat,shipperLong;
    private Polyline polyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_tracking);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getOrderLocation();
        getShipperLocationFromFirebase();
    }
    private void getOrderLocation(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("address")) {
            String address = intent.getStringExtra("address");
            // Chuyển đổi địa chỉ thành tọa độ latitude và longitude
            try {
                Geocoder geocoder = new Geocoder(this); // Khởi tạo đối tượng Geocoder
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address firstAddress = addresses.get(0);
                    double orderLatitude = firstAddress.getLatitude();
                    double orderLongitude = firstAddress.getLongitude();
                    orderLat=orderLatitude;
                    orderLong=orderLongitude;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getShipperLocationFromFirebase() {
        // Truy vấn cơ sở dữ liệu Firebase để lấy thông tin vị trí của shipper
        DatabaseReference shipperLocationRef = FirebaseDatabase.getInstance().getReference().child("ShipperLocation");

        shipperLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem dữ liệu có tồn tại hay không
                if (dataSnapshot.exists()) {
                    // Lấy thông tin vị trí của shipper từ dataSnapshot
                    ShipperLocation shipperLocation = dataSnapshot.getValue(ShipperLocation.class);
                    if (shipperLocation != null) {
                        // Lấy latitude và longitude từ ShipperLocation
                        double shipperLatitude = shipperLocation.getLatitude();
                        double shipperLongitude = shipperLocation.getLongitude();
                       addMarker(shipperLatitude,shipperLongitude);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }
    private void addMarker(double latitude,double longitude){
        LatLng shipperLocation = new LatLng(latitude,longitude);
        MarkerOptions shipperMarkerOptions = new MarkerOptions();
        shipperMarkerOptions.position(shipperLocation);
        shipperMarkerOptions.title("Địa chỉ Shipper");
        mMap.addMarker(shipperMarkerOptions);

        LatLng orderLocation = new LatLng(orderLat,orderLong);
        MarkerOptions orderMarkerOptions = new MarkerOptions();
        orderMarkerOptions.position(orderLocation);
        orderMarkerOptions.title("Địa chỉ đơn hàng");
        mMap.addMarker(orderMarkerOptions);

        drawRoute(shipperLocation,orderLocation);
    }

    private void drawRoute(LatLng shipperLocation, LatLng orderLocation) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(shipperLocation);
        builder.include(orderLocation);
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(shipperLocation, orderLocation)
                .width(5)
                .color(Color.RED);
        polyline = mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng customLocation = new LatLng(10.860866, 106.761250);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(customLocation);
        markerOptions.title("Địa chỉ cửa hàng");
        Marker marker = mMap.addMarker(markerOptions);

    }
}