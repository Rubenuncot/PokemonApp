package com.ruben.apiremota.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RollsEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "rolls") val rolls: Int,
    @ColumnInfo(name = "openDialog") val openDialog: Boolean,
)