package com.example.broadcastreceiver.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.broadcastreceiver.R;
import com.example.broadcastreceiver.model.User;
import com.example.broadcastreceiver.network.ApiClient;
import com.example.broadcastreceiver.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RetrofitFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> userList;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = view.findViewById(R.id.listView);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        fetchUsersWithRetrofit();
        return view;
    }

    private void fetchUsersWithRetrofit() {
        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(new Gson().toJson(response.body()));
                } else {
                    Log.e("API_ERROR", "Lỗi HTTP Retrofit: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối Retrofit: " + t.getMessage());
            }
        });
    }

    private void updateUI(String jsonData) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            try {
                // Định nghĩa kiểu danh sách user
                Type listType = new TypeToken<List<User>>() {}.getType();
                List<User> users = new Gson().fromJson(jsonData, listType);

                userList.clear();
                for (User user : users) {
                    // Không ép kiểu LinkedTreeMap -> User, mà Gson đã parse đúng kiểu
                    userList.add("ID: " + user.getId() + " | " + user.getEmail() + " | " + user.getRole());
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("API_ERROR", "Lỗi xử lý JSON: " + e.getMessage());
            }
        });
    }

}
