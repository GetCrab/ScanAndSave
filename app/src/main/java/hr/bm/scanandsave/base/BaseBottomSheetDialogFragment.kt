package hr.bm.scanandsave.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseBottomSheetDialogFragment: BottomSheetDialogFragment() {

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