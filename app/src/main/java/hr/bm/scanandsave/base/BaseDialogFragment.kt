package hr.bm.scanandsave.base

import androidx.fragment.app.DialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseDialogFragment: DialogFragment() {

    private var disposable: CompositeDisposable? = CompositeDisposable()

    override fun onPause() {
        super.onPause()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    fun addToDisposable(value: Disposable) {
        disposable!!.add(value)
    }
}