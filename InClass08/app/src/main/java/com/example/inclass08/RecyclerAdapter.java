package com.example.inclass08;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import okhttp3.Callback;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static Interface ainterface;
    Context ctx;

    ArrayList<Emails> user = new ArrayList<Emails>();
    private static final String TAG = "demo";

    public RecyclerAdapter(ArrayList<Emails> emails, Context inboxActivity) {
        this.user = emails;
        this.ctx = inboxActivity;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout rv_layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ainterface = (Interface) ctx;
         holder.tv_subject.setText(user.get(position).getSubject());
        holder.tv_date.setText(user.get(position).getCreatedAt());

        holder.ib_delete.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ainterface.deleteItem(position);
            Log.d(TAG, "onClick: Delete Clicked from" + user.get(position).getSubject());
        }
    });

}

    @Override
    public int getItemCount() {
        return user.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder{


    TextView tv_subject;
    TextView tv_date;
    ImageButton ib_delete;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
                tv_subject = itemView.findViewById(R.id.tv_subject);
                tv_date = itemView.findViewById(R.id.tv_date);
                ib_delete = itemView.findViewById(R.id.ib_delete);

    }
}

public interface Interface{
    void deleteItem(int position);
}

}
