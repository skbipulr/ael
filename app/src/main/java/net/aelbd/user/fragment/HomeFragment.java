package net.aelbd.user.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.aelbd.user.R;
import net.aelbd.user.bottomSheet.RestaurantDetailsBottomSheet;
import net.aelbd.user.databinding.FragmentHomeBinding;
import net.aelbd.user.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean locationPermissionsGranted = false;

    private GoogleMap map;
    private FragmentManager fragmentManager;
    private GoogleMapOptions googleMapOptions;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Restaurant> restaurantList;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.swipeRefreshL.setRefreshing(true);
        initialize();

        getLocationPermission();

        getRestaurantList();


        binding.myLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        binding.swipeRefreshL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshL.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }


    private void initialize() {
        restaurantList = new ArrayList<>();
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    /*-------------------------------------------------------------
   get location permission -- start
   -------------------------------------------------------------*/
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionsGranted = true;
                    initializeMap();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            permissions,
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            initializeMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionsGranted = false;
                            return;
                        }
                    }
                    locationPermissionsGranted = true;
                    initializeMap();
                }
            }
        }
    }

    /*-------------------------------------------------------------
    get location permission -- end
    -------------------------------------------------------------*/


    private void getRestaurantList() {
        //API call will be here.
        restaurantList.add(new Restaurant("Cloud Bistro", "Rowshan Tower, 152/2A-2 (1st Floor, Panthapath Road, Dhaka 1205)", 23.751409, 90.386371));
        restaurantList.add(new Restaurant("Santoor Restaurant", "House no. 2, Road No 32, Mirpur Rd, Dhaka 1209", 23.752277, 90.377506));
        restaurantList.add(new Restaurant("Dhanshiri Plus Restaurant", "Barek Mension, Panthapath, 58, 11/A Free School St, Dhaka 1209", 23.750820, 90.389166));
        restaurantList.add(new Restaurant("Dolma Restaurant","102 Kazi Nazrul Islam Ave, Dhaka 1215",23.753178, 90.392365));
        restaurantList.add(new Restaurant("Bangla Restaurant","Station Road, Dhaka 1215",23.757499, 90.393694));

    }

    private void initializeMap() {

        googleMapOptions = new GoogleMapOptions();
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.mapViewFrameLayout, supportMapFragment);
        fragmentTransaction.commit();
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        changeMapStyle(map);


        if (locationPermissionsGranted) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            getDeviceLocation();

            setRestaurantMarker();

            map.setMyLocationEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                openBottomSheet(marker.getSnippet());
                return true;
            }
        });
    }

    private void openBottomSheet(String position) {
        if (!position.equals(null)) {
            Bundle bundle = new Bundle();
            bundle.putString("name", restaurantList.get(Integer.parseInt(position)).getName());
            bundle.putString("address", restaurantList.get(Integer.parseInt(position)).getAddress());
            RestaurantDetailsBottomSheet sendRequestBottomSheet = new RestaurantDetailsBottomSheet();
            sendRequestBottomSheet.setArguments(bundle);
            sendRequestBottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "RestaurantDetailsBottomSheet");
        }

    }

    private void setRestaurantMarker() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<restaurantList.size();i++){
                    Restaurant restaurant = restaurantList.get(i);
                    setMarker(new LatLng(restaurant.getLatitude(),restaurant.getLongitude()),String.valueOf(i),String.valueOf(restaurant.getName().charAt(0)));
                }
                binding.swipeRefreshL.setRefreshing(false);
            }
        },2000);
    }


    private void getDeviceLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (locationPermissionsGranted) {

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                        } else {
                            Toast.makeText(getActivity(), "Unable to access to location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }
    }


    private void moveCamera(LatLng latLng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void setMarker(LatLng latLng, String position, String name) {
        map.addMarker(new MarkerOptions().position(latLng).snippet(position).icon(BitmapDescriptorFactory
                .fromBitmap(createCustomMarker(name))));

    }


    private void changeMapStyle(GoogleMap map) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    getActivity(), R.raw.map_style));

        } catch (Resources.NotFoundException e) {

        }
    }

    public Bitmap createCustomMarker(String name) {
        Bitmap bitmap = null;

        if (getActivity() != null) {
            View marker = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

            TextView nameTV = marker.findViewById(R.id.nameTV);

            nameTV.setText(name);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
            marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.buildDrawingCache();
            bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            marker.draw(canvas);

        }
        return bitmap;
    }


}
