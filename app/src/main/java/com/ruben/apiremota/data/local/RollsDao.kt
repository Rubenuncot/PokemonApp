package com.ruben.apiremota.data.local

import androidx.room.*

@Dao
interface RollsDao {

    @Query("SELECT * FROM RollsEntity ORDER by id DESC LIMIT 1")
    suspend fun getLast(): RollsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRolls(rolls: RollsEntity)

    @Delete
    suspend fun deleteRolls(rolls: RollsEntity)
}