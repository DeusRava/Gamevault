package com.gamevault.app.di

import android.content.Context
import com.gamevault.app.data.api.ItadApiService
import com.gamevault.app.data.api.RawgApiService
import com.gamevault.app.data.api.SteamApiService
import com.gamevault.app.data.db.AppDatabase
import com.gamevault.app.data.db.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

    @Provides
    @Singleton
    @Named("rawg")
    fun provideRawgRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(RawgApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("steam")
    fun provideSteamRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(SteamApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("itad")
    fun provideItadRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ItadApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRawgApiService(@Named("rawg") retrofit: Retrofit): RawgApiService =
        retrofit.create(RawgApiService::class.java)

    @Provides
    @Singleton
    fun provideSteamApiService(@Named("steam") retrofit: Retrofit): SteamApiService =
        retrofit.create(SteamApiService::class.java)

    @Provides
    @Singleton
    fun provideItadApiService(@Named("itad") retrofit: Retrofit): ItadApiService =
        retrofit.create(ItadApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}
