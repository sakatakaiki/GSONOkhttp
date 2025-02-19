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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> userList;
    private OkHttpClient client;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = view.findViewById(R.id.listView);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);

        client = new OkHttpClient();
        gson = new Gson();

        fetchUsersWithOkHttp();
        return view;
    }

    private void fetchUsersWithOkHttp() {
        String url = "http://192.168.34.16:8080/api/users";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Lỗi kết nối OkHttp: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    updateUI(result);
                } else {
                    Log.e("API_ERROR", "Lỗi HTTP OkHttp: " + response.code());
                }
            }
        });
    }

    private void updateUI(String jsonData) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(jsonData, listType);
            userList.clear();
            for (User user : users) {
                userList.add("ID: " + user.getId() + " | " + user.getEmail() + " | " + user.getRole());
            }
            adapter.notifyDataSetChanged();
        });
    }
}
