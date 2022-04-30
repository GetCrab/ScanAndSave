package hr.bm.scanandsave.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

fun parsePrice(price: Double): String {
    return String.format("%.2f", price) + " HRK"
}

fun getPrice(price: String): Double {
    return price.split(" ")[0].toDouble()
}

fun parseDate(date: Date?): String {
    if (date == null)
        return ""

    return try {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateFormatter.format(date)
    } catch (t: Throwable) {
        //TODO handle catch
        ""
    }
}