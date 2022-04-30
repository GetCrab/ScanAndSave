package hr.bm.scanandsave.base

import android.app.Application
import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.bm.scanandsave.utils.Resource
import hr.bm.scanandsave.utils.hasInternetConnection
import hr.bm.scanandsave.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

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

    fun <T> makeDatabaseCall(data: MutableLiveData<Resource<LiveData<T>>>, databaseCall: suspend () -> LiveData<T>) {
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