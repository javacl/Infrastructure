package ir.javad.infrastructure.core.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.javad.infrastructure.R
import ir.javad.infrastructure.R.style
import javax.inject.Inject

abstract class BaseSheet<T : ViewDataBinding, V : ViewModel> :
    BottomSheetDialogFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V

    abstract val bindingVariable: Int
    abstract val viewModel: V
    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun beforeView()
    abstract fun afterView()
    abstract fun destroyView()
    abstract fun resumeView()
    abstract fun pauseView()

    abstract val isKeyboard: Boolean

    override fun getTheme(): Int {
        return style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogs: DialogInterface ->
            val d =
                dialogs as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout?>(R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from<FrameLayout?>(
                bottomSheet
            ).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        if (dialog.window != null) if (isKeyboard) dialog.window!!.setSoftInputMode(
            LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        ) else dialog.window!!.setSoftInputMode(
            LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        return dialog
    }

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
        afterView()
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