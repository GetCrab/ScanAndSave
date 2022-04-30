package hr.bm.scanandsave.ui.activities.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.bm.scanandsave.R
import hr.bm.scanandsave.api.repository.ReceiptRepository
import hr.bm.scanandsave.api.repository.SpendingRepository
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.database.entities.ReceiptWithItems
import hr.bm.scanandsave.database.entities.User
import hr.bm.scanandsave.enums.ErrorType
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DetailedReceiptViewModel
@Inject constructor(
    application: Application,
    private val receiptRepository: ReceiptRepository
) : AndroidViewModel(application) {

    private var disposable: CompositeDisposable?
    private lateinit var user: LiveData<User>
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var showError: MutableLiveData<ErrorType> = MutableLiveData()

    private val itemsListLiveData: MutableLiveData<List<ReceiptItem>> = MutableLiveData()
    private val itemsList: MutableList<ReceiptItem> = mutableListOf()

    private var receiptSaved: MutableLiveData<Boolean> = MutableLiveData()
    private var receiptDate: MutableLiveData<Date?> = MutableLiveData()

    private var totalPrice: Double = 0.0
    private lateinit var receipt: LiveData<ReceiptWithItems>

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun setItems(items: List<ReceiptItem>) {
        itemsList.clear()
        itemsList.addAll(items)
        itemsListLiveData.postValue(items)
    }

    fun deleteReceiptItem(item: ReceiptItem) {
        itemsList.remove(item)
        itemsListLiveData.postValue(itemsList)
    }

    fun setupRepeatReceipt() {
        receiptDate.postValue(Calendar.getInstance().time)
        itemsList.forEach {
            it.itemId = 0
        }
    }

    fun getReceiptItems(): MutableLiveData<List<ReceiptItem>> {
        return itemsListLiveData
    }

    fun getReceiptSaved(): LiveData<Boolean> {
        return receiptSaved
    }

    fun setTotalPrice(price: Double) {
        totalPrice = price
    }

    fun setDate(date: Date?) {
        receiptDate.postValue(date)
    }

    fun getDate(): LiveData<Date?> {
        return receiptDate
    }

    fun getTotalPrice(): Double {
        return totalPrice
    }

    fun addReceiptItem(item: ReceiptItem) {
        itemsList.add(item)
        itemsListLiveData.postValue(itemsList)
    }

    fun editReceiptItem(position: Int, item: ReceiptItem) {
        itemsList[position] = item
        itemsListLiveData.postValue(itemsList)
    }

    fun addReceipt(receipt: Receipt, category: String) {
//        loading.postValue(true)
//        disposable!!.add(
//            Completable.fromAction {
//                receipt.date = receiptDate.value
//                receiptRepository.storeReceiptInDatabase(receipt, itemsList, category)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    receiptSaved.postValue(true)
//                    loading.postValue(false)
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.saving_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }.subscribe()
//        )
        //TODO check what to do if error occurs in this cases
        viewModelScope.launch(Dispatchers.IO) {
            receipt.date = receiptDate.value
            receiptRepository.storeReceiptInDatabase(receipt, itemsList, category)
        }
    }

    fun deleteReceipt(receiptId: Int) {
//        loading.postValue(true)
//        disposable!!.add(
//            Completable.fromAction {
//                receiptRepository.deleteReceiptByIdInDatabase(receiptId)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    receiptSaved.postValue(true)
//                    loading.postValue(false)
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.delete_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }.subscribe()
//        )
        viewModelScope.launch(Dispatchers.IO) {
            receiptRepository.deleteReceiptByIdInDatabase(receiptId)
        }
    }

    fun updateReceipt(receipt: Receipt, category: String) {
//        loading.postValue(true)
//        disposable!!.add(
//            Completable.fromAction {
//                receipt.date = receiptDate.value
//                receiptRepository.updateReceiptInDatabase(receipt, itemsList, category)
//            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .doOnComplete {
//                    receiptSaved.postValue(true)
//                    loading.postValue(false)
//                }.doOnError {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.saving_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }.subscribe()
//        )
        viewModelScope.launch(Dispatchers.IO) {
            receipt.date = receiptDate.value
            receiptRepository.updateReceiptInDatabase(receipt, itemsList, category)
        }
    }

    fun getReceipt(): LiveData<ReceiptWithItems> {
        return receipt
    }

    fun fetchReceipt(receiptId: Long) {
//        loading.postValue(true)
//        disposable!!.add(receiptRepository.getReceipt(receiptId).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : DisposableSingleObserver<ReceiptWithItems>() {
//                override fun onError(e: Throwable) {
//                    Toast.makeText(
//                        getContext(), getContext().getString(R.string.receipts_loading_error),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loading.postValue(false)
//                }
//
//                override fun onSuccess(receiptWithItems: ReceiptWithItems) {
//                    receipt.postValue(receiptWithItems)
//                    loading.postValue(false)
//                }
//            })
//        )
        viewModelScope.launch(Dispatchers.IO) {
            receipt = receiptRepository.getReceipt(receiptId)
        }
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
        receiptDate.postValue(Calendar.getInstance().time)
    }
}