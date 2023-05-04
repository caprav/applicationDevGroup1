package com.example.project

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//VPC - DAO for the DB data
@Dao
interface TitleDBDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTitle(title: TitleDBEntity)

    @Update
    fun updateTitle(title: TitleDBEntity)

    @Delete
    fun deleteTitle(title: TitleDBEntity)

    //VPC - this will be the function to use to find a title based on the watchnow API ID
    @Query("SELECT * FROM titles_table WHERE id LIKE :titleID")
    fun findWatchNOWIDMatch(titleID: Int): TitleDBEntity

    //VPC - this will be the function to use to find a title based on the universal IMDB ID
    // which is in the string format 'tt0109707'
    @Query("SELECT * FROM titles_table WHERE imdb_id LIKE :IMDBid")
    fun findimdbIDMatch(IMDBid: String): TitleDBEntity


    // SELECT COUNT(column_name) FROM table_name;
    @Query ("SELECT COUNT(id) FROM titles_table WHERE imdb_id LIKE :IMDBid")
    fun imdbIDMatchExsists(IMDBid: String): Int

    //this will clear all data from the existing table. Should be done on app startup
    @Query("DELETE FROM titles_table")
    public fun clearTitleTable()

    // should create a count query so you can check how many records are present
    // from a button click on the interface. That could be helpful in troubleshooting

}