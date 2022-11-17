package com.javapysh.myrecyclerview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("task-m-001/list.php")
    Call<List<String>> getUsers();
}
