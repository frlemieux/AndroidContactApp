package com.example.domain.usecase

import com.example.domain.model.Contact
import com.example.domain.repository.ContactRepository
import javax.inject.Inject


class GetContactById @Inject constructor(
    private val contactRepository: ContactRepository
) {

    suspend operator fun invoke(id: Int): Contact? {
        return contactRepository.getContactByIndex(id)
    }

}