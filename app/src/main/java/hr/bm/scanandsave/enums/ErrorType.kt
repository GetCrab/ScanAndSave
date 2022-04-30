package hr.bm.scanandsave.enums

import hr.bm.scanandsave.R

enum class ErrorType(val message: Int) {
    NO_ERROR(R.string.empty),
    PASSWORD_ERROR(R.string.wrong_password),
}