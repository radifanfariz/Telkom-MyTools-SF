package com.mediumsitompul.maps_query_odp_onsite;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.BaseApiService;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.UtilsApi;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


//public class Main2Activity extends AppCompatActivity {
//public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
//public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, AdapterView.OnItemSelectedListener {
public class Maps_SF_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnClickListener {


    private static final String TAG = "LatLong";
    private String text = null;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    //static SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


    SharedPreferences sharedPref;
//    public String password;
//    public Integer userid;
//    private String hash = null;

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    private File file;
    private File sourceFile;
    private File destFile;
    private SimpleDateFormat dateFormatter;

    private int index = 0;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;

    private String odp_name; //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    private String odp_address; //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public static Double lat = null;
    public static Double lng = null;

    public static TextView stp_name;
    public static TextView distance;

    private File foto1;
    private TextView latitude_rmh;
    private TextView longitude_rmh;
    private TextView latitude_odp;
    private TextView longitude_odp;
    

    public static ProgressDialog progressDialog;
    public static boolean inputSuccess = false;
//    String get_userid;
//    private TextView parse_userid;

    //..................................... START FROM UPLOADMULTIIMAGES ...........................
    private boolean cekan;
    private boolean cekan2;
    private boolean cekan3;

//    private int index = 0;
//    private GoogleApiClient mGoogleApiClient;
//    SharedPreferences sharedPref;



    private Button buttonChoose;
    private Button buttonChoose2;
    private Button buttonChoose3;
    private Button buttonUpload;
    private Button take_picture;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    //private EditText editText;
    private EditText ket1;


    private final int requestCode = 1;
    private LocationManager locationManager;
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    private Uri filePath2;
    private Uri filePath3;

    private String imagePath;
    private String imagePath2;
    private String imagePath3;
    private String imageFilePathOri;
    //    private Uri filePath3;
//    private Uri filePath4;
    private static final int CAMERA_REQ_CODE = 39; //QRCODE AND BARCODE, SAMA SAMA OKE   //QR-CODE
    //..................................... END FROM UPLOADMULTIIMAGES ...........................

    private EditText sales_id;
    private EditText cust_name;
    private EditText cust_addr;
    private EditText inst_addr;
    private EditText alt_addr;
    private EditText google_addr; //new
    private EditText edtRad;
    private EditText edtOdo;
    private EditText edtMyir;
    private EditText edtSc;

    private EditText hp;
    private EditText alt_hp;
    private EditText email;
    private EditText packet_indihome;
    private EditText address;
    private String regional;
    Spinner spinner_01; //regional
    private String witel;
    Spinner spinner_02;
    Spinner s1,s2;

    ArrayAdapter<CharSequence> adapter;

    Intent infoIntent;
    Bundle b;
    Intent upsfIntent;

    SharedPreferences pref;

    RequestOptions options;

    private String apiUrl;

    private List<String> imagePathList;

    BaseApiService mApiService;

    public String pathParam;


    //..............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_sf_activity);

        imagePathList = new ArrayList<String>();

        mApiService = UtilsApi.getAPIService();

        pref = getApplicationContext().getSharedPreferences("MyPref",0);

        options = new RequestOptions().fitCenter().placeholder(R.mipmap.noimage).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true);

        //s1 = (Spinner)findViewById(R.id.spinner01);//regional
        s2 = (Spinner)findViewById(R.id.spinner02);
        //s1.setOnItemSelectedListener(this);

//        spinner_02 = (Spinner) findViewById(R.id.spinner02);
//        witel = String.valueOf(spinner_02.getSelectedItem());
        infoIntent = getIntent();
        b = getIntent().getExtras();
        upsfIntent = getIntent();


        dateFormatter = new SimpleDateFormat(
                DATE_FORMAT, Locale.US);


        //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,


//        TextView text1 = (TextView) findViewById(R.id.nama_odp);
//        TextView text2 = (TextView) findViewById(R.id.lat);
//        TextView text3 = (TextView) findViewById(R.id.lng);
//        TextView text4 = (TextView) findViewById(R.id.editHp);
//        TextView text5 = (TextView) findViewById(R.id.editEmail);
//        TextView text6 = (TextView) findViewById(R.id.editPacketIndihome);
//        TextView text7 = (TextView) findViewById(R.id.editSalesId);
//
//        //TextView text8 = (TextView) findViewById(R.id.tvDistance);
//
//
//
//        text1.setText(infoIntent.getStringExtra("nama_odp"));
//        text2.setText(Double.toString(infoIntent.getDoubleExtra("lat1", 0)));
//        text3.setText(Double.toString(infoIntent.getDoubleExtra("lng1", 0)));
//        text4.setText(infoIntent.getStringExtra("hp"));
//        text5.setText(infoIntent.getStringExtra("email"));
//        text6.setText(infoIntent.getStringExtra("packet_indihome"));
//        text7.setText(infoIntent.getStringExtra("sales_id"));

        //text8.setText(infoIntent.getStringExtra("distance"));


        //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,





        //..........................................................................................


        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx MENGISI LAT-LONG xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        TextView nama_odp = (TextView) findViewById(R.id.nama_odp);
//        TextView latOdp = (TextView) findViewById(R.id.lat);
//        TextView lngOdp = (TextView) findViewById(R.id.lng);
//        mLatitudeTextView = (TextView) findViewById(R.id.lat2);
//        mLongitudeTextView = (TextView) findViewById(R.id.lng2);
//
//        nama_odp.setText(infoIntent.getStringExtra("nama_odp"));
//        latOdp.setText(Double.toString(infoIntent.getDoubleExtra("lat1", 0)));
//        lngOdp.setText(Double.toString(infoIntent.getDoubleExtra("lng1", 0)));
//
//        mLatitudeTextView.setText(infoIntent.getStringExtra("currentlat"));
//        mLongitudeTextView.setText(infoIntent.getStringExtra("currentlng"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Process Data SF");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        checkLocationPermission();
        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx END MENGISI LAT-LONG xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


        //....................................IN ONCREATE ..........................................
        cekan = false;
        cekan2 = false;
        cekan3 = false;

        //requestStoragePermission(); // BELAKANGAN DI SET

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose2 = (Button) findViewById(R.id.buttonChoose2);
        buttonChoose3 = (Button) findViewById(R.id.buttonChoose3);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        take_picture = (Button) findViewById(R.id.take_picture);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        //editText = (EditText) findViewById(R.id.editTextName);
        ket1 = (EditText) findViewById(R.id.editTextKet1);



        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonChoose2.setOnClickListener(this);
        buttonChoose3.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        take_picture.setOnClickListener(this);





//        parse_userid = (TextView) findViewById(R.id.userid2);
//        parse_userid.setText(b.getCharSequence("parse_userid"));
//
//        parse_imei = (TextView) findViewById(R.id.imei2);
//        parse_imei.setText(b.getCharSequence("parse_imei"));
//
//        serviceno = (EditText) findViewById(R.id.servicenoValue);  //parameter utk kirim ke database
//        serviceno.setText(b.getCharSequence("serviceno"));

//        TextView nama_odp = (TextView) findViewById(R.id.nama_odp);
//        nama_odp.setText(infoIntent.getStringExtra("nama_odp"));


        latitude_odp = (TextView) findViewById(R.id.lat);  //parameter utk kirim ke database
        longitude_odp = (TextView) findViewById(R.id.lng);  //parameter utk kirim ke database

        latitude_rmh = (TextView) findViewById(R.id.lat2);  //parameter utk kirim ke database
        longitude_rmh = (TextView) findViewById(R.id.lng2);  //parameter utk kirim ke database

        latitude_odp.setText(Double.toString(infoIntent.getDoubleExtra("lat1", 0)));
        longitude_odp.setText(Double.toString(infoIntent.getDoubleExtra("lng1", 0)));

        latitude_rmh.setText(Double.toString(infoIntent.getDoubleExtra("currentlat", 0)));
        longitude_rmh.setText(Double.toString(infoIntent.getDoubleExtra("currentlng", 0)));


        stp_name = (TextView) findViewById(R.id.nama_odp);  //allready
        stp_name.setText(infoIntent.getStringExtra("nama_odp"));
        //distance = (TextView) findViewById(R.id.tvDistance);  //allready

        spinner_02 = (Spinner) findViewById(R.id.spinner02);
        adapter = ArrayAdapter.createFromResource(this,R.array.servicetype_arrays,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_02.setAdapter(adapter);
//        witel = String.valueOf(spinner_02.getSelectedItem());

        sales_id = (EditText) findViewById(R.id.editSalesId);  //parameter utk kirim ke database
        sales_id.setText(b.getCharSequence("sales_id"));

        cust_name = (EditText) findViewById(R.id.editCustomerName);  //parameter utk kirim ke database
        cust_name.setText(b.getCharSequence("cust_name"));

        cust_addr = (EditText) findViewById(R.id.editCustomerAddress);  //parameter utk kirim ke database
        cust_addr.setText(b.getCharSequence("cust_addr"));

        inst_addr = (EditText) findViewById(R.id.editInstallationAddress);  //parameter utk kirim ke database
        inst_addr.setText(b.getCharSequence("inst_addr"));

        alt_addr = (EditText) findViewById(R.id.editAlternatifAddress);

        google_addr = (EditText) findViewById(R.id.editGoogleAddr);  //parameter utk kirim ke database
        google_addr.setText(b.getCharSequence("address"));

        edtRad = (EditText) findViewById(R.id.editJarak_RAD);
        edtRad.setText(b.getCharSequence("rad"));
        edtOdo = (EditText) findViewById(R.id.editJarak_ODO);
        edtOdo.setText(b.getCharSequence("odo"));
        edtMyir = (EditText) findViewById(R.id.editNomor_MYIR);
        edtSc = (EditText) findViewById(R.id.editNomor_SC);
        alt_hp = (EditText) findViewById(R.id.editHpAlternatif);


        hp = (EditText) findViewById(R.id.editHp);  //parameter utk kirim ke database
        hp.setText(b.getCharSequence("hp"));

        email = (EditText) findViewById(R.id.editEmail);  //parameter utk kirim ke database
        email.setText(b.getCharSequence("email"));

        packet_indihome = (EditText) findViewById(R.id.editPacketIndihome);  //parameter utk kirim ke database
        packet_indihome.setText(b.getCharSequence("paket_indihome"));




            try {
                    if(upsfIntent.getStringExtra("mode").equalsIgnoreCase("update")){
                        apiUrl = Maps_Constants.UPDATE_URL;
                        latitude_odp.setText(upsfIntent.getStringExtra("lat1"));
                        longitude_odp.setText(upsfIntent.getStringExtra("lng1"));
                        latitude_rmh.setText(upsfIntent.getStringExtra("currentlat"));
                        longitude_rmh.setText(upsfIntent.getStringExtra("currentlng"));
                        stp_name.setText(upsfIntent.getStringExtra("stp_name"));
                        sales_id.setText(b.getCharSequence("sales_id"));
                        cust_name.setText(b.getCharSequence("cust_name"));
                        cust_addr.setText(b.getCharSequence("cust_addr"));
                        inst_addr.setText(b.getCharSequence("inst_addr"));
                        alt_addr.setText(b.getCharSequence("alt_addr"));
                        google_addr.setText(b.getCharSequence("address"));
                        edtRad.setText(b.getCharSequence("rad"));
                        edtOdo.setText(b.getCharSequence("odo"));
                        edtMyir.setText(b.getCharSequence("myir"));
                        edtSc.setText(b.getCharSequence("nosc"));
                        alt_hp.setText(b.getCharSequence("alt_hp"));
                        hp.setText(b.getCharSequence("hp"));
                        email.setText(b.getCharSequence("email"));
                        packet_indihome.setText(b.getCharSequence("packet_indihome"));
                        ket1.setText(b.getCharSequence("ket1"));
                        int spinnerPosition = adapter.getPosition(b.getCharSequence("witel"));
                        if (spinner_02 != null) {
                            spinner_02.setSelection(spinnerPosition);
                        }




                        Glide.with(this)
                                .asBitmap()
                                .apply(options)
                                .load(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url"))
                                .into(imageView);
//                        imagePath = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url"));

                        Glide.with(this)
                                .asBitmap()
                                .apply(options)
                                .load(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url2"))
                                .into(imageView2);
//                        imagePath2 = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url2"));

                        Glide.with(this)
                                .asBitmap()
                                .apply(options)
                                .load(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url5"))
                                .into(imageView3);
//                        imagePath3 = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url5"));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    imagePath = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url"));
                                    imagePath2 = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url2"));
                                    imagePath3 = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url5"));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else {
                        apiUrl = Maps_Constants.UPLOAD_URL;
                    }

            } catch (Exception e) {
                e.printStackTrace();
                apiUrl = Maps_Constants.UPLOAD_URL;
            }

//        address = (EditText) findViewById(R.id.editGoogleAddr);
//        address.setText(infoIntent.getStringExtra("address"));





        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx VALIDASI DATA / TEXT / IMAGE KOSONG xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

//        buttonUpload.setOnClickListener(new OnClickListener() {
//                                            @Override
//                                            public void onClick(View arg0) {
//
//                                                //if (editText.getText().toString().equals("") ||
//                                                    if (latitude_rmh.getText().toString().equals("")||
//                                                        longitude_rmh.getText().toString().equals("")||
//                                                        latitude_odp.getText().toString().equals("")||
//                                                        longitude_odp.getText().toString().equals("")||
//
//                                                        ket1.getText().toString().equals("")||
//
//                                                        //text1.getText().toString().equals("")||
//
//                                                        //sales_id.getText().toString().equals("")||
//                                                        cust_name.getText().toString().equals("")||
//
//                                                        cust_addr.getText().toString().equals("")||
//                                                        inst_addr.getText().toString().equals("")||
//
//                                                        google_addr.getText().toString().equals("")||
//
//
//                                                        hp.getText().toString().equals("")||
//                                                        email.getText().toString().equals("")||
//                                                        packet_indihome.getText().toString().equals("")||
//
//                                                        spinner_02.getSelectedItem().toString().equals("")||  //stp_port
//                                                        imageView.getDrawable() == null ||
//                                                        imageView2.getDrawable() == null)
//
//                                                {
//                                                    Toast.makeText(Maps_SF_Activity.this, "Silahkan isi text yang kosong...!!!", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Maps_SF_Activity.this, "Success...!!!", Toast.LENGTH_SHORT).show();
//
//                                                    //WARNING.......................................
//                                                    uploadMultipart(); // Next Step
//                                                    //editText.setText("");
//                                                        ket1.setText("");
//                                                    spinner_02.setAdapter(null);
//
//                                                    //sales_id.setText("");
//                                                    cust_name.setText("");
//                                                   // distance.setText("");
//
//                                                    cust_addr.setText("");
//                                                    inst_addr.setText("");
//                                                    google_addr.setText("");
//
//
//                                                    hp.setText("");
//                                                    email.setText("");
//                                                    packet_indihome.setText("");
//
//                                                    stp_name.setText("");
//
//                                                    imageView.setImageBitmap(null);
//                                                    imageView2.setImageBitmap(null);
//
//                                                }
//                                            }
//                                        }
//
//        );


//        Button capturedImageButton = (Button) findViewById(R.id.take_picture);
//        capturedImageButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(photoCaptureIntent, requestCode);
//            }
//        });
    }
    //................ END ONCREATE.................................................................
    private String getImgCachePath(String url) throws ExecutionException, InterruptedException {
        File file = Glide.with(Maps_SF_Activity.this).asFile().load(url).submit().get();
        String path = file.getAbsolutePath();
        return path;
    }


    //..............................................................................................




    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx






    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //String name = editText.getText().toString().trim();
        String ket1a = ket1.getText().toString();

        //WARNING ..................................................................................
//        String path = getPath(filePath);
//        String path2 = getPath(filePath2);
//        String path3 = getPath(filePath3);
        String path = imagePath;
        String path2 = imagePath2;
        String path3 = imagePath3;
        witel = String.valueOf(spinner_02.getSelectedItem());

        String sales_id2 = sales_id.getText().toString().trim();

        String cust_name2 = cust_name.getText().toString().trim();

        String cust_addr2 = cust_addr.getText().toString().trim();
        String inst_addr2 = inst_addr.getText().toString().trim();
        String google_addr2 = google_addr.getText().toString().trim();
        String alt_addr2 = alt_addr.getText().toString().trim();

        String rad = edtRad.getText().toString().trim();
        String odo = edtOdo.getText().toString().trim();
        String no_myir = edtMyir.getText().toString().trim();
        String no_sc = edtSc.getText().toString().trim();


        String hp2 = hp.getText().toString().trim();
        String hp_alt = alt_hp.getText().toString().trim();
        String email2 = email.getText().toString().trim();
        String packet_indihome2 = packet_indihome.getText().toString().trim();

        String latitude_odp2 = latitude_odp.getText().toString().trim();
        String longitude_odp2 = longitude_odp.getText().toString().trim();

        String latitude_rmh2 = latitude_rmh.getText().toString().trim();
        String longitude_rmh2 = longitude_rmh.getText().toString().trim();

        String stp_name2 = stp_name.getText().toString().trim();
        //String distance2 = distance.getText().toString().trim();

        String user_id = pref.getString("userid",null);
        String userid_modified = user_id.replaceFirst("^0","");
        String imei = pref.getString("imei",null);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(this, uploadId, apiUrl);
            if (apiUrl == Maps_Constants.UPLOAD_URL) {
                if (imagePath3 == null) {
                    multipartUploadRequest
                            .addFileToUpload(path, "image") //Adding file
                            .addFileToUpload(path2, "image2"); //Adding file
                }else{
                    multipartUploadRequest
                            .addFileToUpload(path, "image") //Adding file
                            .addFileToUpload(path2, "image2") //Adding file
                            .addFileToUpload(path3, "image3"); //Adding file
                }
            }else {
                multipartUploadRequest
                        .addFileToUpload(path, "image") //Adding file
                        .addFileToUpload(path2, "image2") //Adding file
                        .addFileToUpload(path3, "image3"); //Adding file
            }
                    multipartUploadRequest
                            .addParameter("idx", upsfIntent.getStringExtra("idx")) //Adding text parameter to the request
                            .addParameter("witel", witel) //Adding text parameter to the request
                            //.addParameter("name", name) //Adding text parameter to the request
                            .addParameter("ket1a", ket1a) //Adding text parameter to the request

                            .addParameter("sales_id2", sales_id2) //Adding text parameter to the request
                            .addParameter("cust_name2", cust_name2) //Adding text parameter to the request
                            //.addParameter("distance2", distance2) //Adding text parameter to the request

                            .addParameter("cust_addr2", cust_addr2) //Adding text parameter to the request
                            .addParameter("inst_addr2", inst_addr2) //Adding text parameter to the request
                            .addParameter("google_addr2", google_addr2) //Adding text parameter to the request


                            .addParameter("hp2", hp2) //Adding text parameter to the request
                            .addParameter("email2", email2) //Adding text parameter to the request
                            .addParameter("packet_indihome2", packet_indihome2) //Adding text parameter to the request

                            .addParameter("latitude_odp2", latitude_odp2) //Adding text parameter to the request
                            .addParameter("longitude_odp2", longitude_odp2) //Adding text parameter to the request

                            .addParameter("latitude_rmh2", latitude_rmh2) //Adding text parameter to the request
                            .addParameter("longitude_rmh2", longitude_rmh2) //Adding text parameter to the request

                            .addParameter("stp_name2", stp_name2) //Adding text parameter to the request
                            .addParameter("userid", userid_modified) //Adding text parameter to the request
                            .addParameter("imei", imei) //Adding text parameter to the request

                            .addParameter("alt_addr2",alt_addr2)
                            .addParameter("rad",rad)
                            .addParameter("odo",odo)
                            .addParameter("no_myir",no_myir)
                            .addParameter("no_sc",no_sc)
                            .addParameter("hp_alt",hp_alt)

                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
            Intent intent = new Intent(Maps_SF_Activity.this,SuccessActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    //..............................................................................................


    // } ?????????????????????????????????????????????????????????????????????????????????????????????


    //..............................................................................................




    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //Select Picture
    }

    private void take_picture(){
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
        String packageName = getApplicationContext().getPackageName();
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCaptureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,getPackageName(),photoFile);
//                photoCaptureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                photoCaptureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                this.grantUriPermission(packageName,photoUri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                photoCaptureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,photoUri);
                Log.d("Filepath :",packageName.toString());
                startActivityForResult(photoCaptureIntent,PICK_IMAGE_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Mytools_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName,".jpg",storageDir);

//        try{
//            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 2 , new FileOutputStream(imageFile));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        imageFile.createNewFile();
//        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
//        byte[] bitmapdata = bos.toByteArray();
//
//        FileOutputStream fos = new FileOutputStream(imageFile);
//        fos.write(bitmapdata);
//        fos.flush();
//        fos.close();

//        File compressedImageFile = new Compressor(this).compressToFile(imageFile);
        imageFilePathOri = imageFile.getAbsolutePath();
        imagePathList.add(imageFilePathOri);
//        imageFilePathCompress = compressedImageFile.getAbsolutePath();

        return imageFile;
    };

    public void compressImage(String imageUrl,int quality){
        File file = new File(imageUrl);
        try{
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality , new FileOutputStream(file));
            imageUrl = file.getAbsolutePath();
            Log.d("Image Compress :","Success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            try {
////                if (cekan){
////                    filePath = data.getData();
////                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
////                    imageView.setImageBitmap(bitmap);
////                    cekan = false;
////                }
////                if (cekan2){
////                    filePath2 = data.getData();
////                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
////                    imageView2.setImageBitmap(bitmap);
////                    cekan2 = false;
////                }
////                if (cekan3){
////                    filePath3 = data.getData();
////                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
////                    imageView3.setImageBitmap(bitmap);
////                    cekan3 = false;
////                }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {
            try {
                if (cekan){
                    Glide.with(this)
                            .asBitmap()
                            .apply(options)
                            .load(imageFilePathOri)
                            .into(imageView);

                    imagePath = imageFilePathOri;
                    cekan = false;
                }
                if (cekan2){
                    Glide.with(this)
                            .asBitmap()
                            .apply(options)
                            .load(imageFilePathOri)
                            .into(imageView2);

                    imagePath2 = imageFilePathOri;
                    cekan2 = false;
                }
                if (cekan3){
                    Glide.with(this)
                            .asBitmap()
                            .apply(options)
                            .load(imageFilePathOri)
                            .into(imageView3);

                    imagePath3 = imageFilePathOri;
                    cekan3 = false;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * This is useful when an image is not available in sdcard physically but it displays into photos application via google drive(Google Photos) and also for if image is available in sdcard physically.
     *
     * @param uriPhoto
     * @return
     */
    public String getPathFromGooglePhotosUri(Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uriPhoto, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(this);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return tempFilename;
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }
//...................................................................................................

//...................................................................................................




    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        //Cursor cursor = getContentResolver().query(uri, null, null);

        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        //WARNING...................................................................................
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }





    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    //...............................................................................................

    //...............................................................................................




    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            cekan = true;
//            showFileChooser();
            take_picture();
        }
        if (v == buttonChoose2) {
            cekan2 = true;
//            showFileChooser();
            take_picture();
        }
        if (v == buttonChoose3) {
            cekan3 = true;
//            showFileChooser();
            take_picture();
        }


        if (v == buttonUpload) {
            uploadMultipart();
        }
    }
    //...............................................................................................










    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }


    //private LocationManager locationManager; //???????????????????????????????????????????????????


    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz ACCESS LOCATION PERMISSION zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    private static final int MY_PERMISSION_LOCATION_REQUEST_CODE = 88;
    //permission for android 6 +
    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            }else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz END ACCESS LOCATION PERMISSION zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz










    @Override
    public void onConnected(Bundle bundle) {
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

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

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
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        mLatitudeTextView.setText(infoIntent.getStringExtra("currentlat"));
//        mLongitudeTextView.setText(infoIntent.getStringExtra("currentlng"));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        lat = infoIntent.getDoubleExtra("currentlat",1);
//        lng = infoIntent.getDoubleExtra("currentlng",1);
//        System.out.println("Lokasi saya sekarang............." +lat.toString());
//        TextView tvLat = (TextView) findViewById(R.id.lat2);
//
//        System.out.println("Lokasi saya sekarang............." +lng.toString());
//        TextView tvLng = (TextView) findViewById(R.id.lng2);
//
//        tvLat.setText(lat.toString());
//        tvLng.setText(lng.toString());

    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
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


    private Bitmap decodeFile(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        destFile = new File(file, "img_"
                + dateFormatter.format(new Date()).toString() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }


//    @Override
//    public void onClick(View v) {
//
//    }
}

