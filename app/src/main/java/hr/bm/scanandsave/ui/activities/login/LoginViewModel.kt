package hr.bm.scanandsave.ui.activities.login

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

class LoginViewModel
@Inject constructor(
    application: Application,
    private val repository: SpendingRepository
) : AndroidViewModel(application) {
    private lateinit var user: LiveData<User>
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var showError: MutableLiveData<ErrorType> = MutableLiveData()

    private fun fetchUser() {
        loading.postValue(true)

        //Get current user id from SharedPreference and use it to get user from database
        val userId = getPreferenceInt(getContext(), AppConstants.CURRENT_USER_PREFERENCE)
        user = repository.getUser(userId)

        loading.postValue(false)
    }

    fun getUser(): LiveData<User> {
        return user
    }

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<ErrorType> {
        return showError
    }

    fun login(password: String) {
        loading.postValue(true)

        val encryptedPinBase64 = getPreferenceString(getContext(), AppConstants.PIN_PREFERENCE)
        val ivBytesBase64 = getPreferenceString(getContext(), AppConstants.IV_PREFERENCE)

        //Decode saved password and ivBytes with Base64
        val encryptedPassword = android.util.Base64.decode(encryptedPinBase64, android.util.Base64.NO_PADDING)
        val ivBytes = android.util.Base64.decode(ivBytesBase64, android.util.Base64.NO_PADDING)

        //If user gave correct password, handle login, if not show error text
        if (decryptData(ivBytes, encryptedPassword) == password) {
            showError.postValue(ErrorType.NO_ERROR)

            val mainActivity = Intent(getContext(), MainActivity::class.java)
            mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            getContext().startActivity(mainActivity)
        } else {
            showError.postValue(ErrorType.PASSWORD_ERROR)
        }

        loading.postValue(false)
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        //TODO check where this should be stored ("MyKeyAlias")
        val secretKeyEntry = keyStore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    private fun decryptData(ivBytes: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }

    init {
        fetchUser()
    }
}