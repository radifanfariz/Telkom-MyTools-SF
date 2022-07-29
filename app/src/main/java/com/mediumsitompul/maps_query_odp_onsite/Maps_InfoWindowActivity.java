package com.mediumsitompul.maps_query_odp_onsite;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;



public class Maps_InfoWindowActivity implements GoogleMap.InfoWindowAdapter {

    private Context context;
    public static String name_odp = "No Data";
    public static String distance_rad = "No Data";
    public static String distance_odo = "No Data";
    public static String total = "No Data ";
    public static String service = "No Data";
    public static String avai = "No Data";
    public static String occ = "No Data";
    public static String lat = "No Data";
    public static String lng = "No Data";

    public Maps_InfoWindowActivity(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.maps_infomarker,null);

        TextView name_odp = (TextView) view.findViewById(R.id.nama_odp);
        TextView distance_rad = (TextView) view.findViewById(R.id.tvDistance);
        TextView distance_odo = (TextView) view.findViewById(R.id.tvDistance_odo);
        TextView  total = (TextView) view.findViewById(R.id.total);
        TextView service = (TextView) view.findViewById(R.id.service);
        TextView avai = (TextView) view.findViewById(R.id.avai);
        TextView occ = (TextView) view.findViewById(R.id.occupancy);
        TextView lat = (TextView) view.findViewById(R.id.lat2);
        TextView lng = (TextView) view.findViewById(R.id.lng2);

        Maps_InfoWindowData infoWIndowData = (Maps_InfoWindowData) marker.getTag();


        try {
            name_odp.setText(infoWIndowData.getNameodp());
            distance_rad.setText(infoWIndowData.getDistanceRad());
            distance_odo.setText(infoWIndowData.getDistanceOdo());
            total.setText(infoWIndowData.getTotal());
            service.setText(infoWIndowData.getService());
            avai.setText(infoWIndowData.getAvai());
            occ.setText(infoWIndowData.getOcc());
            lat.setText(infoWIndowData.getLat());
            lng.setText(infoWIndowData.getLng());
        } catch (Exception e) {
            e.printStackTrace();
            name_odp.setText(Maps_InfoWindowActivity.name_odp);
            distance_rad.setText(Maps_InfoWindowActivity.distance_rad);
            distance_odo.setText(Maps_InfoWindowActivity.distance_odo);
            total.setText(Maps_InfoWindowActivity.total);
            service.setText(Maps_InfoWindowActivity.service);
            avai.setText(Maps_InfoWindowActivity.avai);
            occ.setText(Maps_InfoWindowActivity.occ);
            lat.setText(Maps_InfoWindowActivity.lat);
            lng.setText(Maps_InfoWindowActivity.lng);
        }




        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.maps_infomarker,null);
//
//        TextView name_odp = (TextView) view.findViewById(R.id.nama_odp);
//        TextView distance = (TextView) view.findViewById(R.id.tvDistance);
//        TextView  total = (TextView) view.findViewById(R.id.total);
//        TextView service = (TextView) view.findViewById(R.id.service);
//        TextView avai = (TextView) view.findViewById(R.id.avai);
//        TextView occ = (TextView) view.findViewById(R.id.occupancy);
//        TextView lat = (TextView) view.findViewById(R.id.lat2);
//        TextView lng = (TextView) view.findViewById(R.id.lng2);
//
//        Maps_InfoWindowData infoWIndowData = (Maps_InfoWindowData) marker.getTag();
//
//        name_odp.setText(infoWIndowData.getNameodp());
//        distance.setText(infoWIndowData.getDistance());
//        total.setText(infoWIndowData.getTotal());
//        service.setText(infoWIndowData.getService());
//        avai.setText(infoWIndowData.getAvai());
//        occ.setText(infoWIndowData.getOcc());
//        lat.setText(infoWIndowData.getLat());
//        lng.setText(infoWIndowData.getLng());
//
//
//
//
//        return view;
        return null;
    }
}

