package hr.bm.scanandsave.di.modules

import android.content.Context
import hr.bm.scanandsave.database.SpendingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = SpendingDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: SpendingDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideReceiptDao(db: SpendingDatabase) = db.receiptDao()

    @Singleton
    @Provides
    fun provideReceiptItemDao(db: SpendingDatabase) = db.receiptItemDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: SpendingDatabase) = db.categoryDao()
}