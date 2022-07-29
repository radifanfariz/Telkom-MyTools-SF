package com.mediumsitompul.maps_query_odp_onsite;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.PolyUtil;
import com.mediumsitompul.maps_query_odp_onsite.app.AppController;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Maps_MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    //..............................................................................................
    MapFragment mapFragment;
    GoogleMap gMap,gMap2;
    MarkerOptions markerOptions = new MarkerOptions();
    MarkerOptions markerOptions2 = new MarkerOptions();
    Marker myMarker;
    Polyline myPolyline;
    CameraPosition cameraPosition;
    CameraPosition cameraTilt;
    LatLng center, latLng, currentPos;
    public  boolean condLatlng = false;
    public boolean condMove = false;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    String distance_;
    String id_; //medium
    String nama_odp_; //medium

    String lat_; //medium
    String lng_; //medium
    String cLat;
    String cLong;
    private double longitude;
    private double latitude;
    private Marker mCurrLocationMarker,mCurrLocationMarker2;
    private String rad;
    private String odo;
    private String routes_line;
    private double lng;
    private double lat;
    public double filter;

    String total_; //medium
    String service_; //medium
    String avai_; //medium

    Float occupancy;

    public static final String ID = "id";
    public static final String NAMA_ODP = "nama_odp";
    public static final String DISTANCE = "distance";
    public static final String DISTANCE_ODO = "distance_odo";
    public static final String ROUTES_LINE = "routes_line";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public static final String TOTAL = "total";
    public static final String SERVICE = "service";
    public static final String AVAI = "avai";
    //public static String DISTANCE = "distance";

    public static final double RADIUS = 6378;


    private static final int MY_PERMISSION_LOCATION_REQUEST_CODE = 88;//medium
    private LocationManager locationManager, mLocationManager;


    String tag_json_obj = "json_obj_req";
//    Maps_ProgressBarMarker progressBarMarker;
    Maps_DialogInteraction dialogInteraction;
    Bundle info;
    Intent infoIntent;

    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    Maps_InfoWindowData infoWIndowData ;
    SlidingUpPanelLayout slidingUpPanelLayout;
    TextView txtAddrs;
    SearchView searchAddrs;
    Maps_Directions directions;

    AutocompleteSupportFragment autocompleteSupportFragment;
    Boolean addrsReceive = false;
    CardView cardViewAutocomplete;


    //..............................................................................................

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity_main);

        Places.initialize(getApplicationContext(),"AIzaSyAmX8akgpyhr76Vg8M18I8ZFlTQCtXd1EY");
        PlacesClient placesClient = Places.createClient(this);

//        directions = new Maps_Directions(Maps_MainActivity.this,Maps_MainActivity.this);

        autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        cardViewAutocomplete = (CardView)findViewById(R.id.cardview_autocomplete);


        //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        dialogInteraction = new Maps_DialogInteraction();
        progressDialog = new ProgressDialog(this);
        info = new Bundle();
        //infoIntent = new Intent(this,Main2Activity.class);
        infoIntent = new Intent(this, Maps_SF_Activity.class);
        infoWIndowData = new Maps_InfoWindowData();
        txtAddrs = (TextView) findViewById(R.id.txtAddress);
        slidingUpPanelLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.name().equalsIgnoreCase("Collapse")){

                }else if (newState.name().equalsIgnoreCase("Expanded")){

                }
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


//        slidingUpPanelLayout.addPanelSlideListener(onSliderListener());

        checkLocationPermission();        //Android versi 5/6 kali
        checkLocation();                  //Android versi 5/6 kali
        //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

        buildGoogleApiClient();

        // Fixing Later Map loading Delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();
    }


    public void mapsSearch(){
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setCountry("ID");

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@androidx.annotation.NonNull Place place) {
                mCurrLocationMarker.setPosition(place.getLatLng());
                setMarkerMove(1);
                gMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                String myAddress = getmyAddress(place.getLatLng());
                txtAddrs.setText(myAddress);
                startLocationUpdates();
                currentPos = place.getLatLng();
                if (addrsReceive == true){
                    setMarkerMove(2);
                }
            }

            @Override
            public void onError(@androidx.annotation.NonNull Status status) {
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

//    @Override
//    protected  void onResume(){
//        super.onResume();
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            startLocationUpdates();
//        }
//    }

    private SlidingUpPanelLayout.PanelSlideListener onSliderListener(){
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Toast.makeText(getApplicationContext(), "i slide", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Toast.makeText(getApplicationContext(), "i state", Toast.LENGTH_LONG).show();
            }
        };
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    //.........................................................

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap2 = googleMap;
        gMap.setMapType(2);
        gMap.setTrafficEnabled(true);
        center = new LatLng(lat, lng);
        mapsSearch();
        cardViewAutocomplete.setVisibility(View.INVISIBLE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        gMap.setMyLocationEnabled(true);//Tombol setting to current location, butuh permission, walau merah tapi jalan
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().isMyLocationButtonEnabled();
        gMap.getUiSettings().setMapToolbarEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(false);
        gMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setTiltGesturesEnabled(true);
        gMap.setBuildingsEnabled(true);
        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                cardViewAutocomplete.setVisibility(View.VISIBLE);
                mCurrLocationMarker = gMap.addMarker(markerOptions);
                mCurrLocationMarker.setPosition(latLng);
                setMarkerMove(1);
                gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                startLocationUpdates();
            }
        });



        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)); //medium
        cameraPosition = new CameraPosition.Builder().target(center).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setMarkerMove(int i){
        if (i == 1){
            condMove = true;
        }
        if (i == 2) {
            condMove = false;
        }
        startLocationUpdates();
    }




    private void addMarker(final MarkerOptions markerParam, final String polyLineString, final LatLng latlng, final List<String> data, boolean condition) { //.................................. WARNA MARKER ....................................................
        markerParam.position(latlng);
        markerParam.title(data.toString().replaceAll("^\\[|]$","")); //marker diklik, maka akan kelihatan deskripsi/title
        markerParam.snippet(polyLineString);



        myMarker= gMap.addMarker(markerParam);


        if (polyLineString != null){
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    List<LatLng> polylineList = new ArrayList<>();
                    PolylineOptions polylineOptions = new PolylineOptions();
                    try {
                        polylineList = PolyUtil.decode(marker.getSnippet());

                        Log.d("SHOW DATA PolyLine:",polyLineString.toString());

                        polylineOptions.addAll(polylineList);
                        polylineOptions.width(30);
                        polylineOptions.color(0xffff0000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (polylineOptions != null){
                        if (myPolyline != null) {
                            myPolyline.remove();
                        }
                        myPolyline = gMap.addPolyline(polylineOptions);
                        Log.d("POLYLINE In FUNCTION:",polylineList.toString());
                    }
                    return false;
                }
            });
        }


        if (condition == false) {
            myMarker.remove();
        }
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                if (marker.getTitle() == null){
                    marker.setTitle("No Data,No Data,No Data");
                }
                List<String> data = new ArrayList<>(Arrays.asList(marker.getTitle().split(",")));
                infoIntent.putExtra("nama_odp",data.get(0));
                infoIntent.putExtra("rad",data.get(1));
                infoIntent.putExtra("odo",data.get(2));
                infoIntent.putExtra("lat1",marker.getPosition().latitude);
                infoIntent.putExtra("lng1",marker.getPosition().longitude);
                infoIntent.putExtra("currentlat",currentPos.latitude);
                infoIntent.putExtra("currentlng",currentPos.longitude);
                infoIntent.putExtra("address",getmyAddress(currentPos));
                infoIntent.putExtra("userid",getIntent().getStringExtra("parse_userid"));
                infoIntent.putExtra("imei",getIntent().getStringExtra("parse_imei"));


                startActivity(infoIntent);
            }
        });
    }
    //............................................................................................ WARNA MARKER ..............................................


    // Fungsi get JSON marker
//    private String url = "http://192.168.100.78/mytools/android/_odp_compliance_index.php"; //AIO = ALL IN ONE
//    private String url = "http://192.168.43.2/mytools/android/_odp_compliance_index.php";
    //private String url = "http://36.89.34.66/mytools/android/_odp_compliance_index.php"; //AIO = ALL IN ONE





    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Maps_Constants.ODP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);

                    String getObject = jObj.getString("odp");//mediumsitompul, sesuai dgn nama di script PHP
                    JSONArray jsonArray = new JSONArray(getObject);
//                    gMap.addCircle(new CircleOptions()
//                            .center(new LatLng(currentPos.latitude, currentPos.longitude))
//                            .radius(filter*1000)
//                            .fillColor(Color.argb(0, 0, 0, 0)));
                    gMap.addCircle(new CircleOptions()
                            .center(new LatLng(currentPos.latitude, currentPos.longitude))
                            .radius(filter*1000)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(0, 0, 0, 0)));


                    for (int i = 0; i < jsonArray.length(); i++) { // .............................. QUERY DATABASE

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nama_odp_ = jsonObject.getString(NAMA_ODP);

                        distance_ = jsonObject.getString(DISTANCE);
                        double distance_d = Double.parseDouble(distance_);          //convert String to Doeble
                        double distance_doeble = distance_d*1000;                   //Calculate .... x1000
                        String distance_ = new Double(distance_doeble).toString();  //convert Doeble to String
                        rad = String.format("%.2f",Double.valueOf(distance_));

                        odo = jsonObject.getString(DISTANCE_ODO);
                        routes_line = jsonObject.getString(ROUTES_LINE);



                        lat_ = jsonObject.getString(LAT);
                        lng_ = jsonObject.getString(LNG);
                        total_ = jsonObject.getString(TOTAL);
                        service_ = jsonObject.getString(SERVICE);
                        avai_ = jsonObject.getString(AVAI);
                        cLat = Double.toString(latitude);
                        cLong = Double.toString(longitude);

                        occupancy =  (Float.parseFloat(service_)/Float.parseFloat(total_))*100;

                        latLng = new LatLng(
                                Double.parseDouble(jsonObject.getString(LAT)),
                                Double.parseDouble(jsonObject.getString(LNG))
                        );
//                        try {
//                            directions.setDirections(currentPos,latLng);
//                            Log.d("ODO ODO ODO :",Maps_Directions.getDirections());
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }


//                          double distance = radiusMarker(lat_,lng_);
//                        Log.i("Distance",distance);

//                        mCurrLocationMarker = gMap.addMarker(markerOptions);
                        /////////////////////////////////Table Info ODP////////////////////////////////////////////
                        Maps_InfoWindowData infoWIndowData = new Maps_InfoWindowData();
                        infoWIndowData.setNameodp(nama_odp_);
                        infoWIndowData.setDistanceRad(rad + " m");
                        infoWIndowData.setDistanceOdo(odo);
                        infoWIndowData.setTotal(total_);
                        infoWIndowData.setService(service_);
                        infoWIndowData.setAvai(avai_);
                        infoWIndowData.setOcc(Float.toString(occupancy));
                        infoWIndowData.setLat(Double.toString(latLng.latitude));
                        infoWIndowData.setLng(Double.toString(latLng.longitude));

                        Maps_InfoWindowActivity infoWindowActivity = new Maps_InfoWindowActivity(Maps_MainActivity.this);
                        gMap.setInfoWindowAdapter(infoWindowActivity);
                        ///////////////////////////////////////////////////////////////////////////////////////////

//                            addMarker(markerOptions,latLng, "Info ODP",true);
//                            myMarker.setTag(infoWIndowData);
//                            myMarker.showInfoWindow();
                        //Pengaturan marker option termasuk warna marker dilakukan terlebih dahulu sebelum marker dibuat
                        if (occupancy == 0) {
//                                Log.e("Occupancy: ", Float.toString(occupancy));
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_black));
                        } else if ((occupancy > 0) & (occupancy <= 40)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_green));
                        } else if ((occupancy > 40) & (occupancy < 80)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_yellow));
                        } else if ((occupancy >= 80) & (occupancy < 100)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red));
                        } else if (occupancy == 100) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_redblack));
                        }
                        else{
                            gMap.clear();
                        }


                        List <String> data = new ArrayList<>();
                        data.add(nama_odp_);
                        data.add(rad+ " m");
                        data.add(odo);
                        Log.d("SHOW DATA TO INTENT:",data.toString());
//                        addMarker(markerOptions,latLng,nama_odp_,distance_,true);
                        addMarker(markerOptions,routes_line,latLng,data,true);
                        myMarker.setTag(infoWIndowData);
                        myMarker.showInfoWindow();

                    }
                    Toast.makeText(Maps_MainActivity.this,"Data Berhasil Di Query", Toast.LENGTH_SHORT).show();
                    setMarkerMove(2);
                    cardViewAutocomplete.setVisibility(View.INVISIBLE);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(Maps_MainActivity.this, "Tidak Ditemukan ODP Di Daerah Tersebut", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(Maps_MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Maps_MainActivity.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map <String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("lat",Double.toString(currentPos.latitude));
                params.put("long",Double.toString(currentPos.longitude));
                params.put("filter",Double.toString(filter));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }



    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Harap Aktifkan Lokasi Anda")
                .setMessage("GPS anda dalam keadaan nonaktif.\nHarap Aktifkan GPS untuk " +
                        "menggunakan aplikasi ini")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }



    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            //mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Lokasi tidak dapat ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //mLocation = location;
//        if (mCurrLocationMarker != null | startLocationUpdates() == false) {
//            mCurrLocationMarker.remove();
//            startLocationUpdates();
//        }
        //Place current location marker
        if(condLatlng == false) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentPos = latLng;
            markerOptions.position(latLng);
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(  imageResize(R.mipmap.current_location_red, 100, 100)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_location));
            mCurrLocationMarker = gMap.addMarker(markerOptions);
            Log.i("Lokasi", Double.toString(lng));
            cameraTilt = new CameraPosition.Builder().target(latLng).zoom(12).tilt(60).build();
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); //medium
            gMap.animateCamera(CameraUpdateFactory.zoomTo(17)); // Manuel

            gMap.getUiSettings().setZoomControlsEnabled(true);//new +/-
            condLatlng = true;
        }
//        new sentData().execute("192.168.137.1:8080/marker/coba.php");
//        Maps_InfoWindowData infoWIndowData = new Maps_InfoWindowData();
//        infoWIndowData.setNameodp("Current Location");
//        infoWIndowData.setLat(Double.toString(latLng.latitude));
//        infoWIndowData.setLng(Double.toString(latLng.longitude));
//
//        Maps_InfoWindowActivity infoWindowActivity = new Maps_InfoWindowActivity(Maps_MainActivity.this);
//        gMap.setInfoWindowAdapter(infoWindowActivity);
//
//        final MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Location Info");
//        markerOptions.snippet("lat="+ latLng.latitude+ "|| lng="+ latLng.longitude);


//        mCurrLocationMarker = gMap.addMarker(markerOptions);
//        mCurrLocationMarker.setTag(infoWIndowData);
//        mCurrLocationMarker.showInfoWindow();

//        gMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//                new android.app.AlertDialog.Builder(Maps_MainActivity.this)
//                        .setTitle("Select Radius")
//                        .setMessage("Do You Want To Pick Up Location ?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                pickUpLocation();
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .setCancelable(false)
//                        .show();
//            }
//        });
//        gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                LatLng camlatlng = new LatLng(gMap.getCameraPosition().target.latitude,gMap.getCameraPosition().target.longitude);
//                if (currentPos != null){
//                    mCurrLocationMarker.remove();
//                    currentPos = camlatlng;
//                    markerOptions.position(camlatlng);
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(imageResize(R.mipmap.current_location,100,100))); //medium
//                    Maps_InfoWindowData infoWIndowData = new Maps_InfoWindowData();
//                    infoWIndowData.setNameodp("Current Location");
//                    infoWIndowData.setLat(Double.toString(currentPos.latitude));
//                    infoWIndowData.setLng(Double.toString(currentPos.longitude));
//
//                    Maps_InfoWindowActivity infoWindowActivity = new Maps_InfoWindowActivity(Maps_MainActivity.this);
//                    gMap.setInfoWindowAdapter(infoWindowActivity);
//                    mCurrLocationMarker = gMap.addMarker(markerOptions);
//                    mCurrLocationMarker.setTag(infoWIndowData);
//                    mCurrLocationMarker.showInfoWindow();
//                }
//            }
//        });
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(imageResize(R.mipmap.current_location, 100, 100))); //medium
        gMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(imageResize(R.mipmap.current_location_red, 100, 100))); //medium
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_location));
                        } catch (Exception ignored) {

                        }
                    }
                }).start();
            }
        });
        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng camlatlng = new LatLng(gMap.getCameraPosition().target.latitude, gMap.getCameraPosition().target.longitude);
                if (currentPos != null && condMove == true) {
//                    mCurrLocationMarker.remove();
                    currentPos = camlatlng;
//                    markerOptions.position(camlatlng);
//                    Maps_InfoWindowData infoWIndowData = new Maps_InfoWindowData();
//                    infoWIndowData.setNameodp("Current Location");
//                    infoWIndowData.setLat(Double.toString(currentPos.latitude));
//                    infoWIndowData.setLng(Double.toString(currentPos.longitude));
//
//                    Maps_InfoWindowActivity infoWindowActivity = new Maps_InfoWindowActivity(Maps_MainActivity.this);
//                    gMap.setInfoWindowAdapter(infoWindowActivity);
//                    mCurrLocationMarker = gMap.addMarker(markerOptions);
                    mCurrLocationMarker.setPosition(camlatlng);
//                    mCurrLocationMarker.setTag(infoWIndowData);
//                    mCurrLocationMarker.showInfoWindow();
                }
            }
        });
        gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(condMove == true) {
                    LatLng camlatlng = new LatLng(gMap.getCameraPosition().target.latitude, gMap.getCameraPosition().target.longitude);
                    currentPos = camlatlng;
                    startLocationUpdates();
//                    Maps_InfoWindowData infoWIndowData = new Maps_InfoWindowData();
                    infoWIndowData.setNameodp("Current Location");
                    infoWIndowData.setLat(Double.toString(currentPos.latitude));
                    infoWIndowData.setLng(Double.toString(currentPos.longitude));

                    Maps_InfoWindowActivity infoWindowActivity = new Maps_InfoWindowActivity(Maps_MainActivity.this);
                    gMap.setInfoWindowAdapter(infoWindowActivity);
                    mCurrLocationMarker.setTag(infoWIndowData);
                    mCurrLocationMarker.showInfoWindow();
//                    addMarker(markerOptions, currentPos, "Current Location","Null", false);
                    List<String> data = new ArrayList<>();
                    data.add("Current Location");
                    data.add("Null");
                    data.add("Null");
                    addMarker(markerOptions, null, currentPos, data, false);

//                Toast.makeText(getApplicationContext(), currentPos.toString(), Toast.LENGTH_SHORT).show();
                    String myAddress = getmyAddress(currentPos);
                    txtAddrs.setText(myAddress);
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    slidingUpPanelLayout.setTouchEnabled(false);
//                        Toast.makeText(Maps_MainActivity.this, myAddress, Toast.LENGTH_LONG).show();
                    startLocationUpdates();
                }

            }
        });
//            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    mCurrLocationMarker.setVisible(false);
//                    return false;
//                }
//            });


        stopLocationUpdate();
        //move map camera
//        cameraTilt = new CameraPosition.Builder().target(latLng).zoom(12).tilt(60).build();
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); //medium
//        gMap.animateCamera(CameraUpdateFactory.zoomTo(17)); // Manuel
//
//        gMap.getUiSettings().u(true);//new +/-

    }

    private String getmyAddress(LatLng currentPos) {
        String myAddress = "";
        Geocoder geocoder = new Geocoder(Maps_MainActivity.this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(currentPos.latitude,currentPos.longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            myAddress = addresses.get(0).getAddressLine(0);
            if(myAddress == null){
                myAddress = "Alamat Tidak Ditemukan";
            }
            addrsReceive = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myAddress;
    }
    private String getmyLocation(LatLng currentPos) {
        String myAddress = "";
        Geocoder geocoder = new Geocoder(Maps_MainActivity.this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(currentPos.latitude,currentPos.longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            myAddress = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myAddress;
    }



    protected boolean startLocationUpdates() {
        // Create the location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback,Looper.getMainLooper());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            return true;
        }
        else {
            return false;
        }
    }

    protected void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map_setting:
                dialogInteraction.setCheckBox(this,gMap);
                break;
            case R.id.action_get_odp:
                if (startLocationUpdates()) {
                    gMap.clear();
                    ProMarker proMarker = new ProMarker();
                    proMarker.execute();
                }
                break;
            case R.id.action_radius:
                dialogInteraction.setRadioBox(this);
                break;

        }
        return true;
    }
    public Bitmap imageResize(int resource,int width,int height){

        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(resource);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap customMarker = Bitmap.createScaledBitmap(b,width,height,false);

        return customMarker;
    }

    private class ProMarker extends AsyncTask<Void, Integer, String> {
        int progress_status;
        Maps_ProgressBarMarker progressBarMarker;

        @Override
        protected String doInBackground(Void... params) {
            // doInBackground() adalah tempat kita melakukan proses di thread lain
            filter = dialogInteraction.radius;
            getMarkers();
                while(progress_status < 100){
                    try {
                        progress_status += 5;
                        publishProgress(progress_status);
                        Thread.sleep(200);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            return "Success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_status = 0;
            progressBarMarker = new Maps_ProgressBarMarker(Maps_MainActivity.this);
            progressBarMarker.showProgressDialog("Loading Data", "Please Wait...");
//            mCurrLocationMarker2 = gMap.addMarker(markerOptions);
//            mCurrLocationMarker2.setTag(infoWIndowData);
//            mCurrLocationMarker2.showInfoWindow();
//            mCurrLocationMarker2.setPosition(currentPos);
            List<String> data = new ArrayList<>();
            data.add("Pick Location");
            data.add("Null");
            data.add("Null");
            Maps_InfoWindowActivity.name_odp = "Pick Location";
            Maps_InfoWindowActivity.lat = Double.toString(currentPos.latitude);
            Maps_InfoWindowActivity.lng = Double.toString(currentPos.longitude);
            addMarker(markerOptions, null, currentPos, data, true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBarMarker.progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // onPostExecute akan dieksekusi setelah doInBackground selesai dieksekusi

//                mCurrLocationMarker2 = gMap.addMarker(markerOptions);
//                mCurrLocationMarker2.setTag(infoWIndowData);
//                mCurrLocationMarker2.showInfoWindow();
//                mCurrLocationMarker2.setPosition(currentPos);
            progressBarMarker.progressDialog.dismiss();
                setMarkerMove(2);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                if (result == "Success") {
                    progressBarMarker.hideProgressDialog();
                }
//            progressBarMarker = new Maps_ProgressBarMarker(Maps_MainActivity.this);
//            progressBarMarker.showProgressDialog("Loading Data", "Please Wait...");
        }
    }
}


