package ir.javad.infrastructure

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ir.javad.infrastructure.core.di.DaggerAppComponent

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}