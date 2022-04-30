package hr.bm.scanandsave.api.repository

import androidx.lifecycle.LiveData
import hr.bm.scanandsave.api.services.ReceiptRemoteDataSource
import hr.bm.scanandsave.api.services.UserRemoteDataSource
import hr.bm.scanandsave.database.dao.CategoryDao
import hr.bm.scanandsave.database.dao.ReceiptDao
import hr.bm.scanandsave.database.dao.ReceiptItemDao
import hr.bm.scanandsave.database.dao.UserDao
import hr.bm.scanandsave.database.entities.*
import hr.bm.scanandsave.utils.Resource
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class ReceiptRepository @Inject constructor(
    private val remoteDataSource: ReceiptRemoteDataSource,
    private val localReceiptDataSource: ReceiptDao,
    private val localReceiptItemDataSource: ReceiptItemDao,
    private val localCategoryDataSource: CategoryDao
) {

    fun storeReceiptInDatabase(receipt: Receipt, items: List<ReceiptItem>, category: String) : Long {
        receipt.categoryId = getCategoryId(category)
        val receiptId = localReceiptDataSource.insert(receipt)
        items.forEach { item ->
            item.receiptId = receiptId
            storeReceiptItemInDatabase(item)
        }
        return receiptId
    }

    fun updateReceiptInDatabase(receipt: Receipt, items: List<ReceiptItem>, category: String) : Int {
        receipt.categoryId = getCategoryId(category)
        val receiptId = localReceiptDataSource.update(receipt)
        localReceiptItemDataSource.deleteAllFromAccount(receipt.receiptId)
        items.forEach { item ->
            item.receiptId = receipt.receiptId
            storeReceiptItemInDatabase(item)
        }
        return receiptId
    }

    fun repeatReceipt(receiptId: Long) {
        val receiptWithItems = getReceipt(receiptId).value
        receiptWithItems?.let {
            receiptWithItems.receipt.date = Calendar.getInstance().time
            receiptWithItems.receipt.receiptId = 0L
            receiptWithItems.items.forEach {
                it.itemId = 0
            }
            storeReceiptInDatabase(receiptWithItems.receipt, receiptWithItems.items, receiptWithItems.category.name)
        }
    }

    private fun storeReceiptItemInDatabase(item: ReceiptItem) {
        localReceiptItemDataSource.insert(item)
    }

    fun updateReceiptInDatabase(receipt: Receipt) {
        localReceiptDataSource.update(receipt)
    }

    fun deleteReceiptInDatabase(receipt: Receipt) {
        localReceiptDataSource.delete(receipt)
    }

    fun deleteReceiptByIdInDatabase(receiptId: Int) {
        localReceiptDataSource.deleteById(receiptId)
    }

    fun fetchReceipts() {

    }

    fun getReceiptWithItems(): Single<List<ReceiptWithItems>> {
        return localReceiptDataSource.getReceiptsWithItems()
    }

    fun getReceipts(): Single<List<Receipt>> {
        return localReceiptDataSource.getAll()
    }

    fun getReceipts2(): LiveData<List<Receipt>> {
        return localReceiptDataSource.getAll2()
    }

    fun getReceipt(receiptId: Long): LiveData<ReceiptWithItems> {
        return localReceiptDataSource.getReceiptWithItems(receiptId.toInt())
    }

    private fun getCategoryId(category: String): Long {
        return localCategoryDataSource.findIdByName(category)
    }

    fun getCategories() : LiveData<List<Category>> {
        return localCategoryDataSource.getAll()
    }

    fun insertCategory(category: Category): Long {
        return localCategoryDataSource.insert(category)
    }
}