package hr.bm.scanandsave.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bm.scanandsave.database.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE userId LIKE :id")
    fun findById(id: Int): LiveData<User>

    @Query("SELECT * FROM User WHERE username LIKE :username")
    fun findByUsername(username: String): LiveData<User>

//    @Transaction
//    @Query("SELECT * FROM User WHERE userId LIKE :id")
//    fun getUserWithAccounts(id : Int): Single<UserWithAccounts>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Update
    fun update(vararg user: User)
}