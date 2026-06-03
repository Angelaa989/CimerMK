package mk.cimerapp.cimermk

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SavedPostEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedPostDao(): SavedPostDao
}