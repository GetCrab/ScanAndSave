package hr.bm.scanandsave.ui.activities.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import hr.bm.scanandsave.database.entities.User
import hr.bm.scanandsave.api.repository.SpendingRepository
import hr.bm.scanandsave.enums.ErrorType
import hr.bm.scanandsave.ui.activities.main.MainActivity
import hr.bm.scanandsave.utils.AppConstants
import hr.bm.scanandsave.utils.getPreferenceInt
import hr.bm.scanandsave.utils.getPreferenceString
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class SearchViewModel
@Inject constructor(
    application: Application,
    private val repository: SpendingRepository
) : AndroidViewModel(application) {
    private lateinit var user: LiveData<User>
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var showError: MutableLiveData<ErrorType> = MutableLiveData()

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    init {

    }
}