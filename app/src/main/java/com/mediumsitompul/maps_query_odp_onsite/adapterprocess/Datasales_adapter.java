package com.mediumsitompul.maps_query_odp_onsite.adapterprocess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mediumsitompul.maps_query_odp_onsite.Maps_DialogInteraction;
import com.mediumsitompul.maps_query_odp_onsite.Maps_SF_Activity;
import com.mediumsitompul.maps_query_odp_onsite.R;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.BaseApiService;
import com.mediumsitompul.maps_query_odp_onsite.apihelper.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Datasales_adapter extends RecyclerView.Adapter<Datasales_adapter.DataSalesViewHolder>{
    private Context ctx;
    private List<Datasales_getset> datasales_list;
    BaseApiService mApiService;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String idx,newAction,column;
    ProgressDialog progressDialog;
    Maps_DialogInteraction maps_dialogInteraction;

//    String idx = "2";
//    String newAction = "Bandung";
//    String column = "witel";

    public Datasales_adapter(Context ctx, List<Datasales_getset> datasales_list) {
        this.ctx = ctx;
        this.datasales_list = datasales_list;
    }

    @NonNull
    @Override
    public DataSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.datasales_view,null);
        return new DataSalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataSalesViewHolder holder, int position) {
        final Datasales_getset datasales_getset = datasales_list.get(position);
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Data SF");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        holder.txtDatasalesIdx.setText(datasales_getset.getIdx());
        holder.txtDatasalesTimestamp.setText(datasales_getset.getTimestamp());
        holder.txtDatasalesCustname.setText(datasales_getset.getCust_name());
        holder.txtDatasalesMyirsc.setText(datasales_getset.getMyir_sc());
        holder.txtLoginfonoStatus.setText(datasales_getset.getStatus());

        holder.idxClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = holder.txtDatasalesIdx.getText().toString();

//                maps_dialogInteraction.customDialog();
//                processDB(idx,newAction,column);
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                showUpdatedeleteDialog();
            }
        });
        holder.timestampClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = holder.txtDatasalesIdx.getText().toString();
                showUpdatedeleteDialog();
            }
        });
        holder.custnameClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = holder.txtDatasalesIdx.getText().toString();
                showUpdatedeleteDialog();
            }
        });
        holder.myirscClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = holder.txtDatasalesIdx.getText().toString();
                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                showUpdatedeleteDialog();
            }
        });
        holder.statusClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = holder.txtDatasalesIdx.getText().toString();
                showUpdatedeleteDialog();
            }
        });


    }

    @Override
    public int getItemCount() {
        return datasales_list.size();
    }

    public void showUpdatedeleteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Change Data");
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Mengubah(Update) Atau Menghapus(Delete) Data ?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSureDialog();
            }
        });
        alertDialogBuilder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSureDialog2();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void showSureDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder.setMessage("Apakah anda yakin dengan pilihan anda ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                processDB(idx,newAction,column);
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                deleteProcessDB(idx);
//                Toast.makeText(ctx, idx, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, "PROCESS DI BATALKAN", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
    public void showSureDialog2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder.setMessage("Apakah anda yakin dengan pilihan anda ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                processDB(idx);
//                updateIntent(idx);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, "PROCESS DI BATALKAN", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
    ////////////Intent to Update_SF//////////////////////
//    private void updateIntent(String idx){
//        Intent intent = new Intent(ctx, Maps_SF_Activity.class);
//        intent.putExtra("idx",idx);
//        ctx.startActivity(intent);
//    }

    public void processDB(final String idx) {
        mApiService.updateDB(idx)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray array = new JSONArray(response.body().string());
                                JSONObject jsonObject = array.getJSONObject(0);
                                if (jsonObject.getString("idx").equals(idx)){

                                    Intent dataIntent = new Intent(ctx, Maps_SF_Activity.class);
                                    //T_SALES_FORCE API
                                    String witel = jsonObject.getString("witel");

                                    String cust_name = jsonObject.getString("cust_name");
                                    String cust_addr = jsonObject.getString("cust_addr");

                                    String google_addr = jsonObject.getString("google_addr");

                                    String inst_addr = jsonObject.getString("inst_addr");

                                    String stp_name = jsonObject.getString("stp_name");

                                    String latitude_odp = jsonObject.getString("latitude_odp");
                                    String longitude_odp = jsonObject.getString("longitude_odp");

                                    String latitude_inst_addr = jsonObject.getString("latitude_inst_addr");
                                    String longitude_inst_addr = jsonObject.getString("longitude_inst_addr");

                                    String myir_sc = jsonObject.getString("myir_sc");
                                    String phone_number = jsonObject.getString("phone_number");

                                    String hp = jsonObject.getString("hp");
                                    String email = jsonObject.getString("email");
                                    String paket_indihome = jsonObject.getString("packet_indihome");

                                    String url = jsonObject.getString("url");
                                    String url2 = jsonObject.getString("url2");
                                    String url5 = jsonObject.getString("url5");

                                    String ket_1 = jsonObject.getString("ket1");

                                    String rad = jsonObject.getString("rad");
                                    String odo = jsonObject.getString("odo");

                                    String nohp = jsonObject.getString("nohp");
                                    String alt_nohp = jsonObject.getString("alt_nohp");

                                    String alt_addr = jsonObject.getString("alt_addr");
                                    String no_sc = jsonObject.getString("no_sc");

                                    dataIntent.putExtra("mode","update");

                                    dataIntent.putExtra("idx",idx);
                                    dataIntent.putExtra("cust_name",cust_name);
                                    dataIntent.putExtra("stp_name",stp_name);
                                    dataIntent.putExtra("witel",witel);
                                    dataIntent.putExtra("rad",rad);
                                    dataIntent.putExtra("odo",odo);
                                    dataIntent.putExtra("lat1",latitude_odp);
                                    dataIntent.putExtra("lng1",longitude_odp);
                                    dataIntent.putExtra("currentlat",latitude_inst_addr);
                                    dataIntent.putExtra("currentlng",longitude_inst_addr);
                                    dataIntent.putExtra("address",google_addr);
                                    dataIntent.putExtra("cust_addr",cust_addr);
                                    dataIntent.putExtra("inst_addr",inst_addr);
                                    dataIntent.putExtra("alt_addr",alt_addr);
                                    dataIntent.putExtra("myir",myir_sc);
                                    dataIntent.putExtra("nosc",no_sc);
                                    dataIntent.putExtra("hp",hp);
                                    dataIntent.putExtra("alt_hp",alt_nohp);
                                    dataIntent.putExtra("email",email);
                                    dataIntent.putExtra("packet_indihome",paket_indihome);
                                    dataIntent.putExtra("ket1",ket_1);
                                    dataIntent.putExtra("url",url);
                                    dataIntent.putExtra("url2",url2);
                                    dataIntent.putExtra("url5",url5);




                                    ctx.startActivity(dataIntent);




                                    Toast.makeText(ctx, jsonObject.getString("API BERHASIL DI MUAT"), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonObject.getString("error_msg");
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


    public void deleteProcessDB(final String idx) {
        mApiService.deleteDB(idx)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                    if (jsonRESULTS.getString("error").equals("false")) {
                                        Toast.makeText(ctx, "DATA BERHASIL DI HAPUS", Toast.LENGTH_SHORT).show();
//

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

    public static class DataSalesViewHolder extends RecyclerView.ViewHolder {
        TextView txtDatasalesIdx;
        TextView txtDatasalesTimestamp;
        TextView txtDatasalesCustname;
        TextView txtDatasalesMyirsc;
        TextView txtLoginfonoStatus;
        CardView idxClick;
        CardView timestampClick;
        CardView custnameClick;
        CardView myirscClick;
        CardView statusClick;

        public DataSalesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtDatasalesIdx = itemView.findViewById(R.id.txtDatasalesIdx);
            this.txtDatasalesTimestamp = itemView.findViewById(R.id.txtDatasalesTimestamp);
            this.txtDatasalesCustname = itemView.findViewById(R.id.txtDatasalesCustName);
            this.txtDatasalesMyirsc = itemView.findViewById(R.id.txtDatasalesMyirSc);
            this.txtLoginfonoStatus = itemView.findViewById(R.id.txtDatasalesStatus);
            this.idxClick = itemView.findViewById(R.id.idxClick);
            this.timestampClick = itemView.findViewById(R.id.timestampClick);
            this.custnameClick = itemView.findViewById(R.id.custnameClick);
            this.myirscClick = itemView.findViewById(R.id.myirscClick);
            this.statusClick = itemView.findViewById(R.id.statusClick);
        }
    }
}
