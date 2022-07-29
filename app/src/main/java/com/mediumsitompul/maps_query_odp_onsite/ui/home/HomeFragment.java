package com.mediumsitompul.maps_query_odp_onsite.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mediumsitompul.maps_query_odp_onsite.R;
import com.mediumsitompul.maps_query_odp_onsite.adapterprocess.Datasales_adapter;
import com.mediumsitompul.maps_query_odp_onsite.adapterprocess.Datasales_getset;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.BaseApiService;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HomeFragment extends Fragment implements EditText.OnEditorActionListener{

    private HomeViewModel homeViewModel;
    BaseApiService mApiService;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    String date_x;
    String date_y;
    Context ctx;
    List<Datasales_getset> datasales_list;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextInputEditText edttxt_dt_x,edttxt_dt_y;
    TextInputLayout datetime_layout_x,datetime_layout_y;
    CardView card_datetime_x,card_datetime_y,card_datetime_submit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ctx = getContext();
//        final TextView textView = root.findViewById(R.id.text_datasalesidx);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)  getView().findViewById(R.id.recylcerViewDatasales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Data Sales");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        edttxt_dt_x = view.findViewById(R.id.edttxt_dt_2_x);
        card_datetime_x = view.findViewById(R.id.card_datetime_2_x);
        datetime_layout_x = view.findViewById(R.id.datetime_layout_txt_2_x);
        edttxt_dt_y = view.findViewById(R.id.edttxt_dt_2_y);
        card_datetime_y = view.findViewById(R.id.card_datetime_2_y);
        datetime_layout_y = view.findViewById(R.id.datetime_layout_txt_2_y);
        card_datetime_submit = view.findViewById(R.id.card_datetime_2_submit);
        edttxt_dt_x.setOnEditorActionListener(this);
        edttxt_dt_y.setOnEditorActionListener(this);
        edttxt_dt_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerX();
//                Toast.makeText(getApplicationContext(), date_x, Toast.LENGTH_SHORT).show();
            }
        });
        edttxt_dt_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerY();
            }
        });
        card_datetime_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerX();
            }
        });
        card_datetime_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerY();
            }
        });
        card_datetime_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_x = edttxt_dt_x.getText().toString();
                date_y = edttxt_dt_y.getText().toString();
                loadDatasalesLoad(date_x,date_y,userid);
            }
        });

        datasales_list = new ArrayList<>();
        pref = getActivity().getSharedPreferences("MyPref",0);
        mApiService = UtilsApi.getAPIService();
        userid = pref.getString("userid",null);
//        loadDatasalesLoad();
    }

    public void loadDatasalesLoad(String date_x,String date_y,String userid) {
        progressDialog.show();
        mApiService.loadDataSales(date_x,date_y,userid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().string());
                                Toast.makeText(getActivity(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                datasales_list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRESULTS = jsonArray.getJSONObject(i);
                                    if (jsonRESULTS.getString("error").equals("false")) {
                                        // Jika login berhasil maka data nama yang ada di response API
                                        // akan diparsing ke activity selanjutnya.
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                        String idx = jsonRESULTS.getJSONObject("user").getString("idx");
                                        String timestamp = jsonRESULTS.getJSONObject("user").getString("timestamp");
                                        String cust_name = jsonRESULTS.getJSONObject("user").getString("cust_name");
                                        String myir_sc = jsonRESULTS.getJSONObject("user").getString("myir_sc");
                                        String status = jsonRESULTS.getJSONObject("user").getString("status");


                                        datasales_list.add(new Datasales_getset(idx, timestamp, cust_name,myir_sc,status));
                                        Datasales_adapter adapter = new Datasales_adapter(getContext(), datasales_list);
//                                        Toast.makeText(getContext(), Double.toString(adapter.getItemCount()), Toast.LENGTH_LONG).show();
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        // Jika login gagal
                                        String error_message = jsonRESULTS.getString("error_msg");
                                        Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    public void alertDialogDatePickerX(){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_datepicker, null, false);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);

        new AlertDialog.Builder(ctx).setView(view)
                .setTitle("Date Time")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();
                        date_x = year+"-"+month+"-"+day;
                        edttxt_dt_x.setText(date_x);
//                        datetime_layout_x.performClick();
//                        loadLoginfo(userid,date_x,date_y);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }).show();
    }

    public void alertDialogDatePickerY(){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_datepicker, null, false);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);

        new AlertDialog.Builder(ctx).setView(view)
                .setTitle("Date Time")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();
                        date_y = year+"-"+month+"-"+day;
                        edttxt_dt_y.setText(date_y);
//                        datetime_layout_x.performClick();
//                        loadLoginfo(userid,date_x,date_y);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == edttxt_dt_x) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    try {
                        date_x = edttxt_dt_x.getText().toString();
                        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
//                        String part[] = date_x.split("-");
//                    int part3 = Integer.parseInt(part[2]) + 1;
//                    String date_y = part[0] + "-" + part[1] + "-" + part3;
//                    Toast.makeText(getApplicationContext(), date_y, Toast.LENGTH_SHORT).show();
//                    loadLoginfo(date_x, date_y, userid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ctx, "Format Yang Anda Inputkan Salah...!!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
        }
        if (v == edttxt_dt_y){
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    try {
                        date_y = edttxt_dt_y.getText().toString();
                        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0);
//                        String part[] = date_y.split("-");
//                    int part3 = Integer.parseInt(part[2]) + 1;
//                    String date_y = part[0] + "-" + part[1] + "-" + part3;
//                    Toast.makeText(getApplicationContext(), date_y, Toast.LENGTH_SHORT).show();
//                    loadLoginfo(date_x, date_y, userid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ctx, "Format Yang Anda Inputkan Salah...!!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}