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

public class Loginfo_adapter extends RecyclerView.Adapter<Loginfo_adapter.LoginfoViewHolder> {

    private Context ctx;
    private List<Loginfo_getset> loginfo_list;

    public Loginfo_adapter(Context ctx, List<Loginfo_getset> loginfo_list) {
        this.ctx = ctx;
        this.loginfo_list = loginfo_list;
    }

    @NonNull
    @Override
    public LoginfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.loginfo_view,null);
        return new LoginfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginfoViewHolder holder, int position) {
        Loginfo_getset loginfo_getset = loginfo_list.get(position);

        holder.txtLoginfono.setText(loginfo_getset.getNo());
        holder.txtLoginfoTimestamp.setText(loginfo_getset.getTimestamp());
        holder.txtLoginf0Userid.setText(loginfo_getset.getUserid());
    }

    @Override
    public int getItemCount() {
        return loginfo_list.size();
    }

    public static class LoginfoViewHolder extends RecyclerView.ViewHolder {
        TextView txtLoginfono;
        TextView txtLoginfoTimestamp;
        TextView txtLoginf0Userid;

        public LoginfoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtLoginfono = itemView.findViewById(R.id.txtLoginfoNo);
            this.txtLoginfoTimestamp = itemView.findViewById(R.id.txtLoginfoTimestamp);
            this.txtLoginf0Userid = itemView.findViewById(R.id.txtLoginfoUserid);
        }
    }

}
