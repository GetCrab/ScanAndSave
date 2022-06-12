package hr.bm.scanandsave.ui.activities.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import hr.bm.scanandsave.api.repository.ReceiptRepository
import hr.bm.scanandsave.api.services.ReceiptRemoteDataSource
import hr.bm.scanandsave.database.entities.User
import hr.bm.scanandsave.base.BaseViewModel
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.database.entities.ReceiptWithItems
import hr.bm.scanandsave.enums.ErrorType
import hr.bm.scanandsave.utils.ResourceStatus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReceiptsViewModel
@Inject constructor(
    application: Application,
    private val receiptRepository: ReceiptRepository,
    private val receiptRemoteDataSource: ReceiptRemoteDataSource
) : BaseViewModel(application) {

    private var disposable: CompositeDisposable?
    private lateinit var user: LiveData<User>
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var listLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var showError: MutableLiveData<ErrorType> = MutableLiveData()

    private var receiptsWithItems: MutableLiveData<List<ReceiptWithItems>> = MutableLiveData()
    private lateinit var receipts: LiveData<List<Receipt>>
    private var status: MutableLiveData<ResourceStatus> = MutableLiveData()
    //private lateinit var receipts: LiveData<List<Receipt>>

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun getListLoading(): LiveData<Boolean> {
        return listLoading
    }

    fun getReceipts(): LiveData<List<Receipt>> {
        return receipts
    }

    fun getStatus(): LiveData<ResourceStatus> {
        return status
    }

    fun addSimpleReceipt(receipt: Receipt, items: List<ReceiptItem>, category: String) {
//        loading.postValue(true)
//        disposable!!.add(
//            Completable.fromAction {
//                receiptRepository.storeReceiptInDatabase(receipt, items, category)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    fetchReceipts()
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.saving_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }.subscribe()
//        )

//        viewModelScope.launch(Dispatchers.IO) {
//            receiptRepository.storeReceiptInDatabase(receipt, items, category)
//        }

        makeDatabaseCall(null) { receiptRepository.storeReceiptInDatabase(receipt, items, category) }
    }

    fun deleteReceipt(receipt: Receipt) {
//        loading.postValue(true)
//        disposable!!.add(
//            Completable.fromAction {
//                receiptRepository.deleteReceiptInDatabase(receipt)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    fetchReceipts()
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.delete_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }.subscribe()
//        )
//        viewModelScope.launch(Dispatchers.IO) {
//            receiptRepository.deleteReceiptInDatabase(receipt)
//        }
        makeDatabaseCall(null) { receiptRepository.deleteReceiptInDatabase(receipt) }
    }

    private fun fetchReceipts() {
//        loading.postValue(true)
//        disposable!!.add(receiptRepository.getReceipts().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : DisposableSingleObserver<List<Receipt>>() {
//                override fun onError(e: Throwable) {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.receipts_loading_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
////                    loading.postValue(false)
//                    listLoading.postValue(false)
//                }
//
//                override fun onSuccess(receiptsList: List<Receipt>) {
//                    receipts.postValue(receiptsList)
////                    loading.postValue(false)
//                    listLoading.postValue(false)
//                }
//            })
//        )

//        listLoading.postValue(true)
//        receipts = receiptRepository.getReceipts2()
//        listLoading.postValue(false)

        makeApiCallAndStoreData(getContext(), status, receiptRemoteDataSource::getUser, receiptRepository::storeReceiptListInDatabase)

        //makeApiCall(getContext(), receipts, receiptRepository.getReceipts2())
    }

    fun repeatReceipt(receiptId: Long) {
//        disposable!!.add(receiptRepository.getReceipt(receiptId).subscribeOn(Schedulers.io())
//            .subscribeWith(object : DisposableSingleObserver<ReceiptWithItems>() {
//                override fun onError(e: Throwable) {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.receipts_loading_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
////                    loading.postValue(false)
//                    listLoading.postValue(false)
//                }
//
//                override fun onSuccess(receiptWithItems: ReceiptWithItems) {
//                    receiptWithItems.receipt.date = Calendar.getInstance().time
//                    receiptWithItems.receipt.receiptId = 0L
//                    receiptWithItems.items.forEach {
//                        it.itemId = 0
//                    }
//                    receiptRepository.storeReceiptInDatabase(receiptWithItems.receipt, receiptWithItems.items, receiptWithItems.category.name)
////                    loading.postValue(false)
//                    listLoading.postValue(false)
//                }
//            })
//        )


//        viewModelScope.launch(Dispatchers.IO) {
//            receiptRepository.repeatReceipt(receiptId)
//        }

        makeDatabaseCall(null) { receiptRepository.repeatReceipt(receiptId) }
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

        viewModelScope.launch(Dispatchers.IO) {
            receipts = receiptRepository.getReceipts2()
        }

        fetchReceipts()
    }
}