package hr.bm.scanandsave.ui.activities.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.*
import hr.bm.scanandsave.R
import hr.bm.scanandsave.api.repository.ReceiptRepository
import hr.bm.scanandsave.api.repository.SpendingRepository
import hr.bm.scanandsave.database.entities.*
import hr.bm.scanandsave.enums.ErrorType
import hr.bm.scanandsave.ui.activities.main.MainActivity
import hr.bm.scanandsave.utils.AppConstants
import hr.bm.scanandsave.utils.getPreferenceInt
import hr.bm.scanandsave.utils.getPreferenceString
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    application: Application,
    private val receiptRepository: ReceiptRepository
) : AndroidViewModel(application) {

    private var disposable: CompositeDisposable?
    private lateinit var user: LiveData<User>
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var showError: MutableLiveData<ErrorType> = MutableLiveData()

    private var simpleReceiptStore: MutableLiveData<String> = MutableLiveData()
    private var simpleReceiptAmount: MutableLiveData<Double> = MutableLiveData()

    private var categories: MutableLiveData<List<Category>> = MutableLiveData()
    private var selectedCategory: MutableLiveData<Category> = MutableLiveData()

    private var reloadReceipts: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<ErrorType> {
        return showError
    }

    fun getSimpleReceiptStore(): LiveData<String> {
        return simpleReceiptStore
    }

    fun getSimpleReceiptAmount(): LiveData<Double> {
        return simpleReceiptAmount
    }

    fun getReloadReceipts(): LiveData<Boolean> {
        return reloadReceipts
    }

    fun setReloadReceipts() {
        reloadReceipts.postValue(reloadReceipts.value?.not())
    }

    fun getCategories(): LiveData<List<Category>> {
//        return categories
        return receiptRepository.getCategories()
    }

    fun addCategory(category: Category) {
//        disposable!!.add(
//            Completable.fromAction {
//                receiptRepository.insertCategory(category)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    fetchCategories()
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.saving_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }.subscribe()
//        )
        viewModelScope.launch(Dispatchers.IO) {
            receiptRepository.insertCategory(category)
        }
    }

    fun getSelectedCategory(): LiveData<Category> {
        return selectedCategory
    }

    fun setCategory(category: Category) {
        selectedCategory.postValue(category)
    }

    private fun fetchCategories() {
//        disposable!!.add(receiptRepository.getCategories().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : DisposableSingleObserver<List<Category>>() {
//                override fun onError(e: Throwable) {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.categories_loading_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onSuccess(receiptsList: List<Category>) {
//                    categories.postValue(receiptsList)
//                }
//            })
//
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    init {
        disposable = CompositeDisposable()
//        fetchCategories()
    }
}