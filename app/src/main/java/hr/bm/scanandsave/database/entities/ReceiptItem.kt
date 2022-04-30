package hr.bm.scanandsave.database.entities

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = Receipt::class,
    parentColumns = arrayOf("receiptId"),
    childColumns = arrayOf("receiptId"),
    onDelete = ForeignKey.CASCADE
)])
data class ReceiptItem(
    @PrimaryKey(autoGenerate = true) var itemId: Long = 0,
    @ColumnInfo(index = true) var receiptId: Long,
    var name: String,
    val description: String,
    var price: Double,
    val quantity: Int
)