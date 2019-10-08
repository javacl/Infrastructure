package ir.javad.infrastructure.view.mainActivity.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.javad.infrastructure.core.utils.NetworkHandler
import ir.javad.infrastructure.core.utils.ThreadProvider
import ir.javad.infrastructure.view.mainActivity.data.MainActivityDao
import ir.javad.infrastructure.view.mainActivity.data.MainActivityService
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val threadProvider: ThreadProvider,
    private val networkHandler: NetworkHandler,
    private val database: MainActivityDao,
    private val remote: MainActivityService
) : ViewModel() {

    val liveData = MutableLiveData<String>()

    init {
        liveData.value = "salam"
    }
}
