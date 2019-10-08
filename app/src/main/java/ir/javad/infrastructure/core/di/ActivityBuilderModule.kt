package ir.javad.infrastructure.core.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.javad.infrastructure.view.mainActivity.ui.MainActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity
}