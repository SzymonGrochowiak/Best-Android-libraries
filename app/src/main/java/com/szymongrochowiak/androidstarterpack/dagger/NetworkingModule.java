package com.szymongrochowiak.androidstarterpack.dagger;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szymongrochowiak.androidstarterpack.data.local.LocalRepository;
import com.szymongrochowiak.androidstarterpack.data.network.ApiInterface;
import com.szymongrochowiak.androidstarterpack.data.network.NetworkRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Szymon Grochowiak
 */
@Module
public class NetworkingModule {

    public static final String BASE_ENDPOINT = "http://pokeapi.co/api/v2/";
    public static final int CACHE_SIZE_MB = 40 * 1024 * 1024;  // 40 MB

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    Cache provideCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE_MB);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_ENDPOINT)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    NetworkRepository provideNetworkRepository(LocalRepository localRepository, Retrofit retrofit) {
        return new NetworkRepository(localRepository.getLocalRepositoryWriter(), retrofit.create(ApiInterface.class));
    }
}
