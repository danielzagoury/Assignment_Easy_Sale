package com.example.assignment_easy_sale.ApiServies;

import com.example.assignment_easy_sale.Classes.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserResponse {

    private int page;

    @SerializedName("per_page")
    private int perPage;

    private int total;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("data")
    private List<User> users;

    // Constructor
    public UserResponse(int page, int perPage, int total, int totalPages, List<User> users) {
        this.page = page;
        this.perPage = perPage;
        this.total = total;
        this.totalPages = totalPages;
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
