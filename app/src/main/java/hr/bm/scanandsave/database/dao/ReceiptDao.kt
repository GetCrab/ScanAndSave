package hr.bm.scanandsave.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptWithItems
import hr.bm.scanandsave.database.entities.User
import io.reactivex.Single

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM Receipt ORDER BY date desc")
    fun getAll(): Single<List<Receipt>>

    @Query("SELECT * FROM Receipt WHERE receiptId LIKE :id")
    fun findById(id: Int): Single<Receipt>

    @Transaction
    @Query("SELECT * FROM Receipt ORDER BY date desc")
    fun getReceiptsWithItems(): Single<List<ReceiptWithItems>>

    @Transaction
    @Query("SELECT * FROM Receipt ORDER BY date desc")
    fun getReceiptsWithItems2(): LiveData<List<ReceiptWithItems>>
    @Query("SELECT * FROM Receipt ORDER BY date desc")
    fun getAll2(): LiveData<List<Receipt>>

    @Transaction
    @Query("SELECT * FROM Receipt WHERE receiptId LIKE :id")
    fun getReceiptWithItems(id: Int): LiveData<ReceiptWithItems>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(receipt: Receipt): Long

    @Delete
    fun delete(receipt: Receipt)

    @Query("DELETE FROM Receipt WHERE receiptId LIKE :receiptId")
    fun deleteById(receiptId: Int)

    @Update
    fun update(vararg receipt: Receipt): Int
}