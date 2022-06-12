package hr.bm.scanandsave.utils

data class ResourceStatus(val status: Status, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun success(): ResourceStatus {
            return ResourceStatus(Status.SUCCESS, null)
        }

        fun error(message: String): ResourceStatus {
            return ResourceStatus(Status.ERROR, message)
        }

        fun loading(): ResourceStatus {
            return ResourceStatus(Status.LOADING, null)
        }
    }
}