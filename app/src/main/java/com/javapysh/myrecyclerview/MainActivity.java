package com.javapysh.myrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MyAdapter myAdapter;
    List<String> images= new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    MyViewModel myViewModel;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCall();
                        recyclerView.invalidate();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        if(!isOnline()){
            Toast.makeText(this, "Нет интернета!", Toast.LENGTH_SHORT).show();
        }else {
            setCall();
        }
        myViewModel=new ViewModelProvider(this).get(MyViewModel.class);
    }

    private void setCall(){
        APIService apiService = NetworkService.getInstance().getJSONApi();
        Call<List<String>> call = apiService.getUsers();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful()) {
                    images = response.body();
                    setAdapter(images);
                } else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(MainActivity.this, "404, Страница не найдена!"+ response.errorBody(), Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(MainActivity.this, "400, Неверный запрос!"+ response.errorBody(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(List<String> myImages){
       if (isTablet(this)) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        MyAdapter.OnStateClickListener stateClickListener = (position) -> {
            Intent intent = new Intent(MainActivity.this, ActivityImage.class);
            intent.putExtra("image", myImages.get(position));
            startActivity(intent);
        };
        myAdapter = new MyAdapter(getApplicationContext(), myImages, stateClickListener);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      myViewModel.setList(images);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        images=myViewModel.getList();
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }

//    protected void onRestoreInstanceState(Bundle state) {
//        super.onRestoreInstanceState(state);
//        state.getStringArray("key");
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putStringArrayList("key",(ArrayList<String>) images);
//    }

//    public boolean isTablet(Context context) {
//        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
//        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
//        return (xlarge || large);
//    }
}