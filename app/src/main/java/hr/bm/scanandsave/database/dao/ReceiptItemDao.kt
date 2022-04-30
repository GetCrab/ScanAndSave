package hr.bm.scanandsave.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bm.scanandsave.database.entities.ReceiptItem

@Dao
interface ReceiptItemDao {

    @Query("SELECT * FROM ReceiptItem")
    fun getAll(): LiveData<List<ReceiptItem>>

    @Query("SELECT * FROM ReceiptItem WHERE itemId LIKE :id")
    fun findById(id: Int): LiveData<ReceiptItem>

//    @Transaction
//    @Query("SELECT * FROM User WHERE userId LIKE :id")
//    fun getUserWithAccounts(id : Int): Single<UserWithAccounts>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: ReceiptItem): Long

    @Query("DELETE FROM ReceiptItem WHERE receiptId LIKE :receiptId")
    fun deleteAllFromAccount(receiptId: Long)

    @Delete
    fun delete(item: ReceiptItem)

    @Update
    fun update(vararg item: ReceiptItem)
}