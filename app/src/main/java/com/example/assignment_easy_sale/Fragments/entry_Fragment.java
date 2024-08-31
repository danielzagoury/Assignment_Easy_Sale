package com.example.assignment_easy_sale.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.assignment_easy_sale.Adapters.UsersAdapter;
import com.example.assignment_easy_sale.Classes.User;
import com.example.assignment_easy_sale.R;
import com.example.assignment_easy_sale.ViewModel.UsersViewModel;
import com.example.assignment_easy_sale.databinding.FragmentEntryBinding;

import java.util.ArrayList;
import java.util.Collections;

public class entry_Fragment extends Fragment {

    private UsersAdapter usersAdapter;
    private FragmentEntryBinding binding;
    private UsersViewModel usersViewModel;
    private ImageView ivUserPhoto;
    private String avatarPath;
    private User currentUser;

    private ActivityResultLauncher<String> pickPhotoLauncher;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("entry_Fragment", "onCreateView called");

        binding = FragmentEntryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("entry_Fragment", "onViewCreated called");

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        setupRecyclerView();
        observeUsers();
        fetchUsers();
        setupPhotoLauncher();
        ImageView addUserIcon = binding.addusericon;
        addUserIcon.setOnClickListener(v -> showAddUserDialog());

        pickPhotoLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                avatarPath = uri.toString();
                if (ivUserPhoto != null) {
                    Glide.with(requireContext()).load(uri).into(ivUserPhoto);  // Load the image into ImageView
                }
            }
        });
    }


    private void setupRecyclerView() {
        Log.d("entry_Fragment", "Setting up RecyclerView");

        usersAdapter = new UsersAdapter(
                Collections.emptyList(),
                this::showOptionsDialog
        );
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsers.setAdapter(usersAdapter);
    }

    private void observeUsers() {
        Log.d("entry_Fragment", "Observing users data");
        usersViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            Log.d("entry_Fragment", "Users data changed, updating RecyclerView");
            if (users != null) {
                usersAdapter.updateData(users);
            }
        });
    }

    private void fetchUsers() {
        Log.d("entry_Fragment", "Fetching users data");
        usersViewModel.fetchUsers();
    }

    private void showAddUserDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivUserPhoto = dialog.findViewById(R.id.ivUserPhoto);
        Button btnAddPhoto = dialog.findViewById(R.id.btnAddPhoto);
        EditText etFirstName = dialog.findViewById(R.id.etFirstName);
        EditText etLastName = dialog.findViewById(R.id.etLastName);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        Button btnSave = dialog.findViewById(R.id.btnSaveadduser);

        btnAddPhoto.setOnClickListener(v -> {
            pickPhotoLauncher.launch("image/*");
        });

        btnSave.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
                User newUser = new User(0, email, firstName, lastName, avatarPath);
                usersViewModel.addUser(newUser);
                dialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updateUserPhoto(User user, String photoUri) {
        user.setAvatar(photoUri);
        usersViewModel.updateUser(user);
    }
    private void setupPhotoLauncher() {
        pickPhotoLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String photoUri = uri.toString();
                if (currentUser != null) {
                    updateUserPhoto(currentUser, photoUri);
                    Glide.with(requireContext()).load(photoUri).into(ivUserPhoto);
                }
            }
        });
    }

    private void showEditUserDialog(@NonNull User user) {

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_edit_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivUserPhoto = dialog.findViewById(R.id.ivUserPhoto);
        Button btnChangePhoto = dialog.findViewById(R.id.btnChangePhoto);
        EditText etFirstName = dialog.findViewById(R.id.etFirstName);
        EditText etLastName = dialog.findViewById(R.id.etLastName);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        Button btnSave = dialog.findViewById(R.id.btnSaveedituser);

        currentUser = user;
        etFirstName.setText(user.getFirst_name());
        etLastName.setText(user.getLast_name());
        etEmail.setText(user.getEmail());

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(requireContext()).load(user.getAvatar()).into(ivUserPhoto);
        }

        btnChangePhoto.setOnClickListener(v -> {
            pickPhotoLauncher.launch("image/*");
        });

        btnSave.setOnClickListener(v -> {
            user.setFirst_name(etFirstName.getText().toString());
            user.setLast_name(etLastName.getText().toString());
            user.setEmail(etEmail.getText().toString());

            if (avatarPath != null && !avatarPath.isEmpty()) {
                user.setAvatar(avatarPath);
            }

            usersViewModel.updateUser(user);
            dialog.dismiss();
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showOptionsDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select an Action")
                .setItems(new CharSequence[]{"Update", "Delete"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Update
                            Log.d("entry_Fragment", "Update selected for user: " + user.getFirst_name());
                            showEditUserDialog(user);
                            Toast.makeText(getContext(), "Edit User: " + user.getFirst_name(), Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Delete
                            usersViewModel.deleteUser(user);
                            break;
                    }
                })
                .create()
                .show();
    }
}




