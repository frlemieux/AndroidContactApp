package com.example.ui.details

import com.example.domain.model.Contact

sealed interface DetailsUiState {
    data object Loading : DetailsUiState

    data object Error : DetailsUiState

    data class Contact(
        val contact: ContactUi,
    ) : DetailsUiState
}

data class ContactUi(
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
)

fun Contact.toUi() =
    ContactUi(
        name = this.name?.toFullName() ?: "",
        phone = this.phone ?: "",
        email = this.email ?: "",
        address = this.location?.address() ?: "",
    )

private fun Contact.Name?.toFullName(): String? {
    this?.let {
        return "${this.title} ${this.last} ${this.first}"
    }
    return null
}

fun Contact.Location.address() = "\n${street?.number} ${street?.name}\n $city\n $state\n $country"
