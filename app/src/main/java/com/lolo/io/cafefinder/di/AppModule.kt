package com.lolo.io.cafefinder.di

import com.google.gson.GsonBuilder
import com.lolo.io.cafefinder.data.RepositoryImpl
import com.lolo.io.cafefinder.data.network.RetrofitAPI
import com.lolo.io.cafefinder.domain.API
import com.lolo.io.cafefinder.domain.Repository
import com.lolo.io.cafefinder.ui.mobile.CafeListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single<API> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        Retrofit.Builder()
            .baseUrl("https://photon.komoot.io/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(RetrofitAPI::class.java)
    }

    single<Repository> {
        RepositoryImpl(get())
    }

    viewModel {
        CafeListViewModel(get())
    }
}