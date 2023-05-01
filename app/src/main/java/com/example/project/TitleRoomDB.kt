package com.example.project

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TitleDBEntity::class], version = 2)
abstract class TitleRoomDB: RoomDatabase() {
    abstract fun titleDAO(): TitleDBDAO
}