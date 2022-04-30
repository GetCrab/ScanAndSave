package hr.bm.scanandsave.ui.activities.main

import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import timber.log.Timber
import java.lang.StringBuilder
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegexInputFilter(private val regex: Regex) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        if (dest == null || source == null)
            return ""

        var value = StringBuilder(dest)
        value = value.replace(dstart, dend, source.toString())

        if (value.matches(regex))
            return source

        return ""
    }
}