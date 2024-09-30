package org.unam.georocks.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.util.Constants

@Dao
interface rocksDao {

    //Funciones para interactuar con la base de datos

    //Create
    @Insert
    suspend fun insertRock(rock: RockEntity)

    @Insert
    suspend fun insertRocks(rocks: MutableList<RockEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_ROCK_TABLE}")
    suspend fun getAllRocks(): MutableList<RockEntity>

    //Update
    @Update
    suspend fun updateRock(rock: RockEntity)

    //Delete
    @Delete
    suspend fun deleteRock(rock: RockEntity)
}
