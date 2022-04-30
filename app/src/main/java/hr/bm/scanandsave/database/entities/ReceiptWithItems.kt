package hr.bm.scanandsave.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ReceiptWithItems(
    @Embedded val receipt: Receipt,
    @Relation(
        parentColumn = "receiptId",
        entityColumn = "receiptId"
    )
    var items: List<ReceiptItem>,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    var category: Category
)