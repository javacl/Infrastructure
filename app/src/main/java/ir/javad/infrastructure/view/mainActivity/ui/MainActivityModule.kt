package ir.javad.infrastructure.view.mainActivity.ui

import dagger.Module
import dagger.Provides
import ir.javad.infrastructure.core.local.db.InfrastructureDb
import ir.javad.infrastructure.view.mainActivity.data.MainActivityDao
import ir.javad.infrastructure.view.mainActivity.data.MainActivityService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class MainActivityModule {

    @Singleton
    @Provides
    fun provideFundService(retrofit: Retrofit): MainActivityService {
        return retrofit.create(MainActivityService::class.java)
    }

    @Singleton
    @Provides
    fun provideFundDao(db: InfrastructureDb): MainActivityDao {
        return db.mainActivityDao()
    }
}