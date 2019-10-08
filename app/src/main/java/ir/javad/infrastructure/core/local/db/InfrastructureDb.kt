package ir.javad.infrastructure.core.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import ir.javad.infrastructure.view.mainActivity.data.MainActivityDao
import ir.javad.infrastructure.view.mainActivity.model.MainActivityEntity

@Database(
    entities = [MainActivityEntity::class],
    version = 1,
    exportSchema = false
)

abstract class InfrastructureDb : RoomDatabase() {

    abstract fun mainActivityDao(): MainActivityDao
}
