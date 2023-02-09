package com.ruben.apiremota.data.local

import android.content.Context
import androidx.room.Room

private var db: PokemonDatabase? = null
fun getDatabase(applicationContext: Context): PokemonDatabase {
    if (db == null) {
        db = Room.databaseBuilder(
            applicationContext,
            PokemonDatabase::class.java, "tasks"
        ).build()
    }
    return db!!
}

fun closeDatabase() {
    db?.close()
}