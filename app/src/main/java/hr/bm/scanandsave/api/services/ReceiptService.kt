package hr.bm.scanandsave.api.services

import androidx.lifecycle.LiveData
import hr.bm.scanandsave.api.responses.ReceiptResponse
import hr.bm.scanandsave.api.responses.UserResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface ReceiptService {
    @GET("receipts")
    fun getReceipts(): Response<LiveData<ReceiptResponse>>
}