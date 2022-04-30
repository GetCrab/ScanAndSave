package hr.bm.scanandsave.ui.activities.register

import android.app.Application
import android.content.Context
import android.content.Intent
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hr.bm.scanandsave.api.repository.SpendingRepository
import hr.bm.scanandsave.ui.activities.main.MainActivity
import hr.bm.scanandsave.utils.AppConstants.Companion.CURRENT_USER_PREFERENCE
import hr.bm.scanandsave.utils.AppConstants.Companion.IV_PREFERENCE
import hr.bm.scanandsave.utils.AppConstants.Companion.PIN_PREFERENCE
import hr.bm.scanandsave.utils.AppConstants.Companion.REGISTER_PREFERENCE
import hr.bm.scanandsave.utils.setPreference
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject


class RegisterViewModel
@Inject constructor(
    application: Application,
    private val repository: SpendingRepository
) : AndroidViewModel(application) {
    private var disposable: CompositeDisposable?
//    private var user: User

    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    val shouldCloseLiveData = MutableLiveData<Void?>()

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

//    private fun fetchUser() {
//        loading.postValue(true)
//
//        var user = repository.getUserByUsername()
//    }

    fun saveData(username: String, firstName: String, lastName: String, password: String) {
        savePassword(password)

        var userId = 0L
        disposable!!.add(Completable.fromAction {
            userId = repository.storeUserInDatabase(
                username,
                firstName,
                lastName
            )
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .doOnComplete {
                //Set flag for registered user and user id
                setPreference(getContext(), REGISTER_PREFERENCE, true)
                setPreference(
                    getContext(),
                    CURRENT_USER_PREFERENCE,
                    userId.toInt()
                )

                //Start transactions activity for registered user
                val mainActivity = Intent(getContext(), MainActivity::class.java)
                mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                getContext().startActivity(mainActivity)

                //Post value for removing activity observer
                shouldCloseLiveData.postValue(null)

                loading.postValue(false)
            }.doOnError {
//                Toast.makeText(
//                    getContext(), getContext().getString(R.string.saving_error),
//                    Toast.LENGTH_SHORT
//                ).show()
                loading.postValue(false)
            }.subscribe()
        )
    }

    private fun savePassword(password: String) {
        generateKey()

        val encryptedPin = encryptData(password)
        setPreference(
            getContext(), PIN_PREFERENCE,
            android.util.Base64.encodeToString(
                encryptedPin.second,
                android.util.Base64.NO_PADDING
            )
        )
        setPreference(
            getContext(), IV_PREFERENCE,
            android.util.Base64.encodeToString(
                encryptedPin.first,
                android.util.Base64.NO_PADDING
            )
        )
    }

    private fun generateKey() {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParamSpec = KeyGenParameterSpec.Builder(
            "MyKeyAlias",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParamSpec)
        keyGenerator.generateKey()
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKeyEntry = keyStore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    private fun encryptData(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")

        var temp = data
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"

        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes, encryptedBytes)
    }

    private fun getContext(): Context = getApplication<Application>().applicationContext

//    fun setUser(firstName: String, lastName: String) {
////        user.firstName = firstName
////        user.lastName = lastName
//    }

    fun getLoading() = loading

//    override fun onAuthentication() {
//        fetchUser()
//    }

    init {
        disposable = CompositeDisposable()
        loading.postValue(false)
//        user = User(0, "", "")
    }
}