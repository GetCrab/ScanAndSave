package hr.bm.scanandsave.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.bm.scanandsave.database.dao.ReceiptDao
import hr.bm.scanandsave.database.dao.ReceiptItemDao
import hr.bm.scanandsave.database.dao.UserDao
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.database.entities.User
import androidx.sqlite.db.SupportSQLiteDatabase
import hr.bm.scanandsave.database.dao.CategoryDao
import hr.bm.scanandsave.database.entities.Category
import java.util.concurrent.Executors
import hr.bm.scanandsave.enums.Category as CategoryEnum


@Database(entities = [User::class, Receipt::class, ReceiptItem::class, Category::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SpendingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun receiptDao(): ReceiptDao
    abstract fun receiptItemDao(): ReceiptItemDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile private var instance: SpendingDatabase? = null

        fun getDatabase(context: Context): SpendingDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, SpendingDatabase::class.java, "scanAndSaveDB")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //TODO prebacit u applicationScope.launch - cooroutines
                        Executors.newSingleThreadExecutor().execute {
                            enumValues<CategoryEnum>().forEach { category ->
                                instance!!.categoryDao().insert(Category(0, category.name))
                            }
                        }
                    }
                })
            .fallbackToDestructiveMigration()
            .build()
    }
}