package hr.bm.scanandsave.base

import androidx.lifecycle.LiveData
import hr.bm.scanandsave.utils.Resource
import hr.bm.scanandsave.utils.hasInternetConnection
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<LiveData<T>>): Resource<LiveData<T>> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network call has failed for a following reason: $message")
    }

}