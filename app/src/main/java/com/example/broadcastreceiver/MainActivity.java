package com.example.broadcastreceiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> userList;
    private OkHttpClient client;
    private Gson gson; // Thêm GSON

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo ListView
        listView = findViewById(R.id.listView);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);

        // Khởi tạo OkHttpClient và GSON
        client = new OkHttpClient();
        gson = new Gson();

        // Gọi API lấy danh sách user
        fetchUsers();
    }

    private void fetchUsers() {
        String url = "http://192.168.34.16:8080/api/users";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Lỗi kết nối: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    updateUI(result);
                } else {
                    Log.e("API_ERROR", "Lỗi HTTP: " + response.code());
                }
            }
        });
    }

    // Cập nhật UI sau khi lấy dữ liệu
    private void updateUI(String jsonData) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            try {
                // Dùng GSON để parse JSON thành List<User>
                Type listType = new TypeToken<List<User>>() {}.getType();
                List<User> users = gson.fromJson(jsonData, listType);

                userList.clear();
                for (User user : users) {
                    userList.add("ID: " + user.getId() + " | " + user.getEmail() + " | " + user.getRole());
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("API_ERROR", "Lỗi xử lý JSON: " + e.getMessage());
            }
        });
    }
}
