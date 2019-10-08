package ir.javad.infrastructure.view.mainActivity.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ir.javad.infrastructure.BR
import ir.javad.infrastructure.R
import ir.javad.infrastructure.core.base.BaseActivity
import ir.javad.infrastructure.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: MainActivityViewModel
        get() = ViewModelProvider(this, mViewModelFactory).get(MainActivityViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun beforeView() {}

    override fun afterView(savedInstanceState: Bundle?) {}

    override fun destroyView() {}

    override fun resumeView() {}

    override fun pauseView() {}
}
