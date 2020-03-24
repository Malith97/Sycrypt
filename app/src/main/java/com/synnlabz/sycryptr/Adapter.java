package com.synnlabz.sycryptr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;

    public Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView textAccountName;
        public TextView textUsername;
        public TextView textPassword;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textAccountName = (TextView) itemView.findViewById(R.id.account_name);
            textUsername = (TextView) itemView.findViewById(R.id.account_username);
            textPassword = (TextView) itemView.findViewById(R.id.account_password);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Model model = arrayList.get(position);

        String id = model.getId();
        String accountname = model.getAccountname();
        String accountusername = model.getAccountusername();
        String accountpassword = model.getAccountpassword();

        holder.textAccountName.setText(accountname);
        holder.textUsername.setText(accountusername);
        holder.textPassword.setText(accountpassword);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
