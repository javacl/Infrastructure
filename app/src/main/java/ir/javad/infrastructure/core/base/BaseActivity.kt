package ir.javad.infrastructure.core.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel>
    : DaggerAppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V

    abstract val bindingVariable: Int
    abstract val viewModel: V
    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun beforeView()
    abstract fun afterView(savedInstanceState: Bundle?)
    abstract fun destroyView()
    abstract fun resumeView()
    abstract fun pauseView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        beforeView()
        performDataBinding()
        afterView(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        resumeView()
    }

    override fun onPause() {
        super.onPause()
        pauseView()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyView()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        val view: View = mViewDataBinding.root
        ViewCompat.setLayoutDirection(
            view,
            ViewCompat.LAYOUT_DIRECTION_LTR
        )
        mViewModel = viewModel
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    open fun initView() {}

    fun shortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun longToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}