package ir.javad.infrastructure.core.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import ir.javad.infrastructure.App
import ir.javad.infrastructure.BuildConfig
import ir.javad.infrastructure.core.local.db.InfrastructureDb
import ir.javad.infrastructure.core.utils.NetworkHandler
import ir.javad.infrastructure.core.utils.ThreadProvider
import ir.javad.infrastructure.core.utils.api.ApplicationJsonAdapterFactory
import ir.javad.infrastructure.view.mainActivity.ui.MainActivityModule
import kotlinx.coroutines.Dispatchers
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class,
        MainActivityModule::class
    ]
)

class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): CustomInterceptor =
        CustomInterceptor()


    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        customInterceptor: CustomInterceptor
    ): OkHttpClient {

        val timeOut = 30L

        val dispatcher = Dispatcher(Executors.newFixedThreadPool(20))
        dispatcher.maxRequests = 20
        dispatcher.maxRequestsPerHost = 20

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            .dispatcher(dispatcher)
            .connectionPool(ConnectionPool(100, timeOut, TimeUnit.SECONDS))
            .addInterceptor(customInterceptor)
            .addInterceptor(getRedirectInterceptor())
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)

        if (Build.VERSION.SDK_INT < 20) {
            okHttpClientBuilder.sslSocketFactory(
                TLSSocketFactory(),
                TLSSocketFactory().trustManager
            )
        }

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient, moshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideJson(): Moshi {
        return Moshi.Builder()
            .add(ApplicationJsonAdapterFactory.INSTANCE)
            .build()
    }

    @Singleton
    @Provides
    fun provideDb(context: Context): InfrastructureDb {
        return Room
            .databaseBuilder(context, InfrastructureDb::class.java, BuildConfig.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(
            BuildConfig.PREFS_NAME,
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideThreadProvider() = ThreadProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    @Singleton
    @Provides
    fun provideNetworkHandler(context: Context): NetworkHandler {
        return NetworkHandler(context)
    }

    private fun getRedirectInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            var response = chain.proceed(request)
            if (response.code() == 301 || response.code() == 307 || response.code() == 308) {
                request = request.newBuilder()
                    .url(response.header("Location") ?: "")
                    .build()

                response = chain.proceed(request)
            }

            response
        }
    }

}