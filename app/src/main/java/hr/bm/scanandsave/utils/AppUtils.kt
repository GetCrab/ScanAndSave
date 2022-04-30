package hr.bm.scanandsave.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import hr.bm.scanandsave.enums.ErrorType
import hr.bm.scanandsave.utils.AppConstants.Companion.PREFERENCE_NAME
import java.text.SimpleDateFormat
import java.util.*

fun setPreference(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putString(key, value)
        commit()
    }
}

fun setPreference(context: Context, key: String, value: Boolean) {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putBoolean(key, value)
        commit()
    }
}

fun setPreference(context: Context, key: String, value: Int) {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putInt(key, value)
        commit()
    }
}

fun getPreferenceBool(context: Context, key: String): Boolean {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPref.getBoolean(key, false)
}

fun getPreferenceInt(context: Context, key: String): Int {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPref.getInt(key, 0)
}

fun getPreferenceString(context: Context, key: String): String {
    val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPref.getString(key, "")!!
}

fun stringToDate(date: String): Date? {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    return format.parse(date)
}

fun dateToString(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    return format.format(date)
}

fun stringToDouble(string: String): Double {
    return string.replace(".", "").replace(",", ".")
        .replace(Regex("[A-Za-z ]"), "").toDouble()
}

fun isConnectedToInternet(context: Context?): Boolean {
    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    activeNetwork?.isConnectedOrConnecting?.let { return it }
    return false
}

fun getErrorMessage(context: Context?, type: ErrorType): String {
    if (context == null)
        return ""
    return context.getString(type.message)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

//TODO move this to server side
val stores = arrayListOf("spar", "konzum", "muller", "lidl", "dm", "offertissima", "vrutak")

fun getStoreIconName(fullStoreName: String): String {
    stores.forEach {
        if (fullStoreName.contains(it, true))
            return "ic_$it"
    }
    return "ic_store"
}