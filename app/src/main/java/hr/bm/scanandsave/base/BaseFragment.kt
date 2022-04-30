package hr.bm.scanandsave.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : DaggerFragment() {
//    private var unbinder: Unbinder? = null
    private var activity: AppCompatActivity? = null
    private var disposable: CompositeDisposable? = CompositeDisposable()

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(layoutRes(), container, false)
//        unbinder = ButterKnife.bind(this, view)
        return init(inflater, container)
    }
    protected abstract fun init(@NonNull inflater: LayoutInflater, container: ViewGroup?) : View?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onPause() {
        super.onPause()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    fun addToDisposable(value: Disposable) {
        disposable!!.add(value)
    }

    val baseActivity: AppCompatActivity?
        get() = activity

    override fun onDestroyView() {
        super.onDestroyView()
//        if (unbinder != null) {
//            unbinder!!.unbind()
//            unbinder = null
//        }
    }
}