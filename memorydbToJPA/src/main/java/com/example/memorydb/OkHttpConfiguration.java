package com.example.memorydb;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class OkHttpConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}