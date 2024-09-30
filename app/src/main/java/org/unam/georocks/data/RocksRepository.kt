package org.unam.georocks.data

import org.unam.georocks.data.db.RocksDao
import org.unam.georocks.data.db.model.RockEntity

class RocksRepository(
    private val rocksDao: RocksDao
) {

    suspend fun insertRock(rock: RockEntity) {
        rocksDao.insertRock(rock)
    }

    suspend fun getAllRocks(): MutableList<RockEntity> = rocksDao.getAllRocks()

    suspend fun updateRock(rock: RockEntity) {
        rocksDao.updateRock(rock)
    }

    suspend fun deleteRock(rock: RockEntity) {
        rocksDao.deleteRock(rock)
    }
}
