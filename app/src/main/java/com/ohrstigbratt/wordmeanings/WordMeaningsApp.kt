package com.ohrstigbratt.wordmeanings

import android.app.Application
import com.ohrstigbratt.wordmeanings.data.api.BASE_URL
import com.ohrstigbratt.wordmeanings.data.api.WordnikApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

class WordMeaningsApp : Application() {

    lateinit var wordnikApi: WordnikApi

    override fun onCreate() {
        super.onCreate()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        wordnikApi = retrofit.create(WordnikApi::class.java)
    }
}