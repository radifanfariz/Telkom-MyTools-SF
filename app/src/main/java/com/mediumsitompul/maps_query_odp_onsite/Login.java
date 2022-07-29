package com.mediumsitompul.maps_query_odp_onsite;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;


public class Login extends AppCompatActivity {

    TextView imei;
    TextView imei_number;
    String IMEI_Number_Holder;

    TextView subscid;
    TextView subscid_number;
    String SUBSCID_Number_Holder;


    EditText userid;

    EditText pass;


    EditText userid_parameter;

    TelephonyManager telephonyManager;

    private static final int STORAGE_PERMISSION_CODE = 123;


    public static final String MyPREFERENCES = "MyPrefs";


    Button login;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    // Date interval variables
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String CurDate, PrevDate, NextDate;
    Date curDate, prevDate, nextDate;


    public static final String ID = "nameKey";
    public static final String Passw = "phoneKey";

    SharedPreferences sharedPreferences;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        userid = (EditText) findViewById(R.id.userid);
        imei = (TextView) findViewById(R.id.imei);
        subscid = (TextView) findViewById(R.id.subscid);


        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);


        //checkLocationPermission();

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dologin dologin = new Dologin();
                dologin.execute();
            }
        });
        //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

        //IMEI

        imei_number = (TextView) findViewById(R.id.imei);
        subscid_number = (TextView) findViewById(R.id.subscid);


        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI_Number_Holder = telephonyManager.getDeviceId(); //it's oke
        imei_number.setText(IMEI_Number_Holder);

        SUBSCID_Number_Holder = telephonyManager.getSubscriberId(); //it's oke
        subscid_number.setText(SUBSCID_Number_Holder);





        isPermissionGranted(); //call AndroidPermission

        requestStoragePermission1();

    }

//,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,


    //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }




    private void onRequestPermissionsResult1(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1: { //2

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //do ur specific task after read phone state granted
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }



        }
    }


    //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK


    //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

    //Requesting permission
    private void requestStoragePermission1() {
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




    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz END ACCESS LOCATION PERMISSION zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz




    //IMEI xxxxxxxxxxxxxxxxxxxxxxxxxxxxx ANDROID PERMISSION V6 XXXXXXXXXXXX IS WORKING PRPERLY FOR ANDROID VER 6 XXXXXXXXXXXXX
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }



    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxx ANDROID PERMISSION V6 XXXXXXXXXXXX IS WORKING PRPERLY FOR ANDROID VER 6 XXXXXXXXXXXXX




    private class Dologin extends AsyncTask<String, String, String> {


        Long useridstr= Long.parseLong(userid.getText().toString());

        String useridstr1 = new Long(useridstr).toString();
        String imeistr1 = new String(imei.getText().toString());            //PROBLEM
        String subscidstr1 = new String(subscid.getText().toString());            //PROBLEM


        private String hash;
        String passstr=pass.getText().toString();
        String passhash = getMD5(pass.getText().toString());
        public String text;
        private String id;


        String z="";
        boolean isSuccess=false;

        String nm,em,password;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {



            if(useridstr1.trim().equals("")||passstr.trim().equals("")) //medium

                z = "Please enter all fields....";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {


                        //String idquery = "select userid from t_user_mobile where userid = '"+useridstr1+"' and pass='"+passhash+"' and imei='"+IMEI_Number_Holder+"' and flagging='53';";
                        //String idquery = "select userid from t_user_mobile where userid = '"+useridstr1+"' and pass='"+passhash+"' and imei='"+IMEI_Number_Holder+"' and flagging='53';";
                        //String idquery = "select userid from t_user_mobile where userid = '"+useridstr1+"' and pass='"+passhash+"' and flagging='55';";
                        String idquery = "select userid from t_user_mobile where userid = '"+useridstr1+"' and pass='"+passhash+"' and flagging='01';";
                        //String idquery = "select userid from t_user_mobile where userid = '"+useridstr1+"' and pass='"+passhash+"';";

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(idquery);


                        while (rs.next()) {

                            //}

                            if(userid != null)
                            //if (useridstr1 != "")

                            {
                                isSuccess = true;
                                z = "Login successfull";

                            } else {
                                //z = "Userid/Password is wrong";
                                isSuccess = false;
                                z = "Userid/Password is wrong";
                            }

                        }
                        //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

                    }///
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z, Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent=new Intent(Login.this,Maps_MainActivity.class); //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                //intent.putExtra("parse_destination",VAR_DESTINATION);
                intent.putExtra("parse_userid",useridstr1); // yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                intent.putExtra("parse_imei",imeistr1); // yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                intent.putExtra("parse_subscid",subscidstr1); // yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy

                pref = getApplicationContext().getSharedPreferences("MyPref",0);
                editor = pref.edit();

                editor.putString("userid",useridstr1);
                editor.putString("imei",imeistr1);

                editor.commit();




                SharedPreferences.Editor editor = sharedPreferences.edit();


                //editor.putInt(ID, useridstr1);
                editor.putString(Passw, passhash);
                editor.commit();

                startActivity(intent);
                finish();
            }


            progressDialog.hide();

        }



        public String getMD5(String text)
        {
            this.text = text;

            if(text==null)
                return null;
            else
                return makeMD5();
        }

        /**
         * Menghasilkan MD5-Hash
         * @return hasil hash
         */
        private String makeMD5()
        {
            MessageDigest md = null;
            byte[] encryptMsg = null;

            try {
                md = MessageDigest.getInstance( "MD5" );        // getting a 'MD5-Instance'
                encryptMsg = md.digest(text.getBytes());        // solving the MD5-Hash
            }catch (NoSuchAlgorithmException e) {
                System.out.println("No Such Algorithm Exception!");
            }

            String swap="";                                     // swap-string for the result
            String byteStr="";                                  // swap-string for current hex-value of byte
            StringBuffer strBuf = new StringBuffer();

            for(int i=0; i<=encryptMsg.length-1; i++) {

                byteStr = Integer.toHexString(encryptMsg[i]);   // swap-string for current hex-value of byte

                switch(byteStr.length()) {
                    case 1:                                         // if hex-number length is 1, add a '0' before
                        swap = "0"+ Integer.toHexString(encryptMsg[i]);
                        break;

                    case 2:                                         // correct hex-letter
                        swap = Integer.toHexString(encryptMsg[i]);
                        break;

                    case 8:                                         // get the correct substring
                        swap = (Integer.toHexString(encryptMsg[i])).substring(6,8);
                        break;
                }
                strBuf.append(swap);                            // appending swap to get complete hash-key
            }
            hash = strBuf.toString();                           // String with the MD5-Hash

            return hash;                                        // returns the MD5-Hash
        }
    }

}


