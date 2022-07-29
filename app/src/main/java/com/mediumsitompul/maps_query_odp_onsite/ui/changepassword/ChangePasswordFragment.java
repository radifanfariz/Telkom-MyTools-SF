package com.mediumsitompul.maps_query_odp_onsite.ui.changepassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.mediumsitompul.maps_query_odp_onsite.R;
import com.mediumsitompul.maps_query_odp_onsite.SuccessActivity;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.BaseApiService;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;
    Context ctx;
    TextInputEditText edtUserid;
    TextInputEditText edtOldPassword;
    TextInputEditText edtNewPassword;
    CardView btnSavePassword;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    BaseApiService mApiService;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changePasswordViewModel =
                ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        ctx = getContext();
//        final TextView textView = root.findViewById(R.id.text_send);
//        changePasswordViewModel.getText().observe(this, new Observer<String>() {
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
        pref = ctx.getSharedPreferences("MyPref",0);
        userid = pref.getString("userid",null);

        mApiService = UtilsApi.getAPIService();

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Change Password");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        edtUserid = view.findViewById(R.id.edt_userid_password);
        edtOldPassword = view.findViewById(R.id.edt_old_password);
        edtNewPassword = view.findViewById(R.id.edt_new_password);
        btnSavePassword = view.findViewById(R.id.card_btn_save_password);

        edtUserid.setText(userid);
        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = edtUserid.getText().toString();
                String old_password = edtOldPassword.getText().toString();
                String new_password = edtNewPassword.getText().toString();
                if (old_password.isEmpty()) {
                    edtOldPassword.setError("Tidak Boleh Kosong...!!!");
                }
                else if (new_password.isEmpty()){
                    edtNewPassword.setError("Tidak Boleh Kosong...!!!");
                }
                else {
                    changePassword(userid, old_password, new_password);
                }
            }
        });
    }

    public void changePassword(final String userid,String old_password,String new_password) {
        mApiService.changePassword(userid,old_password,new_password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    String success_message = jsonRESULTS.getString("success_msg");
                                    Toast.makeText(ctx, success_message, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ctx,SuccessActivity.class));
//                                    notifyDataSetChanged();

                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(ctx, error_message, Toast.LENGTH_SHORT).show();
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
}