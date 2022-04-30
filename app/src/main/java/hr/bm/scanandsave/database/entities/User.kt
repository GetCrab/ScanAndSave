package hr.bm.scanandsave.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var username: String = ""
)