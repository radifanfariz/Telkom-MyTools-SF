package com.mediumsitompul.maps_query_odp_onsite.ui.logcalang;

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
import com.mediumsitompul.maps_query_odp_onsite.adapterprocess.Logcalang_adapter;
import com.mediumsitompul.maps_query_odp_onsite.adapterprocess.Logcalang_getset;
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

public class LogcalangFragment extends Fragment implements EditText.OnEditorActionListener {

    private LogcalangViewModel logcalangViewModel;
    BaseApiService mApiService;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    String date_x;
    String date_y;
    Context ctx;
    List<Logcalang_getset> logcalang_list;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextInputEditText edttxt_dt_x,edttxt_dt_y;
    TextInputLayout datetime_layout_x,datetime_layout_y;
    CardView card_datetime_x,card_datetime_y,card_datetime_submit;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logcalangViewModel =
                ViewModelProviders.of(this).get(LogcalangViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logcalang, container, false);
        ctx = getContext();
//        final TextView textView = root.findViewById(R.id.text_loginfo);
//        final  TextView textView2  = root.findViewById(R.id.text_loginfo2);
//        final  TextView textView3  = root.findViewById(R.id.text_loginfo3);
//        logViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        logViewModel.getText2().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView2.setText(s);
//            }
//        });
//        logViewModel.getText3().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView3.setText(s);
//            }
//        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)  getView().findViewById(R.id.recylcerViewLogcalang);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Log Calang");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        edttxt_dt_x = view.findViewById(R.id.edttxt_dt_3_x);
        card_datetime_x = view.findViewById(R.id.card_datetime_3_x);
        datetime_layout_x = view.findViewById(R.id.datetime_layout_txt_3_x);
        edttxt_dt_y = view.findViewById(R.id.edttxt_dt_3_y);
        card_datetime_y = view.findViewById(R.id.card_datetime_3_y);
        datetime_layout_y = view.findViewById(R.id.datetime_layout_txt_3_y);
        card_datetime_submit = view.findViewById(R.id.card_datetime_3_submit);
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
                loadLogcalang(date_x,date_y,userid);
            }
        });

        logcalang_list = new ArrayList<>();
        pref = getActivity().getSharedPreferences("MyPref",0);
        mApiService = UtilsApi.getAPIService();
        userid = pref.getString("userid",null);
//        loadLogcalang();
    }

    public void loadLogcalang(String date_x,String date_y,String userid) {
        progressDialog.show();
        mApiService.loadLogcalang(date_x,date_y,userid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().string());
                                Toast.makeText(getActivity(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                logcalang_list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRESULTS = jsonArray.getJSONObject(i);
                                    if (jsonRESULTS.getString("error").equals("false")) {
                                        // Jika login berhasil maka data nama yang ada di response API
                                        // akan diparsing ke activity selanjutnya.
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                        String no = jsonRESULTS.getJSONObject("user").getString("id");
                                        String timestamp = jsonRESULTS.getJSONObject("user").getString("timestamp");
                                        String userid = jsonRESULTS.getJSONObject("user").getString("userid");


                                        logcalang_list.add(new Logcalang_getset(no, timestamp, userid));
                                        Logcalang_adapter adapter = new Logcalang_adapter(getContext(), logcalang_list);
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
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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