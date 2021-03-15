package com.reino.assignment.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reino.assignment.R;
import com.reino.assignment.callback.ContactItemClickListener;
import com.reino.assignment.model.ContactModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsRecyclerViewAdapter extends PagedListAdapter<ContactModel, ContactsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ContactsRecyclerViewAda";
    private Context context;
    private ContactModel contact;
    private String number="";
    private ContactItemClickListener contactItemClickListener;
    private int count =2;

    public static DiffUtil.ItemCallback<ContactModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ContactModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ContactModel oldItem, @NonNull ContactModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContactModel oldItem, @NonNull ContactModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ContactsRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    public ContactsRecyclerViewAdapter(Context context, ContactItemClickListener contactItemClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.contactItemClickListener = contactItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        contact = getItem(position);

        holder.title.setText(contact.getName());
        if (contact.getPhone() != null) {
            number="";
            if (contact.getPhone().size() > 0) {
                number = contact.getPhone().get(0);
                for (int pos = 1; pos < contact.getPhone().size(); pos++) {
                    number = number + "\n" + contact.getPhone().get(pos);
                }
                holder.phone.setText(number);
            }
        }
        Log.d(TAG, "Photo : "+contact.getPhoto());
        if (contact.getPhoto() != null && !contact.getPhoto().equals("")) {
            Uri uri = Uri.parse(contact.getPhoto());
            Glide.with(context)
                    .load(uri)
                    .error(R.drawable.ic_user)
                    .into(holder.iv_profile);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(holder.iv_profile);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener{
        public CircleImageView iv_profile;
        public TextView title;
        public TextView phone;
        public RelativeLayout contactSelectLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            title = (TextView) itemView.findViewById(R.id.tv_name);
            phone = (TextView) itemView.findViewById(R.id.tv_phone);
            contactSelectLayout = itemView.findViewById(R.id.contact_select_layout);

            contactSelectLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (contactItemClickListener != null)
                contactItemClickListener.onItemClick(getItem(getAdapterPosition()));
        }
    }
}

