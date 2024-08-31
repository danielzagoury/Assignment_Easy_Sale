package com.example.assignment_easy_sale.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.assignment_easy_sale.Classes.User;
import com.example.assignment_easy_sale.databinding.RvUserBinding;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;
    private final OnItemLongClickListener onItemLongClickListener;

    public UsersAdapter(List<User> users, OnItemLongClickListener onItemLongClickListener) {
        this.users = users;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvUserBinding binding = RvUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
        holder.itemView.setOnLongClickListener(v -> {
            onItemLongClickListener.onItemLongClick(user);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(User user);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private RvUserBinding binding;

        public UserViewHolder(RvUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.RVFirstName.setText(user.getFirst_name());
            binding.RVLastName.setText(user.getLast_name());
            binding.RVEmail.setText(user.getEmail());

            String avatarUrl = user.getAvatar() != null && !user.getAvatar().isEmpty()
                    ? user.getAvatar()
                    : "https://reqres.in/img/faces/" + user.getId() + "-image.jpg";

            Glide.with(binding.getRoot().getContext())
                    .load(avatarUrl)
                    .into(binding.RVImageView);
        }
    }
}
