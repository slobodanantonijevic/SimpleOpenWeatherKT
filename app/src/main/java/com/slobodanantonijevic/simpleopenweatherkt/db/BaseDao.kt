package com.slobodanantonijevic.simpleopenweatherkt.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable

interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    fun insert(vararg obj: T): Completable
}