package com.pwojtowicz.buybuddies.di

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.auth.TokenInterceptor
import com.pwojtowicz.buybuddies.data.api.AuthApiService
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.api.GroceryListItemApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        return context.getString(R.string.shiro_backend_ip)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(
        authClient: AuthorizationClient
    ): TokenInterceptor = TokenInterceptor(authClient)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideAuthorizationClient(
        @ApplicationContext context: Context
    ): AuthorizationClient = AuthorizationClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context)
    )

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroceryListApiService(retrofit: Retrofit): GroceryListApiService {
        return retrofit.create(GroceryListApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroceryListItemApiService(retrofit: Retrofit): GroceryListItemApiService {
        return retrofit.create(GroceryListItemApiService::class.java)
    }
}