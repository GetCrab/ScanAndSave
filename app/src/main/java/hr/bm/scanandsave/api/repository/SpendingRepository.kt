package hr.bm.scanandsave.api.repository

import hr.bm.scanandsave.api.services.UserRemoteDataSource
import hr.bm.scanandsave.database.dao.UserDao
import hr.bm.scanandsave.database.entities.User
import javax.inject.Inject

class SpendingRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localUserDataSource: UserDao
//    private val localAccountDataSource: AccountDao,
//    private val localTransactionDataSource: TransactionDao
) {
    fun fetchUser() = remoteDataSource.getUser()
    fun storeUserInDatabase(username: String, firstName: String, lastName: String) : Long {
        val user = User(0, firstName, lastName, username)
        return localUserDataSource.insert(user)
    }
//
//    fun getUserAccounts(id: Int) = localUserDataSource.getUserWithAccounts(id)
//    fun getAccountWithTransactions(id: Long) =  localAccountDataSource.getAccountWithTransactions(id)
    fun getUser(id: Int) = localUserDataSource.findById(id)
    fun getUserByUsername(username: String) = localUserDataSource.findByUsername(username)

}