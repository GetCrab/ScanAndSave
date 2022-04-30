package hr.bm.scanandsave.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bm.scanandsave.database.entities.Category
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptWithItems
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT * FROM Category WHERE categoryId LIKE :id")
    fun findById(id: Int): Single<Category>

    @Query("SELECT * FROM Category WHERE name LIKE :name")
    fun findByName(name: String): Single<Category>

    @Query("SELECT categoryId FROM Category WHERE name LIKE :name")
    fun findIdByName(name: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(category: List<Category>)

    @Delete
    fun delete(category: Category)

    @Update
    fun update(vararg category: Category): Int
}