package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContactByIndex(id: Int): Contact?

    fun getPagingDataContact(query: String): Flow<PagingData<Contact>>
}
