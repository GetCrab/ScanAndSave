package hr.bm.scanandsave.utils

import com.microblink.Media
import com.microblink.core.ScanResults

class RecognizerResults private constructor(
    private val results: ScanResults?,
    private val media: Media?,
    private var e: Throwable?
) {
    constructor(results: ScanResults?, media: Media?) : this(results, media, null) {}
    constructor(e: Throwable) : this(null, null, e) {}

    fun results(): ScanResults? {
        return results
    }

    fun media(): Media? {
        return media
    }

    fun e(): Throwable? {
        return e
    }

    fun e(e: Throwable): RecognizerResults {
        this.e = e
        return this
    }

    override fun toString(): String {
        return "RecognizerResults{" +
                "results=" + results +
                ", media=" + media +
                ", e=" + e +
                '}'
    }
}