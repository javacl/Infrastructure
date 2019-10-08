package ir.javad.infrastructure.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.javad.infrastructure.R.style
import javax.inject.Inject

abstract class BaseDialog<T : ViewDataBinding, V : ViewModel> :
    DialogFragment() {

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
        setStyle(
            STYLE_NO_FRAME,
            style.DialogTheme
        )
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
        ViewCompat.setLayoutDirection(
            view,
            ViewCompat.LAYOUT_DIRECTION_LTR
        )
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
}