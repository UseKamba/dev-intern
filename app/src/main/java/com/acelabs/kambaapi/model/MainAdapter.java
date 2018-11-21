package com.acelabs.kambaapi.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acelabs.kambaapi.R;
import com.acelabs.kambaapi.controller.DetailActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Activity> mActivities;
    private String mRedColor = String.valueOf(R.color.cancelledStatusColor);
    private String mPrimaryColor = String.valueOf(R.color.colorPrimary);

    public MainAdapter(Context context, ArrayList<Activity> activities){
        this.context = context;
        this.mActivities = activities;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.mTransaction_typeTv.setText(mActivities.get(position).getTransaction_type());

        if(mActivities.get(position).getTransaction_type().equals("RECHARGE")){
            holder.mAmountTv.setText(String.valueOf(mActivities.get(position).getAmount_paid() + " Kz"));
        }else {
            holder.mAmountTv.setText(String.valueOf(mActivities.get(position).getAmount()) + " Kz");
        }

        if(mActivities.get(position).getStatus().equals("CANCELLED")){
            holder.mStatusTv.setText("Cancelled");
            holder.mStatusTv.setTextColor(Color.parseColor(mRedColor));
            holder.mToTv.setText("Cancelled in " + mActivities.get(position).getCreated_at());

        }else if(mActivities.get(position).getStatus().equals("PAID")){
            holder.mStatusTv.setText("Paid");
            holder.mStatusTv.setTextColor(Color.parseColor(mPrimaryColor));
            holder.mToTv.setText("Paid in " + mActivities.get(position).getCreated_at());

        }else if (mActivities.get(position).getStatus().equals("RECHARGED")) {
            holder.mStatusTv.setText("Recharged");
            holder.mStatusTv.setTextColor(Color.parseColor(mPrimaryColor));
            holder.mToTv.setText("Recharged in " + mActivities.get(position).getCreated_at());
        }

        //Loading kamba logo image and setting to the imageview
        Picasso.get()
               .load(R.drawable.kamba_logo)
               .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTransaction_typeTv, mAmountTv, mStatusTv, mToTv;

        public ViewHolder(View itemView) {
                        super(itemView);

                    mImageView = itemView.findViewById(R.id.kamba_logo);
                    mTransaction_typeTv = itemView.findViewById(R.id.transaction_type);
                    mAmountTv = itemView.findViewById(R.id.amount);
                    mStatusTv = itemView.findViewById(R.id.status);
                    mToTv = itemView.findViewById(R.id.to);

                    itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //Checking if item still exist
                    if(pos != RecyclerView.NO_POSITION){
                        Activity clickedDataItem = mActivities.get(pos);

                        if(clickedDataItem.getTransaction_type().equals("PAYMENT")){
                            Intent intent = new Intent(context, DetailActivity.class);

                            intent.putExtra("transaction_type", clickedDataItem.getTransaction_type());
                            intent.putExtra("to", clickedDataItem.getTo().getFirstname()+" "+clickedDataItem.getTo().getLastname());
                            intent.putExtra("amount", String.valueOf(clickedDataItem.getAmount()));
                            intent.putExtra("description", clickedDataItem.getDescription());
                            intent.putExtra("date", clickedDataItem.getCreated_at());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }else if(clickedDataItem.getTransaction_type().equals("WITHDRAWAL")){
                            Intent intent = new Intent(context, DetailActivity.class);

                            intent.putExtra("transaction_type", clickedDataItem.getTransaction_type());
                            intent.putExtra("amount",String.valueOf(clickedDataItem.getAmount()));
                            if(clickedDataItem.getBank_account() == null){
                                intent.putExtra("bank", "No bank");
                            }else{
                                intent.putExtra("bank", clickedDataItem.getBank_account().getBank().getName());
                            }
                            intent.putExtra("status", clickedDataItem.getStatus());
                            intent.putExtra("date", clickedDataItem.getCreated_at());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }else if(clickedDataItem.getTransaction_type().equals("DEPOSIT")){

                            Intent intent = new Intent(context, DetailActivity.class);

                            intent.putExtra("transaction_type", clickedDataItem.getTransaction_type());
                            intent.putExtra("to", clickedDataItem.getCompany_bank_account().getHolder());
                            intent.putExtra("amount", String.valueOf(clickedDataItem.getAmount()));
                            intent.putExtra("paymentMethod", clickedDataItem.getPayment_method().getName());
                            intent.putExtra("accountType", clickedDataItem.getCompany_bank_account().getType_of());
                            intent.putExtra("bank", clickedDataItem.getCompany_bank_account().getBank().getName());
                            intent.putExtra("status", clickedDataItem.getStatus());
                            intent.putExtra("date", clickedDataItem.getCreated_at());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }else if (clickedDataItem.getTransaction_type().equals("RECHARGE")) {

                            Intent intent = new Intent(context, DetailActivity.class);

                            intent.putExtra("transaction_type", clickedDataItem.getTransaction_type());
                            intent.putExtra("phoneNumber", clickedDataItem.getPhone_number());
                            intent.putExtra("amountPaid", String.valueOf(clickedDataItem.getAmount_paid()));
                            intent.putExtra("rechargePrice", String.valueOf(clickedDataItem.getRecharge_price()));
                            intent.putExtra("amountOfUtts", String.valueOf(clickedDataItem.getAmount_of_utts()));
                            intent.putExtra("validityDays", String.valueOf(clickedDataItem.getValidity_days()));
                            intent.putExtra("mobOperatorName", clickedDataItem.getMobile_operator_name());
                            intent.putExtra("mobOperatorType", clickedDataItem.getMobile_operator_type());
                            intent.putExtra("status", clickedDataItem.getStatus());
                            intent.putExtra("date", clickedDataItem.getCreated_at());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }

                    }

                }
            });

        }
    }
}
