package com.reino.assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reino.assignment.R;
import com.reino.assignment.callback.ContactItemClickListener;
import com.reino.assignment.callback.SelectClickListener;
import com.reino.assignment.model.ContactModel;

public class SingleContactAdapter extends RecyclerView.Adapter<SingleContactAdapter.MyViewHolder> {

    private Context context;
    private ContactModel contact;

    public SingleContactAdapter(ContactModel contactModel) {
        this.contact = contactModel;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.adapter_single_contact_view, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        for (int pos = 0; pos< contact.getPhone().size(); pos++) {
            holder.tvCountryCode.setText(contact.getPhone().get(pos).substring(0,3));
            holder.tvPhone.setText(contact.getPhone().get(pos).substring(3));
        }

        if (contact.getPhoneType() != null) {
            for (int pos = 0; pos < contact.getPhoneType().size(); pos++) {
                holder.tvPhoneType.setText(contact.getPhoneType().get(pos));
            }
        }
    }

    @Override
    public int getItemCount() {
        return contact.getPhone().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCountryCode;
        private TextView tvPhone;
        public TextView tvPhoneType;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            tvCountryCode = itemView.findViewById(R.id.tv_country_code);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvPhoneType = itemView.findViewById(R.id.tv_phone_type);
        }
    }
}
