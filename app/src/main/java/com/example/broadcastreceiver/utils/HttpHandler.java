package com.example.broadcastreceiver.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    private static final String BASE_URL = "http://192.168.34.16:8080/api/users";

    // Gửi request GET lấy danh sách user
    public static String getUsers() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return readStream(conn.getInputStream());
            } else {
                return "Lỗi: " + conn.getResponseCode();
            }
        } catch (Exception e) {
            return "Lỗi kết nối: " + e.getMessage();
        }
    }

    // Gửi request POST để tạo user mới
    public static String createUser(String jsonInput) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == 201) { // 201: Created
                return readStream(conn.getInputStream());
            } else {
                return "Lỗi: " + conn.getResponseCode();
            }
        } catch (Exception e) {
            return "Lỗi kết nối: " + e.getMessage();
        }
    }

    // Đọc dữ liệu trả về từ server
    private static String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
