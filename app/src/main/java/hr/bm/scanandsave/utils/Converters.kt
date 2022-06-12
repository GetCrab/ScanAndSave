package hr.bm.scanandsave.utils

import hr.bm.scanandsave.api.responses.ReceiptResponse
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType

//fun userResponseToUser(userResponse: UserResponse, firstName: String, lastName: String): User {
//    return User(userResponse.userId!!.toLong(), firstName, lastName)
//}
//
//fun userResponseToAccounts(userResponse: UserResponse): List<Account> {
//    val accounts = mutableListOf<Account>()
//    for (account in userResponse.accounts!!) {
//        accounts.add(
//            Account(
//                account!!.id.toLong(), userResponse.userId!!.toLong(),
//                account.iban, stringToDouble(account.amount), account.currency!!
//            )
//        )
//    }
//
//    return accounts
//}
//
//fun userResponseToTransactions(userResponse: UserResponse): List<Transaction> {
//    val transactions = mutableListOf<Transaction>()
//    for (account in userResponse.accounts!!) {
//        for (transaction in account!!.transactions!!) {
//            transactions.add(
//                Transaction(
//                    transaction!!.id.toLong(),
//                    account.id.toLong(),
//                    stringToDate(transaction.date)!!,
//                    transaction.description,
//                    stringToDouble(transaction.amount),
//                    transaction.type?.let { enumValueOf<TransactionType>(it.replace(" ", "_")) }
//                )
//            )
//        }
//    }
//
//    return transactions
//}

fun receiptResponseToReceipts(receiptResponse: ReceiptResponse): List<Receipt> {
    val receipts = mutableListOf<Receipt>()
    //TODO convert
//    receipts.add(Receipt(0, "", null, 0.0, Currency.HRK, ReceiptType.DETAILED, 0))
    return receipts
}

//TODO razmisliti je li bolje ovo ili convertati kad se koristi
inline fun <reified T: Any, K> convertResponse(response: T): List<K> {
    if (T::class.java.isAssignableFrom(ReceiptResponse::class.java)) {
        return receiptResponseToReceipts(response as ReceiptResponse) as List<K>
    }
    return emptyList()
}