package com.example.ui.list

import androidx.compose.ui.graphics.Color
import com.example.domain.model.Contact

data class ContactUi(
    val index: Int,
    val capitalLetter: String,
    val contactName: String,
    val gender: String,
    val capitalLetterBackgroundColor: Color,
    val image : String,
)

fun Contact.toUi() = ContactUi(
    index = index,
    capitalLetter = name?.last?.first()?.uppercase() ?: "",
    capitalLetterBackgroundColor = gender?.toColor() ?: Color.Gray,
    contactName = "${name?.first} ${name?.last}",
    gender = gender ?: "",
    image = picture?.thumbnail ?: ""
)

fun String.toColor(): Color = when (this) {
    "female" -> Color.Magenta
    "male" -> Color.Blue
    else -> Color.Gray
}
