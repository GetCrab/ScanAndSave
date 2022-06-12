package hr.bm.scanandsave.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import hr.bm.scanandsave.api.responses.ReceiptResponse
import hr.bm.scanandsave.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.reflect.KFunction1

open class BaseViewModel
@Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun <T> makeApiCall(context: Context, data: MutableLiveData<Resource<LiveData<T>>>, apiCall: suspend () -> Resource<LiveData<T>>,
                        databaseCall: suspend () -> LiveData<T>) {
        data.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    data.postValue(apiCall())
                } else {
                    showToast(context, "No internet connection")
//                    data.postValue(Resource.error("No internet connection"))
                    data.postValue(Resource.success(databaseCall()))
                }
            } catch (ex: Exception) {
//                when (ex) {
//                    is IOException -> data.postValue(Resource.error("Network Failure " +  ex.localizedMessage))
//                    else -> data.postValue(Resource.error("Conversion Error"))
//                }
                data.postValue(Resource.success(databaseCall()))
//                data.postValue(Resource.error("Fetching data failed"))
            }
        }
    }

    fun <T> makeApiCall(context: Context, data: MutableLiveData<Resource<LiveData<T>>>, apiCall: suspend () -> Resource<LiveData<T>>) {
        data.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    data.postValue(apiCall())
                } else {
                    data.postValue(Resource.error("No internet connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> data.postValue(Resource.error("Network Failure " +  ex.localizedMessage))
                    else -> data.postValue(Resource.error("Conversion Error"))
                }
//                data.postValue(Resource.error("Fetching data failed"))
            }
        }
    }

    fun <T> makeApiCallAndStoreData(
        context: Context, status: MutableLiveData<ResourceStatus>,
        apiCall: suspend () -> Resource<T>, databaseStoreCall: KFunction1<T?, Any>
    ) {
        status.postValue(ResourceStatus.loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    val apiData = apiCall()
//                    databaseStoreCall(apiData.data?.let { convertResponse(it) })
                    databaseStoreCall(apiData.data)
                    status.postValue(ResourceStatus.success())
                } else {
                    status.postValue(ResourceStatus.error("No internet connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> status.postValue(ResourceStatus.error("Network Failure " +  ex.localizedMessage))
                    else -> status.postValue(ResourceStatus.error("Conversion Error"))
                }
//                data.postValue(Resource.error("Fetching data failed"))
            }
        }
    }

    fun <T> makeDatabaseStoreCall(status: MutableLiveData<ResourceStatus>?, databaseCall: suspend () -> LiveData<T>): LiveData<T>? {
        status?.postValue(ResourceStatus.loading())
        var data: LiveData<T>? = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data = databaseCall()
                status?.postValue(ResourceStatus.success())
            } catch (ex: Exception) {
//                when (ex) {
//                    is IOException -> data.postValue(Resource.error("Network Failure " +  ex.localizedMessage))
//                    else -> data.postValue(Resource.error("Conversion Error"))
//                }
                status?.postValue(ResourceStatus.error("Fetching data failed"))
            }
        }
        return data
    }

    fun makeDatabaseCall(status: MutableLiveData<ResourceStatus>?, databaseCall: suspend () -> Any) {
        status?.postValue(ResourceStatus.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                databaseCall()
                status?.postValue(ResourceStatus.success())
            } catch (ex: Exception) {
//                when (ex) {
//                    is IOException -> data.postValue(Resource.error("Network Failure " +  ex.localizedMessage))
//                    else -> data.postValue(Resource.error("Conversion Error"))
//                }
                status?.postValue(ResourceStatus.error("Fetching data failed"))
            }
        }
    }

    fun <T> makeNotUpdateableDatabaseCall(data: MutableLiveData<Resource<T>>, databaseCall: suspend () -> T) {
        data.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data.postValue(Resource.success(databaseCall()))
            } catch (ex: Exception) {
//                when (ex) {
//                    is IOException -> data.postValue(Resource.error("Network Failure " +  ex.localizedMessage))
//                    else -> data.postValue(Resource.error("Conversion Error"))
//                }
                data.postValue(Resource.error("Fetching data failed"))
            }
        }
    }
}