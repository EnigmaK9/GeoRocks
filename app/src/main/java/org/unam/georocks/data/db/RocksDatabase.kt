package org.unam.georocks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.util.Constants

@Database(
    entities = [RockEntity::class],
    version = 1, // Database version for migrations
    exportSchema = true // Default is true.
)
abstract class RocksDatabase : RoomDatabase() {
    // Here goes the DAO
    abstract fun rocksDao(): rocksDao

    // Without dependency injection, we instantiate the database
    // here with a singleton pattern

    companion object {

        @Volatile
        private var INSTANCE: RocksDatabase? = null

        fun getDatabase(context: Context): RocksDatabase {
            // If the instance is not null, we return the one we already have
            // If it is null, we create a new instance and return it
            // (singleton pattern)

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RocksDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
