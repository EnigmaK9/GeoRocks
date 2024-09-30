package org.unam.georocks.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.unam.georocks.util.Constants

@Entity(tableName = Constants.DATABASE_ROCK_TABLE)
data class RockEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rock_id")
    var id: Long = 0,
    @ColumnInfo(name = "rock_name")
    var name: String,
    @ColumnInfo(name = "rock_type")
    var type: String,
    @ColumnInfo(name = "rock_origin", defaultValue = "Unknown")
    var origin: String
)
