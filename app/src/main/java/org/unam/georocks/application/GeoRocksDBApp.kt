package org.unam.georocks.application

import android.app.Application
import org.unam.georocks.data.RocksRepository
import org.unam.georocks.data.db.RocksDatabase

class GeoRocksDBApp: Application() {

    private val database by lazy{
        RocksDatabase.getDatabase(this@GeoRocksDBApp)
    }

    val repository by lazy {
        RocksRepository(database.rocksDao())
    }

}
