package ir.javad.infrastructure.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import ir.javad.infrastructure.core.utils.ViewUtils
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> :
    DaggerFragment() {

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
        mViewModel = viewModel
        beforeView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding =
            DataBindingUtil.inflate(inflater, layoutId, container, false)
        val view = mViewDataBinding.root
        ViewUtils.directionView(view, isLtr = true)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
        afterView(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyView()
    }

    override fun onResume() {
        super.onResume()
        resumeView()
    }

    override fun onPause() {
        super.onPause()
        pauseView()
    }

    open fun initView() {}

    fun shortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun longToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}