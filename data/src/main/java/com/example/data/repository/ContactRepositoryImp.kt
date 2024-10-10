package com.example.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.local.ContactDao
import com.example.data.local.ContactDatabase
import com.example.data.mappers.toContact
import com.example.data.remote.ContactApi
import com.example.data.remote.ContactRemoteMediator
import com.example.domain.model.Contact
import com.example.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImp
    @Inject
    constructor(
        private val contactDao: ContactDao,
        private val contactApi: ContactApi,
        private val contactDatabase: ContactDatabase,
    ) : ContactRepository {
        override suspend fun getContactByIndex(id: Int): Contact? = contactDao.getById(id)?.toContact()

        @OptIn(ExperimentalPagingApi::class)
        override fun getPagingDataContact(query: String): Flow<PagingData<Contact>> =
            Pager(
                config = androidx.paging.PagingConfig(pageSize = 20),
                remoteMediator =
                    ContactRemoteMediator(
                        contactApi = contactApi,
                        contactDatabase = contactDatabase,
                    ),
                pagingSourceFactory = { contactDatabase.dao.pagingSource(query) },
            ).flow.map { pagingData ->
                pagingData.map { it.toContact() }
            }
    }
