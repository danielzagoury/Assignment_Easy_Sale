package com.example.assignment_easy_sale.ViewModel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.assignment_easy_sale.Classes.User;
import com.example.assignment_easy_sale.Repository.UserRepository;
import com.example.assignment_easy_sale.ApiServies.ApiService;
import com.example.assignment_easy_sale.ApiServies.RetrofitInstance;
import com.example.assignment_easy_sale.ApiServies.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Response;

public class UsersViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final MutableLiveData<List<User>> usersLiveData ;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        usersLiveData = new MutableLiveData<>();
        //fetchUsers();
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }
    public void fetchUsers() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int startPage = 1;
                int endPage = 2;
                List<CompletableFuture<List<User>>> futures = IntStream.rangeClosed(startPage, endPage)
                        .mapToObj(page -> CompletableFuture.supplyAsync(() -> fetchUsersFromPage(page)))
                        .collect(Collectors.toList());

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                List<User> allUsersFromApi = futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                usersLiveData.postValue(allUsersFromApi);
                repository.insertUsers(allUsersFromApi);

                Log.d("UsersViewModel", "Fetched " + allUsersFromApi.size() + " users");
            } catch (Exception e) {
                Log.e("UsersViewModel", "Error fetching users: " + e.getMessage());
            }
        });
    }

    private List<User> fetchUsersFromPage(int page) {
        try {
            Log.d("UsersViewModel", "Fetching users from page " + page);
            Call<UserResponse> call = RetrofitInstance.getClient()
                    .create(ApiService.class)
                    .getUsers(page);

            Response<UserResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getUsers();
            }
        } catch (Exception e) {
            Log.e("UsersViewModel", "Error fetching page " + page + ": " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void deleteUser(User user) {
        repository.deleteUser(user);
        List<User> currentUsers = usersLiveData.getValue();
        if (currentUsers != null) {
            currentUsers.remove(user);
            usersLiveData.postValue(currentUsers);
        }
    }

    public void updateUser(User user) {
        repository.updateUser(user);
    }
    /*public void updateUser(User user) {
        repository.updateUser(user);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<User> updatedUsers = repository.getAllUsers().getValue();
            if (updatedUsers != null) {
                int index = -1;
                for (int i = 0; i < updatedUsers.size(); i++) {
                    if (updatedUsers.get(i).getId() == user.getId()) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    updatedUsers.set(index, user);
                    usersLiveData.postValue(updatedUsers);
                }
            }
        });
    }*/

    public void addUser(User user) {
        repository.insertUser(user);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<User> currentUsers = usersLiveData.getValue();
            if (currentUsers != null) {
                currentUsers.add(user);
                usersLiveData.postValue(currentUsers);
            }
        });
    }
}
