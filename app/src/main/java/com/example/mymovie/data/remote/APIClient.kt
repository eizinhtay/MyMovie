package com.example.mymovie.data.remote

import com.example.mymovie.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MovieAPIClient {

    companion object {

        private var retrofit: Retrofit? = null
        private val gson = GsonBuilder()
            .setLenient()
            .create()
        @Synchronized
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                val timeOut = 60

                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
                    .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val request = chain.request()
                            .newBuilder()
                            .build()
                        chain.proceed(request)
                    }
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit
        }


    }


}


