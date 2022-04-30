package hr.bm.scanandsave.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = Category::class,
    parentColumns = arrayOf("categoryId"),
    childColumns = arrayOf("categoryId"),
    onDelete = ForeignKey.NO_ACTION
)])
data class Receipt(
    @PrimaryKey(autoGenerate = true) var receiptId: Long = 0,
    val merchant: String,
    var date: Date?,
    val totalPrice: Double,
    val currency: Currency = Currency.HRK,
    val receiptType: ReceiptType,
    var categoryId: Long
)