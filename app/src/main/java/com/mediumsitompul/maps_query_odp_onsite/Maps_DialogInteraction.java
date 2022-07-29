package com.mediumsitompul.maps_query_odp_onsite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;


public class Maps_DialogInteraction extends AppCompatActivity {

    public double radius = 0.2;
    private int itemSelected = 0;
    private boolean[] checkedItems = {true, true};

    public void setCheckBox(final Context ctx, final GoogleMap gMap){

        final String[] multiChoiceItems = ctx.getResources().getStringArray(R.array.dialog_multi_choice_array);
        new AlertDialog.Builder(ctx)
                .setTitle("Select Map Setting")
                .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                        Log.d("setChackBox", " index is " + index);
                        switch (index){
                            case 0:
                                if (isChecked){
                                    checkedItems[index] = true;
                                    gMap.setMapType(2);
                                    break;
                                }else {
                                    checkedItems[index] = false;
                                    gMap.setMapType(1);
                                    break;
                                }
                            case 1:
                                if (isChecked){
                                    checkedItems[index] = true;
                                    gMap.setTrafficEnabled(true);
                                    break;
                                }else {
                                    checkedItems[index] = false;
                                    gMap.setTrafficEnabled(false);
                                    break;
                                }
                        }
                    }
                })
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();
    }
    public void setRadioBox(final Context ctx){

        String[] singleChoiceItems = ctx.getResources().getStringArray(R.array.dialog_single_choice_array);
        new AlertDialog.Builder(ctx)
                .setTitle("Select Radius")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        Log.d("setRadioBox", " index is " + index);
                        switch (index){
                            case 0:
                                setRadius(0.2,index);
                                break;
                            case 1:
                                setRadius(0.3,index);
                                break;
                            case 2:
                                setRadius(0.4,index);
                                break;
                            case 3:
                                setRadius(0.5,index);
                                break;
                        }
                    }
                })
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();
    }
    public void setRadius(double radius,int itemSelected){
        this.radius = radius;
        this.itemSelected = itemSelected;
    }

    public void customDialog(Activity activity){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(activity).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}

