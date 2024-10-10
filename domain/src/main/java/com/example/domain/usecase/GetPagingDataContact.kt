package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.model.Contact
import com.example.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagingDataContact
    @Inject
    constructor(
        private val contactRepository: ContactRepository,
    ) {
        operator fun invoke(query: String): Flow<PagingData<Contact>> = contactRepository.getPagingDataContact(query)
    }
