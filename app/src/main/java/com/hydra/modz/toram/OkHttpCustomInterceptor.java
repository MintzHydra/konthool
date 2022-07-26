package com.hydra.modz.toram;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCustomInterceptor implements Interceptor {

    private String username, password;
    private String credentials;

    public OkHttpCustomInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
        this.credentials = Credentials.basic(username, password);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }
}
