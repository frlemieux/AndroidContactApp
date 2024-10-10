package com.example.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsert(contact: List<ContactEntity>)

    @Query("SELECT * FROM ContactEntity WHERE `name_first` LIKE '%' || :query || '%'")
    fun pagingSource(query: String): PagingSource<Int, ContactEntity>

    @Query("SELECT * FROM ContactEntity WHERE `index`=:index")
    suspend fun getById(index: Int): ContactEntity?

    @Query("DELETE FROM ContactEntity")
    suspend fun clearAll()
}
