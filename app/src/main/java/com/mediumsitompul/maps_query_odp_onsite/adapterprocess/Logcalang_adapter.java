package com.mediumsitompul.maps_query_odp_onsite.adapterprocess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mediumsitompul.maps_query_odp_onsite.R;

import java.util.List;

public class Logcalang_adapter extends RecyclerView.Adapter<Logcalang_adapter.LogcalangViewHolder> {

    private Context ctx;
    private List<Logcalang_getset> logcalang_list;

    public Logcalang_adapter(Context ctx, List<Logcalang_getset> logcalang_list) {
        this.ctx = ctx;
        this.logcalang_list = logcalang_list;
    }

    @NonNull
    @Override
    public LogcalangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.logcalang_view,null);
        return new LogcalangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogcalangViewHolder holder, int position) {
        Logcalang_getset logcalang_getset = logcalang_list.get(position);

        holder.txtLogcalangno.setText(logcalang_getset.getNo());
        holder.txtLogcalangTimestamp.setText(logcalang_getset.getTimestamp());
        holder.txtLogcalangUserid.setText(logcalang_getset.getUserid());
    }

    @Override
    public int getItemCount() {
        return logcalang_list.size();
    }

    public static class LogcalangViewHolder extends RecyclerView.ViewHolder {
        TextView txtLogcalangno;
        TextView txtLogcalangTimestamp;
        TextView txtLogcalangUserid;

        public LogcalangViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtLogcalangno = itemView.findViewById(R.id.txtLogcalangNo);
            this.txtLogcalangTimestamp = itemView.findViewById(R.id.txtLogcalangTimestamp);
            this.txtLogcalangUserid = itemView.findViewById(R.id.txtLogcalangUserid);
        }
    }

}
