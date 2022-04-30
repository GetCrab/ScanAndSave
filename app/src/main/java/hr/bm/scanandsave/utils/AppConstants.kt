package hr.bm.scanandsave.utils

class AppConstants {
    companion object {
        const val API_BASE_URL = "http://mobile.asseco-see.hr/"

        const val PREFERENCE_NAME = "banking_preference"
        const val REGISTER_PREFERENCE = "isRegistered"
        const val CURRENT_USER_PREFERENCE = "currentUserId"
        const val PIN_PREFERENCE = "pin"
        const val IV_PREFERENCE = "ivBytes"

        const val MIN_PIN_LENGTH = 4
        const val MAX_PIN_LENGTH = 6
    }
}