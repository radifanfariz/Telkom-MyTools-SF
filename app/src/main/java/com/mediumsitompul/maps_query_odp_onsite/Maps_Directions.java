package com.mediumsitompul.maps_query_odp_onsite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.mediumsitompul.maps_query_odp_onsite.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Maps_Directions extends AppCompatActivity {
    Activity activity;
    Context context;
    String tag_json_obj = "json_obj_req";
    String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyAmX8akgpyhr76Vg8M18I8ZFlTQCtXd1EY";
    public static String odo = "Null";
    public static Boolean responseCondition = false;

    public Maps_Directions(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void setDirections(LatLng startPoint, LatLng endPoint) throws UnsupportedEncodingException {
        String origin = URLEncoder.encode(startPoint.latitude+","+startPoint.longitude,"UTF-8");
        String destination = URLEncoder.encode(endPoint.latitude+","+endPoint.longitude,"UTF-8");
        String apiKey  = "AIzaSyAmX8akgpyhr76Vg8M18I8ZFlTQCtXd1EY";
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&key="+apiKey;
//        this.url = url;
        Log.d("Url Direction :",url);
        getMarkers(url);
    }
    public static String getDirections(){
//        this.odo = odo;
        return odo;
    }

    private void getMarkers(String url) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);

                    String getObject = jObj.getString("routes");//mediumsitompul, sesuai dgn nama di script PHP
                    JSONArray jsonArray = new JSONArray(getObject);
//                    JSONObject jsonObject = jsonArray.getJSONObject(1);

                    for (int i = 0; i < jsonArray.length(); i++) { // .............................. QUERY DATABASE

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String getRoutes = jsonObject.getString("legs");
                        JSONArray jsonArrayLegs = new JSONArray(getRoutes);
                        JSONObject jsonObjectLegs = jsonArrayLegs.getJSONObject(i);
                        String getDistance = jsonObjectLegs.getString("distance");
                        JSONObject jsonObjectText = new JSONObject(getDistance);
                        String getText = jsonObjectText.getString("text");
                        odo = getText;

                    }
                    Log.d("ODO VALUES :",odo);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        responseCondition = true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    public interface VolleyCallback{
        void onSuccess(String result) throws JSONException;
    }
}
