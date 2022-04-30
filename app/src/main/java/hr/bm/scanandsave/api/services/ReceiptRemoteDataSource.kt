package hr.bm.scanandsave.api.services

import hr.bm.scanandsave.base.BaseDataSource
import javax.inject.Inject

class ReceiptRemoteDataSource @Inject constructor(
    private val receiptService: ReceiptService
): BaseDataSource() {
    suspend fun getUser() = getResult {
        receiptService.getReceipts()
    }
}