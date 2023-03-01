package com.ruben.apiremota.data.local

import android.content.Context

class RollsDatasource constructor(applicationContext: Context){
    private val db = getDatabase(applicationContext).rollsDao()
    suspend fun getLast(): RollsEntity{
        return db.getLast()
    }

    suspend fun insertRolls(rolls: Int, openDialog: Boolean){
        return db.insertRolls(RollsEntity(rolls = rolls, openDialog = openDialog))
    }

    suspend fun deleteRolls(rolls: Int, openDialog: Boolean){
        return db.deleteRolls(RollsEntity(rolls = rolls, openDialog = openDialog))
    }
}