package com.example.project

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TitleDBEntity::class],version = 2)//, version = 2)
/*    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ])*/

abstract class TitleRoomDB: RoomDatabase() {
    abstract fun titleDAO(): TitleDBDAO

    companion object {
        private var instance: TitleRoomDB? = null
        fun getInstance(context: Context): TitleRoomDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context,TitleRoomDB::class.java,"titles.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as TitleRoomDB
        }
    }
}