package com.reino.assignment.adapter;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.reino.assignment.R;
import com.reino.assignment.callback.EditDeleteClickListener;
import com.reino.assignment.callback.SelectClickListener;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.utils.DateUtils;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends PagedListAdapter<UserModel, UserAdapter.MyViewHolder> {

    private static final String TAG = "UserAdapter";
    private String number="";
    private EditDeleteClickListener mListener;
    private SelectClickListener selectClickListener;

    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    public UserAdapter(EditDeleteClickListener listener, SelectClickListener selectClickListener) {
        super(DIFF_CALLBACK);
        this.mListener = listener;
        this.selectClickListener = selectClickListener;
    }

    //DiffUtil is used to find out whether two object in the list are same or not
    public static DiffUtil.ItemCallback<UserModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<UserModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull UserModel oldCoupon,
                                               @NonNull UserModel newCoupon) {
                    return oldCoupon.getId() == newCoupon.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull UserModel oldCoupon,
                                                  @NonNull UserModel newCoupon) {
                    return oldCoupon.equals(newCoupon);
                }
            };

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.adapter_show_list, parent, false);

        return new MyViewHolder(itemView);
    }
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final UserModel user = getItem(position);

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, user.getName());
        viewBinderHelper.closeLayout(user.getName());

        if (user.getPic() != null && !user.getPic().equals("")) {
            Uri uri = Uri.parse(user.getPic());
            holder.iv_profile.setImageURI(uri);
        } else {
            holder.iv_profile.setImageResource(R.drawable.ic_user);
        }
        holder.tvName.setText(user.getName());
        holder.tvDob.setText(user.getDob());
        setUpHeaderData(user, holder.tvHeader,position);
        if (user.getPhone() != null) {
            number="";
            if (user.getPhone().size() > 0) {
                number = user.getPhone().get(0);
                for (int pos = 1; pos < user.getPhone().size(); pos++) {
                    number = number + "\n" + user.getPhone().get(pos);
                }
                holder.tvPhone.setText(number);
            }
        }

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra(Constant.EXTRA_ID, user.getId());
                if (user.getPic() != null) {
                    intent.putExtra(Constant.EXTRA_PIC, user.getPic());
                } else {
                    intent.putExtra(Constant.EXTRA_PIC, "");
                }
                intent.putExtra(Constant.EXTRA_NAME, user.getName());
                intent.putExtra(Constant.EXTRA_DOB, user.getDob());
//                intent.putStringArrayListExtra(Constant.EXTRA_PHONE, user.getPhone());
                context.startActivity(intent);*/
                mListener.click(false, user);
                Log.d(TAG, "Clicked: Edit");
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.click(true, user);
                Log.d(TAG, "Clicked: Delete");
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpHeaderData(UserModel user, TextView dateTextView, int position) {
        if (user == null) {
            return;
        }
        Long timeStamp = Long.parseLong(user.getTimeStamp());
        Pair<String, String> timeDateForCurrentUser = DateUtils.getHeaderDateAndTime(new Date(timeStamp));
        if(position > 0){
            UserModel prevUser = getItem(position - 1);
            if(prevUser != null){
                timeStamp = Long.parseLong(prevUser.getTimeStamp());
                Pair<String, String> timeDateForPrevUser = DateUtils.getHeaderDateAndTime(new Date(timeStamp));
                if(timeDateForCurrentUser.first.toLowerCase().trim().equals(timeDateForPrevUser.first.toLowerCase().trim())){
                    dateTextView.setVisibility(View.GONE);
                } else  {
                    setHeaderDate(timeDateForCurrentUser.first,dateTextView);
                }
            } else {
                setHeaderDate(timeDateForCurrentUser.first,dateTextView);
            }
        } else {
            setHeaderDate(timeDateForCurrentUser.first,dateTextView);
        }
    }

    private void setHeaderDate(String  date, TextView dateTextView){
        dateTextView.setVisibility(View.VISIBLE);
        dateTextView.setText(date);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CircleImageView iv_profile;
        private TextView tvHeader;
        private TextView tvName;
        private TextView tvDob;
        private TextView tvPhone;
        private TextView tv_edit, tv_delete;
        private SwipeRevealLayout swipeRevealLayout;
        private RelativeLayout rl_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            rl_main = itemView.findViewById(R.id.rl_main);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tvHeader = itemView.findViewById(R.id.tv_header);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDob = itemView.findViewById(R.id.tv_dob);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tv_edit = itemView.findViewById(R.id.txtEdit);
            tv_delete = itemView.findViewById(R.id.txtDelete);

            rl_main.setOnClickListener(this);
            rl_main.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("TAG", "onClick in adapter called: " + view.getId());
            if (selectClickListener != null)
                selectClickListener.onItemClicked(view, getItem(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("TAG", "onLongClick boolean called: " + view.getId());
            if (selectClickListener != null)
                selectClickListener.onItemLongClicked(view, getItem(getAdapterPosition()), getAdapterPosition());

            return true;

        }
    }
}
