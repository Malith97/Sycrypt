package com.synnlabz.sycryptr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;
    private RecyclerView mRecyclerview;

    public Adapter(Context context, ArrayList<Model> arrayList , RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.mRecyclerview = recyclerView;
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView textAccountName;
        public TextView textUsername;
        public AppCompatButton ViewBtn , EditBtn , DeleteBtn;


        public Holder(@NonNull View itemView) {
            super(itemView);
            textAccountName = (TextView) itemView.findViewById(R.id.account_name);
            textUsername = (TextView) itemView.findViewById(R.id.account_username);
            ViewBtn = (AppCompatButton)itemView.findViewById(R.id.btn_view);
            EditBtn = (AppCompatButton)itemView.findViewById(R.id.btn_edit);
            DeleteBtn = (AppCompatButton)itemView.findViewById(R.id.btn_delete);
        }
    }

    private void goToViewAccount(long accountId){
        Intent i = new Intent(context, ViewAccount.class);
        i.putExtra("ACCOUNT_ID", accountId);
        context.startActivity(i);
    }

    private void askPassword(long accountId){
        Intent i = new Intent(context, LoginDialog.class);
        i.putExtra("ACCOUNT_ID", accountId);
        context.startActivity(i);
    }

    private void goToEditAccount(long accountId){
        Intent i = new Intent(context, EditAccount.class);
        i.putExtra("ACCOUNT_ID", accountId);
        context.startActivity(i);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.item_account,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final Model model = arrayList.get(position);

        holder.textAccountName.setText(model.getAccountname());
        holder.textUsername.setText(model.getAccountusername());

        holder.ViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View itemView) {
                askPassword(model.getId());
                //goToViewAccount(model.getId());
            }
        });

        holder.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemView) {
                goToEditAccount(model.getId());
            }
        });

        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("WARNING!!");
                builder.setMessage("Do you want to Delete this Account?");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.deleteAccountRecord(model.getId(), context);

                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, arrayList.size());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Account Removed Successfull", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
